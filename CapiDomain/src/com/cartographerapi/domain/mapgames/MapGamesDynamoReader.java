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
import com.amazonaws.util.StringUtils;

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
     * {@inheritDoc}
     */
    @Override
    public Map<MapId, List<MapGame>> getMapGamesByMapIds(List<MapId> mapIds) {
        Map<MapId, List<MapGame>> results = new HashMap<MapId, List<MapGame>>();

        for (MapId mapId : mapIds) {
            if (StringUtils.isNullOrEmpty(mapId.getMapId())) {
                continue;
            }

            List<MapGame> games = this.getMapGamesByMapId(mapId);

            if (games.size() > 0) {
                results.put(mapId, games);
            }
        }

        return results;
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
