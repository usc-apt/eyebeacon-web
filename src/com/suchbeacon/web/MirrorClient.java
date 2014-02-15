package com.suchbeacon.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.suchbeacon.web.Card.ActionItem;

public class MirrorClient {
	public static void insertTimeline(Card card, String authToken) {
		try {
			URL url = new URL("https://www.googleapis.com/mirror/v1/timeline");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", "Bearer " + authToken);
			connection.setRequestProperty("Content-Type", "application/json");

			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			String s = "{ html: '" + card.getHtml() + "', "
					+ generateBundleCover(card.isBundleCover()) + ", "
					+ generateBundleId(card.getBundleId()) + ", "
					+ generateActionItems(card.getActionItems()) + ", " 
					+ generateSpeakableText(card.getSpeakableText()) + " }";
			System.out.println(s);
			writer.write(s);
			writer.close();

			if (connection.getResponseCode() == HttpURLConnection.HTTP_CREATED
					|| connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				System.out.println("Win!");
			} else {
				System.out.println("Error " + connection.getResponseCode());
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;

				while ((line = reader.readLine()) != null) {
					System.out.println(line);
				}
				reader.close();
				// Server returned HTTP error code.
			}
		} catch (MalformedURLException e) {
			System.out.println("Malformed URL");
			// ...
		} catch (IOException e) {
			System.out.println("IO");
			System.out.println(e.getMessage());
			// ...
		}
	}
	
	private static String generateBundleCover(boolean isBundleCover) {
		return "isBundleCover: " + isBundleCover;
	}

	private static String generateBundleId(String bundleId) {
		return "bundleId: '" + bundleId + "'";
	}

	private static String generateActionItems(List<ActionItem> actionItems) {
		String ai = "menuItems: [ ";
		for (ActionItem a : actionItems) {
			ai += "{";
			if (!a.action.equals("CUSTOM")) {
				ai += "action: '" + a.action + "'";
				if (a.payload != null) {
					ai += ",";
					ai += "payload: '" + a.payload + "'";
				}
			} else {
				// Custom so we need to set that up.
				ai += "id: '" + a.id + "', ";
				ai += "action: '" + a.action + "', ";
				ai += "values: [";
				ai += "{";
				ai += "state: '" + a.state + "', ";
				ai += "displayName: '" + a.displayName + "'";
				if (a.iconUrl != null) ai += ", iconUrl: '" + a.iconUrl + "'";
			}
			ai += "}";
			ai += ", ";
		}
		ai = ai.substring(0, ai.length() - 2);
		ai += " ] ";
		return ai;
	}

	private static String generateSpeakableText(String speakableText) {
		String st = "speakableText: '";
		st += speakableText;
		st += "'";
		return st;
	}
}
