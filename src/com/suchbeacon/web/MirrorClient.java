package com.suchbeacon.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MirrorClient {
	public static void insertTimeline(String html, String authToken) {
		try {
			URL url = new URL("https://www.googleapis.com/mirror/v1/timeline");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", "Bearer " + authToken);
			connection.setRequestProperty("Content-Type", "application/json");

			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			writer.write("{ html: '" + html + "' }");
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
			// ...
		}
	}
}
