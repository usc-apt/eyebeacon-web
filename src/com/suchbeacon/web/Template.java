package com.suchbeacon.web;

import java.util.List;
import java.util.Random;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.gson.Gson;
import com.suchbeacon.web.template.InfoTemplate;
import com.suchbeacon.web.template.IntroTemplate;
import com.suchbeacon.web.template.PaymentTemplate;

public abstract class Template {
	// returns html template to feed into Mirror API
	public abstract List<Card> render();
	
	// returns JSONObject
	public abstract JSONObject toJson() throws JSONException;
	
	protected String generateBundleId() {
		byte[] randomBytes = new byte[36];
		new Random().nextBytes(randomBytes);
		return randomBytes.toString();
	}

	public static Template build(String templateName, String jsonData) {
		Gson gson = new Gson();
		System.out.println("Building Template " + templateName);
		if (templateName.equals("intro")) {
			return gson.fromJson(jsonData, IntroTemplate.class);
		} else if (templateName.equals("info")) {
			return gson.fromJson(jsonData, InfoTemplate.class);
		} else if (templateName.equals("payment")) {
			return gson.fromJson(jsonData, PaymentTemplate.class);
		}
		return null;
	}
}
