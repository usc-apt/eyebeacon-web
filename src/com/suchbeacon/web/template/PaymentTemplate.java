package com.suchbeacon.web.template;

import java.util.ArrayList;
import java.util.List;

import com.suchbeacon.web.Card;
import com.suchbeacon.web.Template;

public class PaymentTemplate extends Template {
	private String name;
	private String price;
	private String imageUrl;
	private String location;
	private String description;

	@Override
	public List<Card> render() {
		System.out.println("Rendering PaymentTemplate");
		String bundleId = generateBundleId();

		List<Card> cards = new ArrayList<Card>();
		String html = "<article>" 
				+ "<figure>" + "<img src=\"" + imageUrl + "\" width=\"100%\" height=\"100%\">" + "</figure>"
				+ "<section>"
				+ "<table class=\"text-small slign-justify\">" + "<tbody>" 
				+ "<tr>" + "<td>Item:</td>" + "<td>"+ name + "</td>" + "</tr>"
				+ "<tr>" + "<td>Price</td>" + "<td>" + price + "</td>" + "</tr>"
				+ "<tr>" + "<td>Location</td>" + "<td>" + location + "</td>" + "</tr>"
				+ "</tbody>" + "</table>" 
				+ "</section>" 
				+ "</article>";
		html += "<article>" 
				+ "<h1 class=\"auto-paginate\">" + "<hr>"
				+ "<p class=\"text-small\">" + description + "</p>"
				+ "</article>";
		cards.add(new Card(html, bundleId, "DELETE"));
		return cards;
	}
}
