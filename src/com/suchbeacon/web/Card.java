package com.suchbeacon.web;

import java.util.ArrayList;
import java.util.List;

public class Card {
	public static class ActionItem {
		public String id;
		public String state;
		public String action;
		public String payload;
		public String displayName;
		public String iconUrl;
		
		// meh, it's not like we're going to use this attribute much anyway
		public boolean removeWhenSelected = true;

		public ActionItem(String action) {
			this.action = action;
			this.payload = null;
		}

		public ActionItem(String action, String payload) {
			this.action = action;
			this.payload = payload;
		}

		public ActionItem(String id, String state, String displayName, String payload) {
			this.id = id;
			this.payload = payload;
			this.state = state;
			this.action = "CUSTOM";
			this.displayName = displayName;
		}

		public ActionItem setIconUrl(String iconUrl) {
			this.iconUrl = iconUrl;
			return this;
		}
	}

	private String html;
	private String bundleId;
	private String speakableText;
	private List<ActionItem> actionItems;
	private boolean isBundleCover = false;

	public String getHtml() {
		return html;
	}

	public String getBundleId() {
		return bundleId;
	}

	public String getSpeakableText() {
		return speakableText;
	}
	
	public boolean isBundleCover() {
		return isBundleCover;
	}
	
	public List<ActionItem> getActionItems() {
		return actionItems;
	}

	public Card setSpeakableText(String speakableText) {
		this.speakableText = speakableText;
		return this;
	}

	public Card setIsBundleCover(boolean isBundleCover) {
		this.isBundleCover = isBundleCover;
		return this;
	}
	
	public Card(String html, String bundleId, List<ActionItem> actionItems) {
		this.html = html;
		this.bundleId = bundleId;
		this.speakableText = "";
		this.actionItems = actionItems;
	}

	public Card(String html, String bundleId, ActionItem... actionItems) {
		this.html = html;
		this.bundleId = bundleId;
		this.speakableText = "";

		this.actionItems = new ArrayList<ActionItem>();
		for (ActionItem a : actionItems) {
			this.actionItems.add(a);
		}
	}

	public Card(String html, String bundleId, String... actions) {
		this.html = html;
		this.bundleId = bundleId;
		this.speakableText = "";

		this.actionItems = new ArrayList<ActionItem>();
		for (String s : actions) {
			this.actionItems.add(new ActionItem(s));
		}

	}

}
