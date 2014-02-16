package com.suchbeacon.web.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.suchbeacon.web.Constants;

@SuppressWarnings("serial")
public class VenmoAuthServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(VenmoAuthServlet.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		String auth_token = req.getParameter("code");
		JSONObject authJson = new JSONObject();
		URL authUrl = new URL(Constants.VENMO_OAUTH_ACCESSTOKEN);

		HttpURLConnection connection = (HttpURLConnection) authUrl.openConnection();
		connection.setDoOutput(true);	
		connection.setRequestMethod("POST");
		try {
			authJson.put("client_id", Constants.VENMO_CLIENT_ID);
			authJson.put("client_secret", Constants.VENMO_CLIENT_SECRET);
			authJson.put("code", auth_token);

			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			writer.write("client_id=" + Constants.VENMO_CLIENT_ID 
					+ "&client_secret=" + Constants.VENMO_CLIENT_SECRET 
					+ "&code=" + auth_token);
			
			writer.close();
		} catch (JSONException e) {
		}

		if (connection.getResponseCode() == HttpURLConnection.HTTP_CREATED
				|| connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			System.out.println("Success!");

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			JSONObject jsonResponse;

			String jsonRawResponse = "";
			String line;

			while ((line = reader.readLine()) != null)
				jsonRawResponse.concat(line);

			reader.close();

			try {
				jsonResponse = new JSONObject(jsonRawResponse);
				log.log(Level.INFO, "VENMORESPONSE: " + jsonResponse.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}

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
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

	}
}
