package com.suchbeacon.web.servlet;

import static com.googlecode.objectify.ObjectifyService.ofy;

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
import com.suchbeacon.web.User;

@SuppressWarnings("serial")
public class VenmoAuthServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(VenmoAuthServlet.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String code = req.getParameter("code");
		String email = req.getParameter("state");
		
		URL authUrl = new URL(Constants.VENMO_OAUTH_ACCESSTOKEN);
		HttpURLConnection connection = (HttpURLConnection) authUrl.openConnection();
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");

		OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
		writer.write("client_id=" + Constants.VENMO_CLIENT_ID 
				+ "&client_secret=" + Constants.VENMO_CLIENT_SECRET
				+ "&code=" + code);
		writer.close();
		
		if (connection.getResponseCode() == HttpURLConnection.HTTP_CREATED
				|| connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			System.out.println("Success!");

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			JSONObject jsonResponse;

			String jsonRawResponse = "";
			String line;
			while ((line = reader.readLine()) != null)
				jsonRawResponse = jsonRawResponse.concat(line);

			reader.close();
			try {
				jsonResponse = new JSONObject(jsonRawResponse);
				System.out.println("VenmoResponse: ");
				System.out.println(jsonResponse.toString());
				String venmoAccessToken = jsonResponse.getString("access_token");
				String venmoRefreshToken = jsonResponse.getString("refresh_token");
				
				User u = User.findUser(email);
				if(u != null) {
					System.out.println("found email. Linking venmo");
					u.setVenmoAuthToken(venmoAccessToken);
					u.setVenmoRefreshToken(venmoRefreshToken);
					ofy().save().entity(u).now();
				} else {
					System.out.println("Creating new user with email " + email);
					u = new User(email, venmoAccessToken, venmoRefreshToken);
					ofy().save().entity(u).now();
				}
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
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

	}
}
