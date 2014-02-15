package com.suchbeacon.web;

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
	
}
