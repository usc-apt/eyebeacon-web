package com.suchbeacon.web;

import java.util.List;

import com.google.gson.Gson;
import com.suchbeacon.web.template.IntroTemplate;

public abstract class Template {
	// returns html template to feed into Mirror API
	public abstract List<String> render();

	public static Template build(String templateName, String jsonData) {
		Gson gson = new Gson();
		System.out.println("Building Template " + templateName);
		if (templateName.equals("intro")) {
			return gson.fromJson(jsonData, IntroTemplate.class);
		}
		return null;
	}
}
