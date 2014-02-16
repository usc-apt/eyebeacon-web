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
	
	public static GlassResponse insertSubscription(User user, String authToken) {
		try {
			JSONObject jsonBody = new JSONObject();
			jsonBody.put("collection", "timeline");
			jsonBody.put("userToken", user.email);
			jsonBody.put("callbackUrl", "https://mirrornotifications.appspot.com/forward?url=" + "http://suchbeacon.com/glass/notify");
			
			JSONArray operationArray = new JSONArray();
			operationArray.put("CUSTOM");
			jsonBody.put("operation", operationArray);
			
			return request("https://www.googleapis.com/mirror/v1/subscriptions", authToken, jsonBody.toString());
		} catch (JSONException e) {
			return new GlassResponse("400 Bad Request", "JSON Exception");
		} 
	}
	
	
	public static GlassResponse insertTimeline(Card card, String authToken) {
		try {
			JSONObject jsonBody = new JSONObject();
			jsonBody.put("html", card.getHtml());
			jsonBody.put("isBundleCover", card.isBundleCover());
			jsonBody.put("bundleId", card.getBundleId());
			jsonBody.put("menuItems", generateActionItems(card.getActionItems()));
			jsonBody.put("notification", generateNotification());
			jsonBody.put("speakableText", card.getSpeakableText());
			
			return request("https://www.googleapis.com/mirror/v1/timeline", authToken, jsonBody.toString());
		} catch (JSONException e) {
			return new GlassResponse("400 Bad Request", "JSON Exception");
		} 
	}
	
	public static GlassResponse request(String requestUrl, String authToken, String body) {
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", "Bearer " + authToken);
			connection.setRequestProperty("Content-Type", "application/json");

			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			writer.write(body);
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
			return new GlassResponse("400 Bad Request", "IO Exception");
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
				menuItem.put("payload", a.payload);
				System.out.println("RemovingWhenSelected");
				menuItem.put("removeWhenSelected", a.removeWhenSelected);
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
