package com.cartographerapi.domain.gameevents;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import com.cartographerapi.domain.game.MatchId;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Domain object that handles the events that happen for a game.
 * <pre>
 *    MatchId
 *    EventCount
 * </pre>
 *
 * @author GodlyPerfection
 *
 */
@DynamoDBTable(tableName="GameEvents")
public class GameEvents {

    @JsonProperty("MatchId")
    private String matchId;

    @JsonProperty("EventCount")
    private Integer eventCount;

    @DynamoDBHashKey(attributeName="MatchId")
    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    @DynamoDBAttribute(attributeName="EventCount")
    public Integer getEventCount() {
        return eventCount;
    }

    public void setEventCount(Integer eventCount) {
        this.eventCount = eventCount;
    }

    public GameEvents(MatchId matchId, Integer eventCount) {
        this.matchId = matchId.getMatchId();
        this.eventCount = eventCount;
    }

    public GameEvents() {
    }

}
