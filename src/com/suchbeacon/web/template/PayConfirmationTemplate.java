package com.suchbeacon.web.template;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.suchbeacon.web.Card;
import com.suchbeacon.web.Item;
import com.suchbeacon.web.Template;
import com.suchbeacon.web.Card.ActionItem;

public class PayConfirmationTemplate extends Template {
	private boolean paid;
	private Long itemId;

	@Override
	public List<Card> render() {
		Item item = Item.findItem(itemId);
		String bundleId = generateBundleId();
		List<Card> cards = new ArrayList<Card>();
		List<ActionItem> actionItems = new ArrayList<ActionItem>();
		
		String paidText = paid ? "Item Purchased!" : "Payment failed...";
		String paidDesc = paid ? "<p class=\"text-small\">Pick up at " + item.getLocation() + "</p>"
				: "<p class=\"text-small\">Please check your Venmo account.</p>";

		String html = "<article>" 
				+ "<figure>" 
				+ "<img src=\"" + item.getImageUrl() + "\" width=\"100%\" height=\"100%\">"
				+ "</figure>" 
				+ "<section>" 
				+ "<h1 class=\"text-normal\">" + paidText + "</h1>" + "<hr>"
				+ "<p class=\"text-minor\">$" + item.getPrice() + "</p>" + paidDesc 
				+ "</section>"
				+ "</article>";

		actionItems.add(new ActionItem("DELETE"));

		cards.add(new Card(html, bundleId, actionItems));
		return cards;
	}

	@Override
	public JSONObject toJson() throws JSONException {
		Item item = Item.findItem(itemId);
		JSONObject json = new JSONObject();
		json.put("name", item.getName());
		json.put("price", item.getPrice());
		json.put("imageUrl", item.getImageUrl());
		json.put("location", item.getLocation());
		json.put("paid", paid);

		return json;
	}

}
