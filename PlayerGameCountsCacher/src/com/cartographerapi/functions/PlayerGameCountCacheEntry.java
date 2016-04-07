package com.cartographerapi.functions;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="Cache-PlayerGameCounts")
public class PlayerGameCountCacheEntry {
	private String day;
	private Integer time;
	private String gamertag;

	@DynamoDBHashKey(attributeName="Day")
	public String getDay() {
		return day;
	}
	
	public void setDay(String day) {
		this.day = day;
	}

	@DynamoDBRangeKey(attributeName="Time")
	public Integer getTime() {
		return time;
	}
	
	public void setTime(Integer time) {
		this.time = time;
	}

	@DynamoDBAttribute(attributeName="Gamertag")
	public String getGamertag() {
		return gamertag;
	}
	
	public void setGamertag(String gamertag) {
		this.gamertag = gamertag;
	}

	public PlayerGameCountCacheEntry(String day, Integer time, String gamertag) {
		this.day = day;
		this.time = time;
		this.gamertag = gamertag;
	}
	
	public PlayerGameCountCacheEntry() {
	}

}
