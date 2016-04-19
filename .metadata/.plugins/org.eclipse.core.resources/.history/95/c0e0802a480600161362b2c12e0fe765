package com.cartographerapi.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.document.Item;
import java.util.Date;
import java.util.Map;
import org.joda.time.DateTimeZone;
import org.joda.time.DateTime;

/**
 * Holds the data relevant to the counts for Games for a single Player.
 * <pre>
 *    Gamertag
 *    GamesCompleted
 *    TotalGames
 *    LastUpdated
 * </pre>
 * 
 * @author GodlyPerfection
 *
 */
@DynamoDBTable(tableName="PlayerGameCounts")
public class PlayerGameCounts {

	private String gamertag;
	private Integer gamesCompleted;
	private Integer totalGames;
	private Date lastUpdated;

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

	@DynamoDBAttribute(attributeName="LastUpdated")
	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public PlayerGameCounts(String gamertag, Integer gamesCompleted, Integer totalGames, Date lastUpdated) {
		this.gamertag = gamertag;
		this.gamesCompleted = gamesCompleted;
		this.totalGames = totalGames;
		this.lastUpdated = lastUpdated;
	}

	public PlayerGameCounts(String gamertag, Integer gamesCompleted, Integer totalGames) {
		this(gamertag, gamesCompleted, totalGames, new Date());
	}

	public PlayerGameCounts(Item item) {
		this(
			item.getString("Gamertag"),
			item.getNumber("GamesCompleted").intValue(),
			item.getNumber("TotalGames").intValue(),
			new DateTime(item.getString("LastUpdated"), DateTimeZone.UTC).toDate()
		);
	}

	public PlayerGameCounts(Map<String, Object> map) {
		this(
			(String)map.get("gamertag"),
			(Integer)map.get("gamesCompleted"),
			(Integer)map.get("totalGames"),
			new DateTime(map.get("lastUpdated"), DateTimeZone.UTC).toDate()
		);
	}

	public PlayerGameCounts(String gamertag) {
		this(gamertag, 0, 0, new Date());
	}

	public PlayerGameCounts() {
	}
}
