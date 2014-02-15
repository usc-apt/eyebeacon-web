package com.suchbeacon.web;

import java.util.ArrayList;
import java.util.List;

public class Card {
	public static class ActionItem {
		public String action;
		public String payload;
		public ActionItem(String action, String payload) {
			this.action = action;
			this.payload= payload;
		}
	}
	
	private String html;
	private String bundleId;
	private List<ActionItem> actionItems;
	
	public String getHtml() {
		return html;
	}
	public String getBundleId() {
		return bundleId;
	}
	public List<ActionItem> getActionItems() {
		return actionItems;
	}
	
	public Card(String html, String bundleId, ActionItem ... actionItems) {
		this.html = html;
		this.bundleId = bundleId;
		
		this.actionItems = new ArrayList<ActionItem>();
		for(ActionItem a : actionItems) {
			this.actionItems.add(a);
		}
	}
	
	public Card(String html, String bundleId, String ... actions) {
		this.html = html;
		this.bundleId = bundleId;
		
		this.actionItems = new ArrayList<ActionItem>();
		for(String s : actions) {
			this.actionItems.add(new ActionItem(s));
		}
		
	}
	

}
