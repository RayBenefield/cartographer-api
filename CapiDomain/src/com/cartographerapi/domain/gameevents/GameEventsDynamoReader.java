package com.cartographerapi.domain.gameevents;

import java.util.List;
import java.util.ArrayList;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

import com.amazonaws.util.StringUtils;

import com.cartographerapi.domain.game.MatchId;

/**
 * Reader repository for GameEvents from a DynamoDB table.
 *
 * @see GameEventsWriter
 *
 * @author GodlyPerfection
 *
 */
public class GameEventsDynamoReader implements GameEventsReader {

    private AmazonDynamoDBClient client;
    private DynamoDBMapper dbMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public GameEvents getGameEventsByMatchId(MatchId matchId) {
		return dbMapper.load(GameEvents.class, matchId.getMatchId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GameEvents> getGameEventsByMatchIds(List<MatchId> matchIds) {
        List<GameEvents> results = new ArrayList<GameEvents>();

        for (MatchId matchId : matchIds) {
            if (StringUtils.isNullOrEmpty(matchId.getMatchId())) {
                continue;
            }

            GameEvents events = this.getGameEventsByMatchId(matchId);

            if (events != null) {
                results.add(events);
            }
        }

        return results;
    }

    /**
     * The lazy IOC constructor.
     */
    public GameEventsDynamoReader() {
        client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(Regions.US_WEST_2));
        dbMapper = new DynamoDBMapper(client);
    }

}
