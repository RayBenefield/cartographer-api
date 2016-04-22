package com.cartographerapi.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshalling;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

@DynamoDBTable(tableName="PlayerGames")
public class PlayerGame {
	
	private ObjectMapper mapper;
	private String gamertag;
	private String matchId;
	private Integer gameNumber;
	private JsonNode gameData;

	@DynamoDBHashKey(attributeName="Gamertag")
	public String getGamertag() {
		return gamertag;
	}

	public void setGamertag(String gamertag) {
		this.gamertag = gamertag;
	}

	@DynamoDBAttribute(attributeName="MatchId")
	public String getMatchId() {
		return matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}

	@DynamoDBRangeKey(attributeName="GameNumber")
	public Integer getGameNumber() {
		return gameNumber;
	}

	public void setGameNumber(Integer gameNumber) {
		this.gameNumber = gameNumber;
	}
	
	@DynamoDBMarshalling (marshallerClass = JsonNodeMarshaller.class)
	public JsonNode getGameData() {
		return gameData;
	}
	
	public void setGameData(JsonNode gameData) {
		this.gameData = gameData;
	}
	
	public void setGameData(String gameData) {
		try {
			this.gameData = mapper.readTree(gameData);
		} catch (IOException exception) {
			this.gameData = mapper.createObjectNode();
			this.setGameNumber(0);
		}
	}

	public PlayerGame(String gamertag, Integer gameNumber, String gameData) {
		mapper = new ObjectMapper();

		this.gamertag = gamertag;
		this.gameNumber = gameNumber;
		this.setGameData(gameData);
		this.matchId = this.gameData.path("Id").path("MatchId").asText();
	}

	public PlayerGame(String gamertag, Integer gameNumber, JsonNode gameData) {
		mapper = new ObjectMapper();

		this.gamertag = gamertag;
		this.gameNumber = gameNumber;
		this.gameData = gameData;
		this.matchId = this.gameData.path("Id").path("MatchId").asText();
	}

	// TODO Change gameData column to be ProperCase instead of camel case
	public PlayerGame(Item item) {
		this(
			item.getString("Gamertag"),
			item.getNumber("GameNumber").intValue(),
			item.getString("gameData")
		);
	}
	
	public PlayerGame() {
	}
}
