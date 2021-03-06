package com.cartographerapi.domain.playergamecounts;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

import com.amazonaws.util.StringUtils;

import com.cartographerapi.domain.players.Player;

/**
 * Reader repository for PlayerGameCounts from a DynamoDB table.
 *
 * @see PlayerGameCountsReader
 *
 * @author GodlyPerfection
 *
 */
public class PlayerGameCountsDynamoReader implements PlayerGameCountsUpdatedReader {

    private AmazonDynamoDBClient client;
    private DynamoDBMapper dbMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public PlayerGameCounts getPlayerGameCountsByGamertag(String gamertag) {
        return dbMapper.load(PlayerGameCounts.class, gamertag);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PlayerGameCounts> getPlayerGameCountsByPlayers(List<Player> players) {
        List<PlayerGameCounts> counts = new ArrayList<PlayerGameCounts>();

        for (Player player : players) {
            if (StringUtils.isNullOrEmpty(player.getGamertag())) {
                continue;
            }

            PlayerGameCounts count = dbMapper.load(PlayerGameCounts.class, player.getGamertag());

            if (count != null) {
                counts.add(count);
            }
        }

        return counts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlayerGameCounts getPlayerGameCountsByPlayerGameCounts(PlayerGameCounts counts) {
        PlayerGameCounts foundCounts = dbMapper.load(PlayerGameCounts.class, counts.getGamertag());
        if (foundCounts == null) {
            return counts;
        }

        return foundCounts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PlayerGameCounts> getAllPlayerGameCountsNotUpdatedSince(String date) {
        // TODO Refactor this to use the DBMapper scanning instead which will clean up some code.
        List<PlayerGameCounts> result = new ArrayList<PlayerGameCounts>();
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>();
        expressionAttributeValues.put(":val", new AttributeValue().withS(date));

        ScanRequest scanRequest = new ScanRequest()
            .withTableName("PlayerGameCounts")
            .withFilterExpression("LastUpdated < :val")
            .withExpressionAttributeValues(expressionAttributeValues);

        ScanResult scanResult = client.scan(scanRequest);
        for (Map<String, AttributeValue> scanItem : scanResult.getItems()){
            Item item = Item.fromMap(InternalUtils.toSimpleMapValue(scanItem));
            PlayerGameCounts counts = new PlayerGameCounts(item);
            result.add(counts);
        }

        return result;
    }

    /**
     * The lazy IOC constructor.
     */
    public PlayerGameCountsDynamoReader() {
        client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(Regions.US_WEST_2));
        dbMapper = new DynamoDBMapper(client);
    }

}
