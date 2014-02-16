package com.suchbeacon.web.template;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.suchbeacon.web.Card;
import com.suchbeacon.web.Card.ActionItem;
import com.suchbeacon.web.Template;

public class InfoTemplate extends Template {
	private String name;
	private String location;
	private String imageUrl;
	private String videoUrl;
	private String speakableText;
	private Info[] infos;

	@Override
	public List<Card> render() {
		List<ActionItem> actionItems = new ArrayList<ActionItem>();
		System.out.println("Rendering InfoTemplate");
		List<Card> cards = new ArrayList<Card>();
		String bundleId = generateBundleId();
		
		if (speakableText == null) {
			speakableText = name; 
		}
		speakableText += " ";	
		
		String html = "<article class=\"cover-only\">"
				+ "<figure>"
				+ "<img src=\"" + imageUrl + "\" width=\"100%\" height=\"100%\">"
				+ "</figure>" 
				+ "<section>" 
				+ "<h1 class=\"text-large\">" + name 
				+ (videoUrl != null ? "<img src=\"http://ptzlabs.com/play.png\" style=\"margin-left: 15px\" />" : "")
				+ "</h1>"
				+ "<hr>"
				+ "<p class=\"text-small\">" + "Located In:" + "</p>" 
				+ "<p class=\"text-minor\">" + location + "</p>" 
				+ "</section>"
				+ "</article>";

		//Adds speakable text only if we have info to tell the user
		for (Info i : infos) {
			html += "<article class=\"auto-paignate\">"
					//+ "<section>"
					+ "<h1 class=\"text-large\">" + i.section + "</h1>"
					+ "<p class=\"text-small\">" + i.desc + "</p>"
					//+ "</section>"
					+ "</article>";
			speakableText += i.section + " " + i.desc + " ";
		}
		
		if(videoUrl != null)
			actionItems.add(new ActionItem("PLAY_VIDEO", videoUrl));
		if(!speakableText.isEmpty())
			actionItems.add(new ActionItem("READ_ALOUD"));
		actionItems.add(new ActionItem("DELETE"));
		
		//We can setSpeakableText anyway because there will not be a read_aloud action
		cards.add((new Card(html, bundleId, actionItems).setSpeakableText(speakableText)));

		return cards;
	}

	static class Info {
		private String section;
		private String desc;

		public Info() { }
		public JSONObject toJson() throws JSONException {
			JSONObject json = new JSONObject();
			json.put("section", section);
			json.put("desc", desc);
			return json;
		}
	}
	
	public JSONObject toJson() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("name", name);
		json.put("location", location);
		json.put("imageUrl", imageUrl);
		json.put("videoUrl", videoUrl);
		json.put("speakableText", speakableText);
		
		JSONArray infoArray = new JSONArray();
		for(Info i : infos) {
			infoArray.put(i.toJson());
		}
		json.put("infos", infoArray);
		
		return json;
	}
}
