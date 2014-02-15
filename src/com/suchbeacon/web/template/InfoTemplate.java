package com.suchbeacon.web.template;

import java.util.ArrayList;
import java.util.List;

import com.suchbeacon.web.Card;
import com.suchbeacon.web.Card.ActionItem;
import com.suchbeacon.web.Template;

public class InfoTemplate extends Template {
	private String name;
	private String imageUrl;
	private String location;
	private String videoUrl;
	private String speakableText;
	private Info[] infos;

	@Override
	public List<Card> render() {
		List<ActionItem> actionItems = new ArrayList<ActionItem>();
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

		//Adds speakable text only if we have info to tell the user
		for (Info i : infos) {
			html += "<article class=\"auto-paignate\">"
					+ "<h1 class=\"text-large\">" + i.section + "</h1>"
					+ "<p class=\"text-small\">" + i.desc + "</p>" 
					+ "</article>";
			speakableText += i.section + " " + i.desc;
		}
		actionItems.add(new ActionItem("DELETE"));
		
		if(!speakableText.isEmpty()){
			actionItems.add(new ActionItem("READ_ALOUD"));
		}
		
		if(videoUrl != null)
			actionItems.add(new ActionItem("PLAY_VIDEO", videoUrl));
		
		//We can setSpeakableText anyway because there will not be a read_aloud action
		cards.add((new Card(html, bundleId, (ActionItem[]) actionItems.toArray()).setSpeakableText(speakableText)));

		return cards;
	}

	static class Info {
		private String section;
		private String desc;

		public Info() {
		}
	}
}
