package com.cartographerapi.domain;

/**
 * A custom domain object for a Cloudwatch ScheduledEvent, since I couldn't find
 * one from the AWS SDK for Java.
 * <pre>
 *     Account
 *     Region
 *     Source
 *     Time
 *     Id
 * </pre>
 * 
 * @author GodlyPerfection
 *
 */
public class ScheduledEvent {
	
	private String account;
	private String region;
	private String source;
	private String time;
	private String id;
	
	public String getAccount() {
		return account;
	}
	
	public void setAccount(String account) {
		this.account = account;
	}

	public String getRegion() {
		return region;
	}
	
	public void setRegion(String region) {
		this.region = region;
	}

	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}

	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public ScheduledEvent(String time) {
		this.time = time;
	}
	
	public ScheduledEvent() {
	}
}
