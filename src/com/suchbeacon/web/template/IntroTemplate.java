package com.suchbeacon.web.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.suchbeacon.web.Card;
import com.suchbeacon.web.Card.ActionItem;
import com.suchbeacon.web.Template;

public class IntroTemplate extends Template {
	private String name;
	private String imageUrl;
	private Exhibit[] exhibits;

	@Override
	public List<Card> render() {
		System.out.println("Rendering IntroTemplate");
		List<Card> cards = new ArrayList<Card>();
		String bundleId = generateBundleId();
		
		cards.add(new Card("<article class=\"photo\">"
				+ "<img src=\"" + imageUrl + "\" width=\"100%\" height=\"100%\" />"
				+ "<div class=\"overlay-gradient-tall-dark\" />"
				+ "<section>" + "<p class=\"text-auto-size\">" + name + "</p>" + "</section>"
				+ "</article>", bundleId, "DELETE"));

		for (Exhibit e : exhibits) {
			List<ActionItem> actionItems = new ArrayList<ActionItem>();
			actionItems.add(new ActionItem("DELETE"));
			
			if(e.videoUrl != null)
				actionItems.add(new ActionItem("PLAY_VIDEO", e.videoUrl));
				
			cards.add(new Card("<article class=\"photo\">"
					+ "<img src=\"" + e.imageUrl + "\" width=\"100%\" height=\"100%\" />"
					+ "<div class=\"overlay-gardient-tall-dark\" />"
					+ "<section>" + "<p class=\"text-auto-size\">" + e.name + "</p>" + "</section>"
					+ "</article>", bundleId, (String[]) actionItems.toArray() ));
		}
		return cards;
	}

	static class Exhibit {
		private String name;
		private double[] location;
		private String imageUrl;
		private String videoUrl;
		public Exhibit() {
		}
	}

}
