package com.cartographerapi.domain.playergames;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

import com.cartographerapi.domain.players.Player;

/**
 * Reader repository for PlayerGames from a DynamoDB table.
 *
 * @see PlayerGamesWriter
 *
 * @author GodlyPerfection
 *
 */
public class PlayerGamesDynamoReader implements PlayerGamesReader {

    private AmazonDynamoDBClient client;
    private DynamoDBMapper dbMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PlayerGame> getPlayerGamesByGamertag(String gamertag) {
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(gamertag));

        DynamoDBQueryExpression<PlayerGame> queryExpression = new DynamoDBQueryExpression<PlayerGame>()
            .withKeyConditionExpression("Gamertag = :val1")
            .withExpressionAttributeValues(eav);

        return dbMapper.query(PlayerGame.class, queryExpression);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    //TODO actually implement this method with the start and count
    public List<PlayerGame> getPlayerGamesByGamertag(String gamertag, Integer start, Integer count) {
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(gamertag));

        DynamoDBQueryExpression<PlayerGame> queryExpression = new DynamoDBQueryExpression<PlayerGame>()
            .withKeyConditionExpression("Gamertag = :val1")
            .withExpressionAttributeValues(eav);

        return dbMapper.query(PlayerGame.class, queryExpression);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Player, List<PlayerGame>> getPlayerGamesByPlayers(List<Player> players) {
        Map<Player, List<PlayerGame>> results = new HashMap<Player, List<PlayerGame>>();

        for (Player player : players) {
            results.put(player, this.getPlayerGamesByGamertag(player.getGamertag()));
        }

        return results;
    }

    /**
     * The lazy IOC constructor.
     */
    public PlayerGamesDynamoReader() {
        client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(Regions.US_WEST_2));
        dbMapper = new DynamoDBMapper(client);
    }

}
