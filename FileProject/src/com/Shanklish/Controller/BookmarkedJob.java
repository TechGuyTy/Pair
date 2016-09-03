package com.Shanklish.Controller;

public class BookmarkedJob 
{
	private String url;
	private int uID;
	private int id;
	private String JobTitle;
	
	
	public String getJobTitle() {
		return JobTitle;
	}
	public void setJobTitle(String jobTitle) {
		JobTitle = jobTitle;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getuID() {
		return uID;
	}
	public void setuID(int uID) {
		this.uID = uID;
	}
}
