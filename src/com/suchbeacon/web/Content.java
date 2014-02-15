package com.suchbeacon.web;

import java.util.List;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.gson.Gson;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

/*
 * Content holds templateName and templateDataJson.
 * With those data, we can build a Template, which will be turned into Cards.
 */
@Entity
public class Content {
	@Id
	Long id;
	@Index int majorId;
	@Index int minorId;

	@Ignore Template template;
	@Ignore List<Card> cards;
	
	transient String templateName;
	transient String templateDataJson;
	
	private Content() {}
	
	public Content(int majorId, int minorId, String templateName, String templateDataJson) {
		this.majorId = majorId;
		this.minorId = minorId;
		this.templateName = templateName;
		this.templateDataJson = templateDataJson;
	}
	
	public Template buildTemplate() {
		if(template == null) {
			template = Template.build(templateName, templateDataJson);
		}
		return template;
	}
	
	public List<Card> buildCards() {
		if(cards == null) {
			cards = buildTemplate().render();
		}
		return cards;
	}
	
	public JSONObject toJson() {
		buildCards();
		// IMPORTANT: Renders json for TEMPLATE, not card.
		try {
			return template.toJson();
		} catch (JSONException e) {
			System.out.println("Failed to build JSON for " + majorId + "/" + minorId);
			return null;
		}
	}
}
