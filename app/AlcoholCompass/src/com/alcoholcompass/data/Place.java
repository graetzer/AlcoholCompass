package com.alcoholcompass.data;

import java.util.List;

public class Place {
	private String name, address;
	private int id, open, closed;
	private double latitude, longitude, rating;
	private List<GuestbookEntry> guestbookEntries;
	
	public Place(
			int id,
			String name,
			String address,
			int open,
			int closed,
			double latitude,
			double longitude
			){
		this.setID(id);
		this.setName(name);
		this.setAddress(address);
		this.setOpen(open);
		this.setClosed(closed);
		this.setLatitude(latitude);
		this.setLongitude(longitude);
		this.setRating(0);
		
	}
	
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getClosed() {
		return closed;
	}

	public void setClosed(int closed) {
		this.closed = closed;
	}

	public int getOpen() {
		return open;
	}

	public void setOpen(int open) {
		this.open = open;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public void setGuestbookEntries(List<GuestbookEntry> entries) {
		this.guestbookEntries = entries;
	}
	
	public List<GuestbookEntry> getGuestbookEntries() {
		return this.guestbookEntries;
	}
	
}
