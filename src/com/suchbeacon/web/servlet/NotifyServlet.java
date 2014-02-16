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

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.mirror.model.Notification;
import com.google.api.services.mirror.model.UserAction;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.suchbeacon.web.Constants;

@SuppressWarnings("serial")
public class NotifyServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Logger log = Logger.getLogger(VenmoAuthServlet.class.getName());

		JsonFactory jsonFactory = new JacksonFactory();
		Notification not = jsonFactory.fromInputStream(req.getInputStream(), Notification.class);
		for (UserAction ua : not.getUserActions()) {

			// If we have a custom action, we know it is a payment
			if (ua.getType().equals("CUSTOM")) {
				URL payURL								= new URL(Constants.VENMO_PAY_URL);
				HttpURLConnection connection = (HttpURLConnection)payURL.openConnection();
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				
				String buyingItemId 			= ua.getPayload();
				JSONObject paymentRequest = new JSONObject();
				
				try {
					// TODO: Pull from the database and get the information about the item
					/* Temporary Info for item */
					String buyingItemDesc = "Dolla Dolla Bill Y'all";
					String buyingItemPrice = "3309";
					//Our own access_token
					paymentRequest.put("access_token", "78907908");
					
					//person it is going to
					paymentRequest.put("email", "venmo@venmo.com"); 
					paymentRequest.put("note", buyingItemDesc);
					paymentRequest.put("price", buyingItemPrice);
					
					OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
					writer.write(paymentRequest.toString());
				
					if(connection.getResponseCode() == HttpURLConnection.HTTP_CREATED
							|| connection.getResponseCode() == HttpURLConnection.HTTP_OK){
						log.log(Level.INFO, "Successful POST to " + Constants.VENMO_PAY_URL);
						String line, jsonResponseRaw = "";
						BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						while((line=reader.readLine())!=null)
							jsonResponseRaw.concat(line);
						reader.close();
						JSONObject jsonResponse = new JSONObject(jsonResponseRaw);
						
						//Check to see if the payment was settled or failed
						if(jsonResponse.get("status").equals("settled")){
							log.log(Level.INFO, "The Payment was settled!");
							//TODO: Insert Timeline Item to tell the user where to go go pickup their item
						}
						else{
							log.log(Level.WARNING, "The Payment failed! Sucks to Suck");
							//TODO: Insert Timeline Item to tell the user his payment failed.
						}
					}	 	
				} catch (JSONException e) {
					resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad Payload/DB Population");
				}
			}
		}
	}
}
