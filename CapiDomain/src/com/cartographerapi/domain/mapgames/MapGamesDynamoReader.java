package com.cartographerapi.domain.mapgames;

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
 * Reader repository for MapGames from a DynamoDB table.
 *
 * @see MapGamesWriter
 *
 * @author GodlyPerfection
 *
 */
public class MapGamesDynamoReader implements MapGamesReader {

    private AmazonDynamoDBClient client;
    private DynamoDBMapper dbMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MapGame> getMapGamesByMapId(MapId mapId) {
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(mapId.getMapId()));

        DynamoDBQueryExpression<MapGame> queryExpression = new DynamoDBQueryExpression<MapGame>()
            .withKeyConditionExpression("MapId = :val1")
            .withExpressionAttributeValues(eav);

        return dbMapper.query(MapGame.class, queryExpression);
    }

    /**
     * The lazy IOC constructor.
     */
    public MapGamesDynamoReader() {
        client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(Regions.US_WEST_2));
        dbMapper = new DynamoDBMapper(client);
    }

}
