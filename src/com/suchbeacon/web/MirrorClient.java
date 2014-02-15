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
	public static GlassResponse insertTimeline(Card card, String authToken) {
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
					+ generateNotification() + ", "
					+ generateSpeakableText(card.getSpeakableText()) + " }";
			System.out.println(s);
			writer.write(s);
			writer.close();

			if (connection.getResponseCode() == HttpURLConnection.HTTP_CREATED
					|| connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				return new GlassResponse("200/201 OK", "");
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String totalLines = "", line;
				while ((line = reader.readLine()) != null) {
					totalLines += line;
				}
				reader.close();
				return new GlassResponse(String.valueOf(connection.getResponseCode()), totalLines);
			}
		} catch (MalformedURLException e) {
			return new GlassResponse("400 Bad Request", "Malformed URL");
		} catch (IOException e) {
			System.out.println("IO");
			System.out.println(e.getMessage());
			return new GlassResponse("400 Bad Request", "IO Exception");
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
	
	private static String generateNotification() {
		return "notification: { level: 'DEFAULT' }";
	}

	private static String generateSpeakableText(String speakableText) {
		String st = "speakableText: '";
		st += speakableText;
		st += "'";
		return st;
	}
}
