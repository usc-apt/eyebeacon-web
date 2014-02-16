package com.suchbeacon.web;

import java.util.List;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class WebResponse {
	private String status;
	private String message;
	private JSONObject data;
	private List<GlassResponse> glassResp;
	
	public WebResponse(String status, String message) {
		this.status = status;
		this.message = message;
	}
	
	public WebResponse(String status, JSONObject data) {
		this.status = status;
		this.data = data;
	}

	public WebResponse(String status, JSONObject data, List<GlassResponse> glassResp) {
		this.status = status;
		this.data = data;
		this.glassResp = glassResp;
	}
	
	public JSONObject toJson() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("status", status);
		if (message != null) { json.put("message", message); }
		if (data != null) { json.put("data", data); }
		
		if (glassResp != null) {
			JSONArray grArray = new JSONArray();
			for (GlassResponse gr : glassResp) {
				grArray.put(gr.toJson());
			}
			json.put("glassResp", grArray);
		}
		
		return json;
	}
	
}
