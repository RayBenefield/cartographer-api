package com.cartographerapi.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * Holds the data relevant to the counts for Games for a single Player.
 * <pre>
 *    Gamertag
 *    GamesCompleted
 *    TotalGames
 * </pre>
 * 
 * @author GodlyPerfection
 *
 */
@DynamoDBTable(tableName="PlayerGameCounts")
public class PlayerGameCounts {
	String gamertag;
	Integer gamesCompleted;
	Integer totalGames;

	@DynamoDBHashKey(attributeName="Gamertag")
	public String getGamertag() {
		return gamertag;
	}

	public void setGamertag(String gamertag) {
		this.gamertag = gamertag;
	}

	@DynamoDBAttribute(attributeName="GamesCompleted")
	public Integer getGamesCompleted() {
		return gamesCompleted;
	}

	public void setGamesCompleted(Integer gamesCompleted) {
		this.gamesCompleted = gamesCompleted;
	}

	@DynamoDBAttribute(attributeName="TotalGames")
	public Integer getTotalGames() {
		return totalGames;
	}

	public void setTotalGames(Integer totalGames) {
		this.totalGames = totalGames;
	}

	public PlayerGameCounts(String gamertag, Integer gamesCompleted, Integer totalGames) {
		this.gamertag = gamertag;
		this.gamesCompleted = gamesCompleted;
		this.totalGames = totalGames;
	}

	public PlayerGameCounts(String gamertag) {
		this(gamertag, 0, 0);
	}

	public PlayerGameCounts() {
	}
}