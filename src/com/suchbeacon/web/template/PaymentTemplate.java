package com.suchbeacon.web.template;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.suchbeacon.web.Card;
import com.suchbeacon.web.Card.ActionItem;
import com.suchbeacon.web.Template;

public class PaymentTemplate extends Template {
	private Long itemId;
	private String name;
	private double price;
	private String imageUrl;
	private String location;
	private String description;

	@Override
	public List<Card> render() {
		System.out.println("Rendering PaymentTemplate");
		String bundleId = generateBundleId();
		List<Card> cards = new ArrayList<Card>();
		List<ActionItem> actionItems = new ArrayList<ActionItem>();
		
		String html = "<article>" 
				+ "<figure>" + "<img src=\"" + imageUrl + "\" width=\"100%\" height=\"100%\">" + "</figure>"
				+ "<section>"
				+ "<h1 class=\"text-large\">" + name + "</h1>"
				+ "<hr>"
				+ "<p class=\"text-minor\">$" + price + "</p>"
				+ "<p class=\"text-small\">" + location + "</p>"
				+ "</section>" 
				+ "</article>";
		html += "<article class=\"auto-paginate>"
				+ "<h1 class=\"text-large\">" + name + "</h1>"
				+ "<hr>"
				+ "<p class=\"text-small\">" + description + "</p>"
				+ "</article>";
		
		actionItems.add(new ActionItem("purchase", "DEFAULT", "Purchase", String.valueOf(itemId)));
		actionItems.add(new ActionItem("DELETE"));
		
		cards.add(new Card(html, bundleId, actionItems));
		return cards;
	}

	@Override
	public JSONObject toJson() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("name", name);
		json.put("price", price);
		json.put("imageUrl", imageUrl);
		json.put("location", location);
		json.put("description", description);
		json.put("itemId", itemId);
		return json;
	}
	
	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getLocation() {
		return location;
	}

	public String getDescription() {
		return description;
	}

}
