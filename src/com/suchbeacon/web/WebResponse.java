package com.suchbeacon.web;

import java.util.ArrayList;
import java.util.List;

public class WebResponse {
	private String status;
	private String data;
	private List<GlassResponse> glassResp;
	
	public WebResponse(String status, String data) {
		this.status = status;
		this.data = data;
		this.glassResp = new ArrayList<GlassResponse>();
	}

	public WebResponse(String status, String data, List<GlassResponse> glassResp) {
		this.status = status;
		this.data = data;
		this.glassResp = glassResp;
	}
	
}
