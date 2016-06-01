package com.cartographerapi.domain.gameevents;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import java.util.List;

/**
 * Writer repository for GameEvents into a DynamoDB table.
 *
 * @see GameEventsWriter
 *
 * @author GodlyPerfection
 *
 */
public class GameEventsDynamoWriter implements GameEventsWriter {

    private AmazonDynamoDBClient client;
    private DynamoDBMapper dbMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public GameEvents saveGameEvents(GameEvents gameEvents) {
        dbMapper.save(gameEvents);
        return gameEvents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GameEvents> saveGameEvents(List<GameEvents> gameEvents) {
        dbMapper.batchSave(gameEvents);
        return gameEvents;
    }

    /**
     * The lazy IOC constructor.
     */
    public GameEventsDynamoWriter() {
        client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(Regions.US_WEST_2));
        dbMapper = new DynamoDBMapper(client);
    }

}
