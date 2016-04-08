package com.cartographerapi.functions;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import java.util.List;

@DynamoDBTable(tableName="Cache-PlayerGameCounts")
public class PlayerGameCountCacheEntry {
	private String day;
	private Long time;
	private List<String> gamertag;

	@DynamoDBHashKey(attributeName="Day")
	public String getDay() {
		return day;
	}
	
	public void setDay(String day) {
		this.day = day;
	}

	@DynamoDBRangeKey(attributeName="Time")
	public Long getTime() {
		return time;
	}
	
	public void setTime(Long time) {
		this.time = time;
	}

	@DynamoDBAttribute(attributeName="Gamertag")
	public List<String> getGamertag() {
		return gamertag;
	}
	
	public void setGamertag(List<String> gamertag) {
		this.gamertag = gamertag;
	}

	public PlayerGameCountCacheEntry(String day, Long time, List<String> gamertag) {
		this.day = day;
		this.time = time;
		this.gamertag = gamertag;
	}
	
	public PlayerGameCountCacheEntry() {
	}

}
