package com.cartographerapi.domain.playergames;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

import java.util.List;
import java.util.ArrayList;

/**
 * Writer repository for PlayerGames to a DynamoDB table.
 *
 * @see PlayerGamesWriter
 *
 * @author GodlyPerfection
 *
 */
public class PlayerGamesDynamoWriter implements PlayerGamesWriter {

    private AmazonDynamoDBClient client;
    private DynamoDBMapper dbMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PlayerGame> savePlayerGames(List<PlayerGame> games) {
        dbMapper.batchSave(games);

        List<BarePlayerGame> bareGames = new ArrayList<BarePlayerGame>();
        for (PlayerGame game : games) {
            BarePlayerGame bareGame = new BarePlayerGame(game);
            bareGames.add(bareGame);
        }

        dbMapper.batchSave(bareGames);
        return games;
    }

    /**
     * The lazy IOC constructor.
     */
    public PlayerGamesDynamoWriter() {
        client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(Regions.US_WEST_2));
        dbMapper = new DynamoDBMapper(client);
    }

}
