package com.cartographerapi.domain;

public class DomainQueueFillRequest {
	
	private String domainObject;
	private String queueUrlKey;
	
	public String getQueueUrlKey() {
		return queueUrlKey;
	}
	
	public void setQueueUrlKey(String queueUrlKey) {
		this.queueUrlKey = queueUrlKey;
	}
	
	public String getDomainObject() {
		return domainObject;
	}
	
	public void setDomainObject(String domainObject) {
		this.domainObject = domainObject;
	}
	
	public DomainQueueFillRequest(String domainObject, String queueUrlKey) {
		this.domainObject = domainObject;
		this.queueUrlKey = queueUrlKey;
	}

}
