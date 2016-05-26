package com.cartographerapi.domain.game;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import java.util.List;
import java.util.ArrayList;

/**
 * Writer repository for Games into a DynamoDB table.
 *
 * @see GamesWriter
 *
 * @author GodlyPerfection
 *
 */
public class GamesDynamoWriter implements GamesWriter {

    private AmazonDynamoDBClient client;
    private DynamoDBMapper dbMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public Game saveGame(Game game) {
        dbMapper.save(game);
        dbMapper.save(new BareGame(game));
        return game;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Game> saveGames(List<Game> games) {
        dbMapper.batchSave(games);

        List<BareGame> bareGames = new ArrayList<BareGame>();
        for (Game game : games) {
            BareGame bareGame = new BareGame(game);
            bareGames.add(bareGame);
        }
        dbMapper.batchSave(bareGames);

        return games;
    }

    /**
     * The lazy IOC constructor.
     */
    public GamesDynamoWriter() {
        client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(Regions.US_WEST_2));
        dbMapper = new DynamoDBMapper(client);
    }

}
