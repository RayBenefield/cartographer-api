package com.cartographerapi.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import java.util.Date;

@DynamoDBTable(tableName="PlayerGamesCheckpoints")
public class PlayerGamesCheckpoint {

	private String gamertag;
	private Integer totalGamesLoaded;
	private String lastMatch;
	private Date lastUpdated;
	
	@DynamoDBHashKey(attributeName="Gamertag")
	public String getGamertag() {
		return gamertag;
	}

	public void setGamertag(String gamertag) {
		this.gamertag = gamertag;
	}

	@DynamoDBAttribute(attributeName="LastMatch")
	public String getLastMatch() {
		return lastMatch;
	}

	public void setLastMatch(String lastMatch) {
		this.lastMatch = lastMatch;
	}

	@DynamoDBAttribute(attributeName="TotalGamesLoaded")
	public Integer getTotalGamesLoaded() {
		return totalGamesLoaded;
	}

	public void setTotalGamesLoaded(Integer totalGamesLoaded) {
		this.totalGamesLoaded = totalGamesLoaded;
	}

	@DynamoDBAttribute(attributeName="LastUpdated")
	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public PlayerGamesCheckpoint(String gamertag, Integer totalGamesLoaded, String lastMatch) {
		this.gamertag = gamertag;
		this.totalGamesLoaded = totalGamesLoaded;
		this.lastMatch = lastMatch;
		this.lastUpdated = new Date();
	}

	public PlayerGamesCheckpoint(String gamertag) {
		this(gamertag, 0, "");
	}

	public PlayerGamesCheckpoint() {
	}

}
