package com.suchbeacon.web;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class User {

	@Id Long id;
	@Index String email;
	Date registerTime;
	String authToken;
	
	String venmoAuthToken;
	String venmoRefreshToken;

	private User() { } 
	
	public User(String email, String authToken) {
		this.email = email;
		this.authToken = authToken;
		this.registerTime = new Date();
	}
	
	public User(String email, String venmoAuthToken, String venmoRefreshToken) {
		this.email = email;
		this.venmoAuthToken = venmoAuthToken;
		this.venmoRefreshToken = venmoRefreshToken;
		this.registerTime = new Date();
	}
	
	public String getVenmoAuthToken() {
		return venmoAuthToken;
	}

	public String getVenmoRefreshToken() {
		return venmoRefreshToken;
	}

	public void setVenmoAuthToken(String venmoAuthToken) {
		this.venmoAuthToken = venmoAuthToken;
	}

	public void setVenmoRefreshToken(String venmoRefreshToken) {
		this.venmoRefreshToken = venmoRefreshToken;
	}
	
	public static void updateAuthToken(String email, String newAuthToken) {
		User u = findUser(email);
		u.authToken = newAuthToken;
		ofy().save().entity(u).now();
	}
	
	public static User findUser(String email) {
		User u = ofy().load().type(User.class).filter("email", email).first().now();
		if(u != null) {
			return u;
		} else {
			return null;
		}
	}
	
	public static String getAuthTokenFromEmail(String email) {
		return findUser(email).authToken;
	}
}
