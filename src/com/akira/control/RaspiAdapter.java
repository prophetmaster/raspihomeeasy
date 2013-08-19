package com.akira.control;

import java.util.ArrayList;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class RaspiAdapter extends BaseAdapter {

	

	Context _context;
	

	public class ViewHolder {
		public TextView deviceid;
		public TextView desc;
		public ImageView edit;
		public ImageView capture;
		public ImageView delete;
		public Button buttonOn;
		public Button buttonOff;
	}

	private ArrayList<RaspiListView> _data;
	Context _c;

	public RaspiAdapter(ArrayList<RaspiListView> data, Context context) {
		_data = data;
		_context = context;
	}

	@Override
	public int getCount() {
		// return size in int of the listview
		return _data.size();
	}

	@Override
	public Object getItem(int position) {
		// return position int for row
		return _data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// return id of item
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		// Configure the view holder
		View view = convertView;
		final ViewHolder holder;

				
		if (view == null) {

			LayoutInflater vi = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.list_item, parent, false);

			holder = new ViewHolder();
			
			
			
			holder.deviceid = (TextView) view.findViewById(R.id.deviceid);
			holder.desc 	= (TextView) view.findViewById(R.id.description);
			
			holder.edit 	= (ImageView) view.findViewById(R.id.edit);
			
			holder.buttonOn = (Button) view.findViewById(R.id.buttonON);
			holder.buttonOff = (Button) view.findViewById(R.id.buttonOFF);
			holder.delete 	= (ImageView) view.findViewById(R.id.delete);
			
			
			
			
			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		final RaspiListView item = _data.get(position);

		holder.deviceid.setText(item.deviceid);
		holder.desc.setText(item.desc);
				
		holder.edit.setOnClickListener(
	            new Button.OnClickListener() {
	                @Override
	                public void onClick(View v) {

	                	final InputMethodManager imm = (InputMethodManager) _context.getSystemService(Context.INPUT_METHOD_SERVICE);
	        			
	        			final Dialog dialog = new Dialog(_context);
	        			dialog.setContentView(R.layout.dialogadd);
	        			dialog.setTitle("Edit receptor");
	        			
	        			final EditText etid = (EditText) dialog.findViewById(R.id.editTextId);
	        			final EditText etdesc = (EditText) dialog.findViewById(R.id.editTextDesc);
	        			
	        			etid.setText(item.deviceid);
	        			etdesc.setText(item.desc);
	        			
	        			
	        			Button buttonCancel = (Button) dialog.findViewById(R.id.buttonCancel);
	        			Button buttonEdit = (Button) dialog.findViewById(R.id.buttonAdd);
	        			buttonEdit.setText("Save");
	        			buttonCancel.setOnClickListener(new OnClickListener() {
	        				@Override
	        				public void onClick(View v) {
	        					dialog.cancel();
	        					InputMethodManager mgr = (InputMethodManager)(v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
	        	                mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
	        				}
	        			});
	        			
	        			buttonEdit.setOnClickListener(new OnClickListener() {
	        				@Override
	        				public void onClick(View v) {
	        					
	        					String text = etid.getText().toString().trim();
	        	                String desc = etdesc.getText().toString().trim();
	        	                
	        	                RaspiListView Detail;
	        	        		Detail = new RaspiListView();
	        	        		Detail.setDeviceId(text);
	        	        		Detail.setDesc(desc);
	        	        		
	        	        		MainActivity.devicelist.set(position,Detail);
	        	        		MainActivity.myAdapter.notifyDataSetChanged();
	        	        		
	        	        		MainActivity.setDeviceListPref(_context);
	        	        			        	        		
	        	                dialog.dismiss();
	        	                InputMethodManager mgr = (InputMethodManager)(v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
	        	                mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
	        				}
	        			});
	        			
	        			dialog.show();
	        			
	        	        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	                }
	            }
	        );
		
		
		
		
		holder.delete.setOnClickListener(
	            new Button.OnClickListener() {
	                @Override
	                public void onClick(View v) {
	                	AlertDialog.Builder adb=new AlertDialog.Builder(_context);
	                    adb.setTitle("Delete?");
	                    adb.setMessage("Are you sure you want to delete " + item.deviceid);
	                    adb.setNegativeButton("Cancel", null);
	                    adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
	                        public void onClick(DialogInterface dialog, int which) {
	                        	MainActivity.devicelist.remove(position);		                        	
	                        	notifyDataSetChanged();
	                        	MainActivity.setDeviceListPref(_context);
	                        	MainActivity.SwitchingDemo();
	                        }});
	                    adb.show();
	                }
	            }
	        );
		
		
		holder.buttonOn.setOnClickListener( new View.OnClickListener() { 
		     public void onClick(View v) {  
		    	 
		    	 new RaspiAsyncTask(_context).execute(item.deviceid.toString(), "1");
		    	 Toast.makeText(_context, "Device " + item.deviceid + " : "+item.desc+" turned ON", Toast.LENGTH_SHORT).show();
		     } 
	    });
		
		
		holder.buttonOff.setOnClickListener( new View.OnClickListener() { 
		     public void onClick(View v) {  
		    	
		    	 new RaspiAsyncTask(_context).execute(item.deviceid.toString(), "0");
		    	 Toast.makeText(_context, "Device " + item.deviceid + " : "+item.desc+" turned OFF", Toast.LENGTH_SHORT).show();

		     } 
	    });  
		
		
		return view;
	}

	
}