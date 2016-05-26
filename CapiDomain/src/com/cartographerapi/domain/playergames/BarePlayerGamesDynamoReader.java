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

/**
 * Reader repository for BarePlayerGames from a DynamoDB table.
 *
 * @see BarePlayerGamesWriter
 *
 * @author GodlyPerfection
 *
 */
public class BarePlayerGamesDynamoReader implements BarePlayerGamesReader {

    private AmazonDynamoDBClient client;
    private DynamoDBMapper dbMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BarePlayerGame> getBarePlayerGamesByGamertag(String gamertag) {
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(gamertag));

        DynamoDBQueryExpression<BarePlayerGame> queryExpression = new DynamoDBQueryExpression<BarePlayerGame>()
            .withKeyConditionExpression("Gamertag = :val1")
            .withExpressionAttributeValues(eav);

        return dbMapper.query(BarePlayerGame.class, queryExpression);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    //TODO actually implement this method with the start and count
    public List<BarePlayerGame> getBarePlayerGamesByGamertag(String gamertag, Integer start, Integer count) {
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(gamertag));

        DynamoDBQueryExpression<BarePlayerGame> queryExpression = new DynamoDBQueryExpression<BarePlayerGame>()
            .withKeyConditionExpression("Gamertag = :val1")
            .withExpressionAttributeValues(eav);

        return dbMapper.query(BarePlayerGame.class, queryExpression);
    }

    /**
     * The lazy IOC constructor.
     */
    public BarePlayerGamesDynamoReader() {
        client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(Regions.US_WEST_2));
        dbMapper = new DynamoDBMapper(client);
    }

}