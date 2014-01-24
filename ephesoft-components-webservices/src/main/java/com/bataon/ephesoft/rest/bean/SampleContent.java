package com.bataon.ephesoft.rest.bean;

import java.util.Date;

public class SampleContent {
	 
	String title;
	String url;
	String summary;
	Date createdDate;
	
	public String getTitle() {
		return title;
	}
	public String getUrl() {
		return url;
	}
	public String getSummary() {
		return summary;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
 
	
}