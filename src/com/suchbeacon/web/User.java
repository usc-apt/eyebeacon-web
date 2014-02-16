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
	
	private User() { } 
	
	public User(String email) {
		this.email = email;
		this.registerTime = new Date();
	}
	
	public static User findUser(String email) {
		User u = ofy().load().type(User.class).filter("email", email).first().now();
		if(u != null) {
			return u;
		} else {
			return null;
		}
		
	}
}
