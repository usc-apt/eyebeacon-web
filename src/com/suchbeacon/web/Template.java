package com.suchbeacon.web;

import java.util.List;
import java.util.Random;

import com.google.gson.Gson;
import com.suchbeacon.web.template.IntroTemplate;

public abstract class Template {
	// returns html template to feed into Mirror API
	public abstract List<Card> render();
	
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
		}
		return null;
	}
}
