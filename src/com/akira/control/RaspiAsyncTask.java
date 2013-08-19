package com.akira.control;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;


public class RaspiAsyncTask extends AsyncTask<Object, String, String> {

	SharedPreferences SP;
	public static String URL;
	public static String USERNAME;
	public static String PASSWORD;
	
	public Context ctx;


	public RaspiAsyncTask(Context context) {

		this.ctx = context;

	}

	@Override
	protected String doInBackground(Object... object) {
		
		SP = PreferenceManager.getDefaultSharedPreferences(ctx);
		URL = SP.getString("url", "");
		USERNAME = SP.getString("username", "");
		PASSWORD = SP.getString("password", "");
		
		String device = (String) object[0];
		String command = (String) object[1];
		String response = null;
		HttpEntity resEntity = null;

		Log.i("TEST","url : "+MainActivity.URL);
		Log.i("TEST","username : "+MainActivity.USERNAME);
		Log.i("TEST","password : "+MainActivity.PASSWORD);
		Log.i("TEST","device : "+device);
		Log.i("TEST","command : "+command);
		
		try {
			HttpClient client = new DefaultHttpClient();
			String postURL = MainActivity.URL;
			HttpPost post = new HttpPost(postURL);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", MainActivity.USERNAME));
			params.add(new BasicNameValuePair("password", MainActivity.PASSWORD));

			params.add(new BasicNameValuePair("device", device));
			params.add(new BasicNameValuePair("command", command));

			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
					HTTP.UTF_8);
			post.setEntity(ent);
			HttpResponse responsePOST = client.execute(post);
			resEntity = responsePOST.getEntity();
			if (resEntity != null) {
				Log.i("RESPONSE",EntityUtils.toString(resEntity));
				//response = EntityUtils.toString(resEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	protected void onProgressUpdate(String... s) {
		super.onProgressUpdate(s);
		// Log.i(getClass().getSimpleName(), ""+s[0]);
	}

	@Override
	protected void onPostExecute(String response) {
		super.onPostExecute(response);

		if (response != null) {

			
		}
		
	}

}