package com.suchbeacon.web.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
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
import com.suchbeacon.web.Card;
import com.suchbeacon.web.Constants;
import com.suchbeacon.web.Item;
import com.suchbeacon.web.MirrorClient;
import com.suchbeacon.web.Template;
import com.suchbeacon.web.User;

@SuppressWarnings("serial")
public class NotifyServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Logger log = Logger.getLogger(NotifyServlet.class.getName());

		JsonFactory jsonFactory = new JacksonFactory();
		Notification not = jsonFactory.fromInputStream(req.getInputStream(), Notification.class);
		
		if(!not.getCollection().equals("timeline")){
			return;
		}
		
		String email = not.getUserToken();
		
		System.out.println(not.toPrettyString());
		for (UserAction ua : not.getUserActions()) {
			// If we have a custom action, we know it is a payment
			if (ua.getType().equals("CUSTOM") ) {
				
				// TODO: Pull from the database and get the information about the item
				/* Temporary Info for item */
				String buyingItemId = ua.getPayload();
				System.out.println("PAYLOAD: " + buyingItemId);
				Item item = Item.findItem(Long.parseLong(buyingItemId));
				String accessToken = User.findUser(email).getVenmoAuthToken();
				String targetEmail = item.getTargetEmail();
				
				URL payURL = new URL(Constants.VENMO_PAY_URL);
				HttpURLConnection connection = (HttpURLConnection) payURL.openConnection();
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");

				OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
				writer.write("access_token=" + accessToken
						+ "&email=" + targetEmail
						+ "&note=" + item.getName()
						+ "&amount=" + item.getPrice());
				writer.close();
				
				try {
					if (connection.getResponseCode() == HttpURLConnection.HTTP_CREATED
							|| connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
						log.info("Successful POST to " + Constants.VENMO_PAY_URL);
						String line, jsonResponseRaw = "";
						BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						while ((line = reader.readLine()) != null)
							jsonResponseRaw = jsonResponseRaw.concat(line);
						reader.close();

						JSONObject jsonResponse = new JSONObject(jsonResponseRaw);
						System.out.println(jsonResponse.toString());

						JSONObject payConfirmData = new JSONObject();
						payConfirmData.put("itemId", buyingItemId);
						
						// Check to see if the payment was settled or failed
						String paymentStatus = jsonResponse.getJSONObject("data").getJSONObject("payment").getString("status");
						if (paymentStatus.equals("settled")) {
							log.info("The Payment was settled!");
							payConfirmData.put("paid", true);
						} else {
							log.info("The Payment failed! Sucks to Suck");
							payConfirmData.put("paid", false);
						}

						Card mCard = Template.build("payConfirm", payConfirmData.toString()).render().get(0);
						MirrorClient.updateTimelineItem(mCard, User.getAuthTokenFromEmail(not.getUserToken()), not.getItemId());
					} else {
						System.out.println("HTTPError: " + connection.getResponseCode() );
						String line, jsonResponseRaw = "";
						BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						while ((line = reader.readLine()) != null)
							jsonResponseRaw = jsonResponseRaw.concat(line);
						
						reader.close();
						System.out.println("Response: " + jsonResponseRaw);
						
						JSONObject payConfirmData = new JSONObject();
						payConfirmData.put("itemId", buyingItemId);
						payConfirmData.put("paid", false);
						Card mCard = Template.build("payConfirm", payConfirmData.toString()).render().get(0);
						MirrorClient.insertTimeline(mCard, User.getAuthTokenFromEmail(not.getUserToken()));
					}
				} catch (JSONException e) {
					resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad Payload/DB Population");
				}
			}
		}
	}
}
