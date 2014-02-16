package com.suchbeacon.web;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Item {
	@Id
	Long		id;
	String	name;
	String	description;
	String	location;
	double	price;

	private Item() {
	}

	public Item(String name, String description, String location, double price) {
		this.name = name;
		this.price = price;
		this.location = location;
		this.description = description;
	}
	
	public static Item findItem(Long id) {
		Item i = ofy().load().type(Item.class).filter("id", id).first().now();
		if(i != null) {
			return i;
		} else {
			return null;
		}
	}
}
