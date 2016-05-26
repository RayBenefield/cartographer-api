package com.cartographerapi.domain.game;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

/**
 * Reader repository for BareGames from a DynamoDB table.
 *
 * @see BareGamesReader
 *
 * @author GodlyPerfection
 *
 */
public class BareGamesDynamoReader implements BareGamesReader {

    private AmazonDynamoDBClient client;
    private DynamoDBMapper dbMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public BareGame getBareGameByMatchId(MatchId matchId) {
        return dbMapper.load(BareGame.class, matchId.getMatchId());
    }

    /**
     * The lazy IOC constructor.
     */
    public BareGamesDynamoReader() {
        client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(Regions.US_WEST_2));
        dbMapper = new DynamoDBMapper(client);
    }

}
