package com.cartographerapi.domain.bareplayergames;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

import java.util.List;
import java.util.ArrayList;

/**
 * Writer repository for BarePlayerGames to a DynamoDB table.
 *
 * @see BarePlayerGamesWriter
 *
 * @author GodlyPerfection
 *
 */
public class BarePlayerGamesDynamoWriter implements BarePlayerGamesWriter {

    private AmazonDynamoDBClient client;
    private DynamoDBMapper dbMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public BarePlayerGame saveBarePlayerGame(BarePlayerGame game) {
        dbMapper.save(game);
        return game;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BarePlayerGame> saveBarePlayerGames(List<BarePlayerGame> games) {
        dbMapper.batchSave(games);
        return games;
    }

    /**
     * The lazy IOC constructor.
     */
    public BarePlayerGamesDynamoWriter() {
        client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(Regions.US_WEST_2));
        dbMapper = new DynamoDBMapper(client);
    }

}
