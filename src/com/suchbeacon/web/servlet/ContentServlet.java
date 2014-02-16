package com.suchbeacon.web.servlet;
import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.gson.Gson;
import com.suchbeacon.web.Card;
import com.suchbeacon.web.Content;
import com.suchbeacon.web.GlassResponse;
import com.suchbeacon.web.MirrorClient;
import com.suchbeacon.web.WebResponse;

@SuppressWarnings("serial")
public class ContentServlet extends HttpServlet {	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		int majorId = 0;
		int minorId = 0;
		String accessToken = "";
		try {
			majorId = Integer.parseInt(req.getParameter("majorId"));
			minorId = Integer.parseInt(req.getParameter("minorId"));
			accessToken = req.getParameter("accessToken");
			if (accessToken.length() < 1) {
				throw new NullPointerException();
			}
		} catch (NumberFormatException e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing major or minor id");
			return;
		} catch (NullPointerException e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing access token");
			return;
		}
		
		Content c = ofy().load().type(Content.class)
				.filter("majorId", majorId)
				.filter("minorId", minorId).first().now();

		if (c != null) {
			List<Card> cards = c.buildCards();
			List<GlassResponse> responses = new ArrayList<GlassResponse>();
			String status = "OK";
			for(Card card : cards) { 
				GlassResponse response = MirrorClient.insertTimeline(card, accessToken);
				if(!response.getStatus().equals("200/201 OK")) { status = response.getStatus(); }
				responses.add(response);
			}
			
			try {
				JSONObject webResp = new WebResponse(status, c.toJson(), responses).toJson();
				resp.setContentType("application/json");
				resp.getWriter().println(webResp.toString());
			} catch (JSONException e) { 
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "target content parsing error");
				System.out.println("Error parsing JSON");
			}
			
		} else {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "cannot find target content");
		}
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		int majorId = 0; 
		int minorId = 0;
		String templateName = "";
		String templateDataJson = "";
		try {
			majorId = Integer.parseInt(req.getParameter("majorId"));
			minorId = Integer.parseInt(req.getParameter("minorId"));
			templateName = req.getParameter("template");
			templateDataJson = req.getParameter("templateData");
			if (templateName.length() < 1 || templateDataJson.length() < 1) {
				throw new NullPointerException();
			}
		} catch (NumberFormatException e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing major or minor id");
			return;
		} catch (NullPointerException e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing template data");
			return;
		}
		
		Content c = new Content(majorId, minorId, templateName, templateDataJson);
		ofy().save().entity(c).now();
		if (c != null) {
			Gson gson = new Gson();
			resp.setContentType("application/json");
			resp.getWriter().println(gson.toJson(new WebResponse("ok", "Save successful")));
		}
	}
}
