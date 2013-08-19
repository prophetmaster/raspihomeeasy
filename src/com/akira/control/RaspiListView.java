package com.akira.control;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class RaspiListView {

	public String deviceid;
	public String desc;

	
	public String getDeviceId() {
		return deviceid;
	}

	public void setDeviceId(String deviceid) {
		this.deviceid = deviceid;
	}
	
	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("deviceid", deviceid);
            obj.put("desc", desc);
        } catch (JSONException e) {
            
            Log.i("TEST","DefaultListItem.toString JSONException: "+e.getMessage());
        }
        return obj;
    }
}