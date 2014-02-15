package com.suchbeacon.web;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class GlassResponse {
	private String status;
	private String message;
	
	public String getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
	
	public GlassResponse(String status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
	
	public JSONObject toJson() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("status", status);
		try{
			JSONObject mirrorResp = new JSONObject(message);
			json.put("message", mirrorResp);
		} catch (JSONException e) {
			json.put("message", message);
		}
		return json;
	}
	
	
}
