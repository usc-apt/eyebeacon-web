package com.suchbeacon.web.template;

import java.util.ArrayList;
import java.util.List;

import com.suchbeacon.web.Template;

public class IntroTemplate extends Template {
	private String name;
	private String imageUrl;
	private Exhibit[] exhibits;

	@Override
	public String render() {
		System.out.println("Rendering IntroTemplate");

		String cards = "";
		cards += "<article class=\"photo\">"
				+ "<img src=\"" + imageUrl + "\" width=\"100%\" height=\"100%\" />"
				+ "<div class=\"overlay-gradient-tall-dark\" />"
				+ "<section>" + "<p class=\"text-auto-size\">" + name + "</p>" + "</section>"
				+ "</article>";

		for (Exhibit e : exhibits) {
			cards += "<article class=\"photo\">"
					+ "<img src=\"" + e.imageUrl + "\" width=\"100%\" height=\"100%\" />"
					+ "<div class=\"overlay-gardient-tall-dark\" />"
					+ "<section>" + "<p class=\"text-auto-size\">" + e.name + "</p>" + "</section>"
					+ "</article>";
		}
		return cards;
	}

	static class Exhibit {
		private String name;
		private double[] location;
		private String imageUrl;

		public Exhibit() {
		}
	}

}
