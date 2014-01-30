package com.bataon.ephesoft.rest.bean;

import java.util.Date;

import com.ephesoft.dcma.da.domain.BatchInstance;

public class RssBatchStatus {
	 
	String title;
	String summary;
	Date createdDate;
	
	public RssBatchStatus(BatchInstance batchInstance) {
		title = batchInstance.getIdentifier();
		summary = batchInstance.getStatus().toString();
		createdDate = batchInstance.getLastModified();
	}
	public String getTitle() {
		return title;
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
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
 
	
}