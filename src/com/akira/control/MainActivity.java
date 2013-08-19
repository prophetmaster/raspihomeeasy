package com.akira.control;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import com.akira.control.RaspiListener;
import com.akira.control.RaspiAdapter;
import com.akira.control.RaspiListView;
import com.akira.control.RaspiAsyncTask;


public class MainActivity extends SherlockFragmentActivity implements RaspiListener {

	private static final int RESULT_SETTINGS = 1;
	static final int RESULT_LOAD_IMAGE = 1;
	
	public static ArrayList<RaspiListView> devicelist;
	public static RaspiAsyncTask myTask = null;
	//public static JSONArray jsonArray = new JSONArray();
	
	public static String URL;
	public static String USERNAME;
	public static String PASSWORD;

	static SharedPreferences SP;
	ActionBar actionBar;
	static RaspiAdapter myAdapter;
	static ListView myList;
	static LinearLayout demolayout;
	
	public static ListView listView;
	
	protected static RaspiListView ID_IMAGE;

	Button btnallon;
	Button btnalloff;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);

		
		devicelist = new ArrayList<RaspiListView>();

		// Retrieve preferences
		getPref();

		// Action bar Sherlock
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);

		btnallon = (Button)findViewById(R.id.buttonAllOn);
		btnallon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				for (int i=0; i < devicelist.size(); i++) {
		            
					 String device = devicelist.get(i).getDeviceId().toString();
					 new RaspiAsyncTask(MainActivity.this).execute(device, "1");
					 Log.i("TEST", "device : "+device);
			    }
			}
		});
		
		btnalloff = (Button)findViewById(R.id.buttonAllOff);
		btnalloff.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for (int i=0; i < devicelist.size(); i++) {
		            
					 String device = devicelist.get(i).getDeviceId().toString();
					 new RaspiAsyncTask(MainActivity.this).execute(device, "0");
					 Log.i("TEST", "device : "+device);
			    }
			}
		});
		
		demolayout = (LinearLayout)findViewById(R.id.LinearLayoutDemo);
		myList = (ListView) findViewById(R.id.listViewResults);
		myList.setAdapter(myAdapter = new RaspiAdapter(devicelist, this));	
		myAdapter.notifyDataSetChanged();
		
		SwitchingDemo();

	}	
	
	
	public static void SwitchingDemo() {
		// TODO Auto-generated method stub
		if (devicelist.size() <= 0) {
			demolayout.setVisibility(View.VISIBLE);		
		} else {
			demolayout.setVisibility(View.GONE);
		}
	}


	public void getPref() {
				
		SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		URL = SP.getString("url", "");
		USERNAME = SP.getString("username", "");
		PASSWORD = SP.getString("password", "");
		
		String test = SP.getString("devicelist", "");	
		JSONObject jsonResponse;
		
		try {
			jsonResponse = new JSONObject(test);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("devicelist");
			
			for(int i = 0; i<jsonMainNode.length();i++){
				   
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				
				RaspiListView Detail = new RaspiListView();
	        	Detail.setDeviceId(jsonChildNode.optString("deviceid"));
	        	Detail.setDesc(jsonChildNode.optString("desc"));
	        	devicelist.add(Detail);
				   
		  }
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setPref(String mypref, boolean value) {
		// Retrieve preferences
		SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		Editor editor = SP.edit();
		editor.putBoolean(mypref, value);
		editor.commit();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);
	}

	// Define menu action
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {

		case R.id.add:
			
			final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			
			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.dialogadd);
			dialog.setTitle("Add receptor");
			
			final EditText etid = (EditText) dialog.findViewById(R.id.editTextId);
			final EditText etdesc = (EditText) dialog.findViewById(R.id.editTextDesc);
			
			Button buttonCancel = (Button) dialog.findViewById(R.id.buttonCancel);
			Button buttonAdd = (Button) dialog.findViewById(R.id.buttonAdd);
			
			buttonCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.cancel();
				}
			});
			
			buttonAdd.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					String text = etid.getText().toString().trim();
	                String desc = etdesc.getText().toString().trim();
	                
	                
	                RaspiListView Detail = new RaspiListView();
	        		Detail.setDeviceId(text);
	        		Detail.setDesc(desc);
	        		devicelist.add(Detail);
	        		myAdapter.notifyDataSetChanged();
	 	        		        		
	                dialog.dismiss();
	                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);          
	                
	                setDeviceListPref(MainActivity.this);
	                
	                SwitchingDemo();
	                //Log.i("TEST", "\"devicelist\":"+jsonArray.toString());
				}
			});
			
			dialog.show();
			
	        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	        
			break;
			
		case R.id.menu_settings:
			Intent intent_setting = new Intent(MainActivity.this, Preferences.class);
			startActivityForResult(intent_setting, RESULT_SETTINGS);
			break;

		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SETTINGS:
			// Retrieve preferences
			SharedPreferences SP = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());

			URL = SP.getString("url", "");
			USERNAME = SP.getString("username", "");
			PASSWORD = SP.getString("password", "");
			break;

		}

	}
	
	
	
	
	public static void setDeviceListPref(Context context){

		JSONArray jsonArray = new JSONArray();
		Log.i("BEFORE", "\"devicelist\":"+jsonArray.toString());
		for (int i=0; i < devicelist.size(); i++) {
            jsonArray.put(devicelist.get(i).getJSONObject());
	    }
	    
	    SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = SP.edit();
		editor.putString("devicelist", "{\"devicelist\":"+jsonArray.toString()+"}");
		editor.commit();
		
		Log.i("AFTER", "\"devicelist\":"+jsonArray.toString());
	}

	@Override
	public void onTaskComplete() {
		// Response from async task
		
	}	
	
	@Override
    protected void onDestroy() {
        super.onDestroy();
 
        
    }

	
}