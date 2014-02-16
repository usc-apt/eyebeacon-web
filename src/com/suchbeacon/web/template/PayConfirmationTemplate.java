package com.suchbeacon.web.template;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.suchbeacon.web.Card;
import com.suchbeacon.web.Template;
import com.suchbeacon.web.Card.ActionItem;

public class PayConfirmationTemplate extends Template {
	private String name;
	private double price;
	private String imageUrl;
	private String location;
	private boolean paid;
	
	@Override
	public List<Card> render() {
		String bundleId = generateBundleId();
		List<Card> cards = new ArrayList<Card>();
		List<ActionItem> actionItems = new ArrayList<ActionItem>();
		String paidText = paid?"Item Purchased!":"Payment failed...";
		String paidDesc = paid?
				"<p class=\"text-small\">Pick up at " + location + "</p>":
					"<p class=\"text-small\">Please check your Venmo account.</p>";
		
		String html = "<article>" 
				+ "<figure>" + "<img src=\"" + imageUrl + "\" width=\"100%\" height=\"100%\">" + "</figure>"
				+ "<section>"
				+ "<h1 class=\"text-normal\">" + paidText + "</h1>"
				+ "<hr>"
				+ "<p class=\"text-minor\">$" + price + "</p>"
				+	paidDesc
				+ "</section>" 
				+ "</article>";
		return null;
	}

	@Override
	public JSONObject toJson() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("name", name);
		json.put("price", price);
		json.put("imageUrl", imageUrl);
		json.put("location", location);
		json.put("paid", paid);
		
		return json;
	}

}
