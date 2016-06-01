package com.cartographerapi.domain.baregames;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

import java.util.List;
import java.util.ArrayList;

/**
 * Writer repository for BareGames to a DynamoDB table.
 *
 * @see BareGamesWriter
 *
 * @author GodlyPerfection
 *
 */
public class BareGamesDynamoWriter implements BareGamesWriter {

    private AmazonDynamoDBClient client;
    private DynamoDBMapper dbMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public BareGame saveBareGame(BareGame game) {
        dbMapper.save(game);
        return game;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BareGame> saveBareGames(List<BareGame> games) {
        dbMapper.batchSave(games);
        return games;
    }

    /**
     * The lazy IOC constructor.
     */
    public BareGamesDynamoWriter() {
        client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(Regions.US_WEST_2));
        dbMapper = new DynamoDBMapper(client);
    }

}
