package com.suchbeacon.web.template;

import com.suchbeacon.web.Template;

public class IntroTemplate extends Template {
	private String name;
	private String imageUrl;
	private Exhibit[] exhibits;

	@Override
	public String render() {
		System.out.println("Rendering IntroTemplate");
		String s = "";
		s += "Name: " + name + "\n";
		s += "Image: " + imageUrl + "\n";
		if (exhibits != null) {
			for (Exhibit e : exhibits) {
				s += "Exhibit: " + e.name + "\n";
			}
		}
		return s;
	}

	static class Exhibit {
		private String name;
		private double[] location;
		private String imageUrl;

		public Exhibit() {
		}
	}

}
