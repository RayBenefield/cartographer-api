package com.cartographerapi.domain.game;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshalling;

import com.cartographerapi.domain.JsonNodeMarshaller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.IOException;

import java.util.Map;
import java.util.LinkedHashMap;

/**
 * Domain object that handles all relevant game details.
 * <pre>
 *    MatchId
 * </pre>
 *
 * @author GodlyPerfection
 *
 */
@DynamoDBTable(tableName="BareGames")
public class BareGame {

    private ObjectMapper mapper;

    @JsonProperty("MatchId")
    private String matchId;

    @DynamoDBHashKey(attributeName="MatchId")
    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    @SuppressWarnings("unchecked")
    public BareGame(Map<String, Object> map) {
        this(
            (String)map.get("matchId")
        );
    }

    public BareGame(String matchId) {
        this.matchId = matchId;
    }

    public BareGame(Game game) {
        this.matchId = game.getMatchId();
    }

    public BareGame(Item item) {
        this(
            item.getString("MatchId")
        );
    }

    public BareGame() {
        mapper = new ObjectMapper();
    }

}
