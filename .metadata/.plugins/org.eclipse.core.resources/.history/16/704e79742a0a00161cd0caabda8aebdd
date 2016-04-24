package com.cartographerapi.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshalling;
import java.io.IOException;
import java.util.Map;
import java.util.LinkedHashMap;

@DynamoDBTable(tableName="Games")
public class Game {

	private ObjectMapper mapper;
	private String matchId;
	private JsonNode gameData;

	@DynamoDBHashKey(attributeName="MatchId")
	public String getMatchId() {
		return matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}
	
	@DynamoDBAttribute(attributeName="GameData")
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
		}
	}

	@SuppressWarnings("unchecked")
	public Game(Map<String, Object> map) {
		this(
			(String)map.get("matchId"),
			(LinkedHashMap<String, Object>)map.get("gameData")
		);
	}
	
	public Game(String matchId, Map<String, Object> gameData) {
		mapper = new ObjectMapper();

		this.matchId = matchId;
		try {
			this.setGameData(mapper.writeValueAsString(gameData));
		} catch (IOException exception) {
		}
	}
	
	public Game(String matchId, JsonNode gameData) {
		this.matchId = matchId;
		this.gameData = gameData;
	}
	
	public Game(String matchId, String gameData) {
		mapper = new ObjectMapper();

		this.matchId = matchId;
		this.setGameData(gameData);
	}

	public Game(Item item) {
		this(
			item.getString("MatchId"),
			item.getString("GameData")
		);
	}

	public Game() {
	}

}
