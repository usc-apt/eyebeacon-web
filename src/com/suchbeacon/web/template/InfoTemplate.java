package com.suchbeacon.web.template;

import java.util.ArrayList;
import java.util.List;

import com.suchbeacon.web.Card;
import com.suchbeacon.web.Template;

public class InfoTemplate extends Template {
	private String name;
	private String imageUrl;
	private String location;
	private Info[] infos;

	@Override
	public List<Card> render() {
		System.out.println("Rendering InfoTemplate");
		List<Card> cards = new ArrayList<Card>();
		String bundleId = generateBundleId();

		String html = "<article class=\"cover-only\">"
				+ "<figure>"
				+ "<img src=\"" + imageUrl + "\" width=\"100%\" height=\"100%\">"
				+ "</figure>" 
				+ "<section>" 
				+ "<h1 class=\"text-large\">" + name + "</h1>" + "<hr>"
				+ "<p class=\"text-small\">" + "Located In:" + "</p>" 
				+ "<p class=\"text-minor\">" + location + "</p>" 
				+ "</section>"
				+ "</article>";
		cards.add(new Card(html, bundleId, "DELETE"));

		for (Info i : infos) {
			html = "<article class=\"auto-paignate\">"
					+ "<h1 class=\"text-large\">" + i.section + "</h1>"
					+ "<p class=\"text-small\">" + i.desc + "</p>" 
					+ "</article>";
			cards.add(new Card(html, bundleId, "DELETE"));
		}

		return cards;
	}

	static class Info {
		private String section;
		private String desc;

		public Info() {
		}
	}
}
