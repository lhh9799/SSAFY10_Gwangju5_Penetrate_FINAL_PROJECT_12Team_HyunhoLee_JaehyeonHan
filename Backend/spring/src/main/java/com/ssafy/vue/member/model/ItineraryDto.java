package com.ssafy.vue.member.model;

public class ItineraryDto {
	private String userId;
	private int day;
	private String place;
	private int contentId;
	private String addr;
	private double latitude;
	private double longtitude;
	private int sequence;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public int getContentId() {
		return contentId;
	}

	public void setContentId(int contentId) {
		this.contentId = contentId;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	@Override
	public String toString() {
		return "ItineraryDto [userId=" + userId + ", day=" + day + ", place=" + place + ", contentId=" + contentId
				+ ", addr=" + addr + ", latitude=" + latitude + ", longtitude=" + longtitude + ", sequence=" + sequence
				+ "]";
	}

}
