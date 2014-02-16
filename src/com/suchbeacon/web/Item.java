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
	String  imageUrl;
	double	price;

	private Item() {
	}

	public Item(String name, String description, String location, String imageUrl, double price) {
		this.name = name;
		this.price = price;
		this.location = location;
		this.imageUrl = imageUrl;
		this.description = description;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public static Item findItem(Long id) {
		return ofy().load().type(Item.class).id(id).now();
	}
}
