package com.cartographerapi.domain.playergames;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.cartographerapi.domain.JsonNodeMarshaller;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshalling;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Domain object that handles the data for a Player to Game relationship.
 * <pre>
 *    Gamertag
 *    MatchId
 *    GameNumber
 * </pre>
 *
 * @author GodlyPerfection
 *
 */
@DynamoDBTable(tableName="BarePlayerGames")
public class BarePlayerGame {

    private ObjectMapper mapper;

    @JsonProperty("Gamertag")
    private String gamertag;

    @JsonProperty("MatchId")
    private String matchId;

    @JsonProperty("GameNumber")
    private Integer gameNumber;

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

    public BarePlayerGame(PlayerGame game) {
        mapper = new ObjectMapper();

        this.gamertag = game.getGamertag();
        this.gameNumber = game.getGameNumber();
        this.matchId = game.getMatchId();
    }

    public BarePlayerGame(String gamertag, Integer gameNumber, String matchId) {
        mapper = new ObjectMapper();

        this.gamertag = gamertag;
        this.gameNumber = gameNumber;
        this.matchId = matchId;
    }

    public BarePlayerGame(Item item) {
        this(
            item.getString("Gamertag"),
            item.getNumber("GameNumber").intValue(),
            item.getString("MatchId")
        );
    }

    public BarePlayerGame() {
    }
}
