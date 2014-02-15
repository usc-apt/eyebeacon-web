package com.suchbeacon.web;

import java.util.List;

import com.google.gson.Gson;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Content {
	@Id
	Long id;
	@Index int majorId;
	@Index int minorId;

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
	
	public List<Card> buildCards() {
		if(cards == null) {
			cards = Template.build(templateName, templateDataJson).render();
		}
		return cards;
	}
	
	public String toJson() {
		buildCards();
		return new Gson().toJson(this);
	}
}
