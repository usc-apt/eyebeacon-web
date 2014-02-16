package com.suchbeacon.web.template;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.suchbeacon.web.Card;
import com.suchbeacon.web.Card.ActionItem;
import com.suchbeacon.web.Template;

public class IntroTemplate extends Template {
	String name;
	String imageUrl;
	Exhibit[] exhibits;
	
	private IntroTemplate() { }

	@Override
	public List<Card> render() {
		System.out.println("Rendering IntroTemplate");
		List<Card> cards = new ArrayList<Card>();
		String bundleId = generateBundleId();
		
		cards.add(new Card("<article class=\"photo\">"
				+ "<img src=\"" + imageUrl + "\" width=\"100%\" height=\"100%\" />"
				+ "<div class=\"overlay-gradient-tall-dark\" />"
				+ "<section>" + "<p class=\"text-auto-size\">" + name + "</p>" + "</section>"
				+ "</article>", bundleId, "DELETE").setIsBundleCover(true));

		for (Exhibit e : exhibits) {
			List<ActionItem> actionItems = new ArrayList<ActionItem>();
			
			if(e.videoUrl != null)
				actionItems.add(new ActionItem("PLAY_VIDEO", e.videoUrl));
		
			actionItems.add(new ActionItem("DELETE"));
				
			cards.add(new Card("<article class=\"photo\">"
					+ "<img src=\"" + e.imageUrl + "\" width=\"100%\" height=\"100%\" />"
					+ "<div class=\"overlay-gardient-tall-dark\" />"
					+ "<section>" + "<p class=\"text-auto-size\">"
					+ e.name
					+ (e.videoUrl != null ? "<img src=\"http://ptzlabs.com/play.png\" style=\"margin-left: 15px\" />" : "")
					+ "</p>" + "</section>"
					+ "</article>", bundleId, actionItems ));
		}
		return cards;
	}

	static class Exhibit {
		String name;
		double[] location;
		String imageUrl;
		String videoUrl;
		
		public Exhibit() { }
		public JSONObject toJson() throws JSONException {
			JSONObject json = new JSONObject();
			json.put("name", name);
			json.put("imageUrl", imageUrl);
			json.put("videoUrl", videoUrl);
			
			JSONArray locationArray = new JSONArray();
			locationArray.put(location[0]);
			locationArray.put(location[1]);
			json.put("location", locationArray);
			
			return json;
		}
	}

	@Override
	public JSONObject toJson() throws JSONException {
		JSONObject json = new JSONObject();
		
		json.put("name", name);
		json.put("imageUrl", imageUrl);
		
		JSONArray exhibitsArray = new JSONArray();
		for(Exhibit e : exhibits) {
			exhibitsArray.put(e.toJson());
		}
		json.put("exhibits", exhibitsArray);
		
		return json;
	}

}
