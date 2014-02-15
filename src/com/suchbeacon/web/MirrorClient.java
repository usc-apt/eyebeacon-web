package com.suchbeacon.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.suchbeacon.web.Card.ActionItem;

public class MirrorClient {
	public static GlassResponse insertTimeline(Card card, String authToken) {
		try {
			URL url = new URL("https://www.googleapis.com/mirror/v1/timeline");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			JSONObject jsonBody = new JSONObject();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", "Bearer " + authToken);
			connection.setRequestProperty("Content-Type", "application/json");

			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			jsonBody.put("html", card.getHtml());
			jsonBody.put("isBundleCover", card.isBundleCover());
			jsonBody.put("bundleId", card.getBundleId());
			jsonBody.put("menuItems", generateActionItems(card.getActionItems()));
			jsonBody.put("notification", generateNotification());
			jsonBody.put("speakableText", card.getSpeakableText());
			System.out.println(jsonBody.toString());
			writer.write(jsonBody.toString());
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
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new GlassResponse("400 Bad Request", "JSON Exception");
		}
	}

	private static JSONArray generateActionItems(List<ActionItem> actionItems) throws JSONException{
		
		JSONArray menuItems = new JSONArray();
		
		for (ActionItem a : actionItems) {
			JSONObject menuItem = new JSONObject();
			menuItem.put("action", a.action);
			if (!a.action.equals("CUSTOM")) {
				if (a.payload != null) {
					menuItem.put("payload",  a.payload);
				}
			} else {
				// Custom so we need to set that up.
				menuItem.put("id", a.id);
				
				JSONArray valuesArray = new JSONArray();
				
				JSONObject value = new JSONObject();
				value.put("state", a.state);
				value.put("displayName", a.displayName);
				if (a.iconUrl != null) value.put("iconUrl", a.iconUrl);
				
				valuesArray.put(value);
				menuItem.put("values", valuesArray);
			}
			menuItems.put(menuItem);
		}
		return menuItems;
	}
	
	private static JSONObject generateNotification() throws JSONException{
		JSONObject notification = new JSONObject();
		notification.put("level",  "DEFAULT");
		return notification;
	}
}
