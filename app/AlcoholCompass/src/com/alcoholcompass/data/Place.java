package com.alcoholcompass.data;

public class Place {
	private String name, address;
	private int open, closed;
	private float latitude, longitude, rating;
	
	public Place(String name,
			String address,
			int open,
			int closed,
			float latitude,
			float longitude
			){
		
		this.setName(name);
		this.setAddress(address);
		this.setOpen(open);
		this.setClosed(closed);
		this.setLongitude(longitude);
		this.setLatitude(latitude);
		this.setRating(0);
		
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

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	
	
}
