package com.cartographerapi.domain.game;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshalling;

import com.cartographerapi.domain.JsonNodeMarshaller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

import java.util.Map;
import java.util.LinkedHashMap;

/**
 * Domain object that handles all relevant game details.
 * <pre>
 *    MatchId
 *    GameData
 * </pre>
 *
 * @author GodlyPerfection
 *
 */
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
            new MatchId((String)map.get("matchId")),
            (LinkedHashMap<String, Object>)map.get("gameData")
        );
    }

    public Game(MatchId matchId, Map<String, Object> gameData) {
        mapper = new ObjectMapper();

        this.matchId = matchId.getMatchId();
        try {
            this.setGameData(mapper.writeValueAsString(gameData));
        } catch (IOException exception) {
        }
    }

    public Game(MatchId matchId, JsonNode gameData) {
        this.matchId = matchId.getMatchId();
        this.gameData = gameData;
    }

    public Game(MatchId matchId, String gameData) {
        mapper = new ObjectMapper();

        this.matchId = matchId.getMatchId();
        this.setGameData(gameData);
    }

    public Game(Item item) {
        this(
            new MatchId(item.getString("MatchId")),
            item.getString("GameData")
        );
    }

    public Game() {
        mapper = new ObjectMapper();
    }

}
