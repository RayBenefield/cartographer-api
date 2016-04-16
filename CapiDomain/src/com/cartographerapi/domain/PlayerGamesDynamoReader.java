package com.cartographerapi.domain;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

public class PlayerGamesDynamoReader implements PlayerGamesReader {

	private AmazonDynamoDBClient client;
	private DynamoDBMapper dbMapper;

	public PlayerGamesDynamoReader() {
		client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(Regions.US_WEST_2));
		dbMapper = new DynamoDBMapper(client);
	}

	@Override
	public List<PlayerGame> getPlayerGamesByGamertag(String gamertag) {
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(gamertag));

        DynamoDBQueryExpression<PlayerGame> queryExpression = new DynamoDBQueryExpression<PlayerGame>()
			.withKeyConditionExpression("Gamertag = :val1")
			.withExpressionAttributeValues(eav);
                    
		List<PlayerGame> games = dbMapper.query(PlayerGame.class, queryExpression);
		return games;
	}

	@Override
	//TODO actually implement this method with the start and count
	public List<PlayerGame> getPlayerGamesByGamertag(String gamertag, Integer start, Integer count) {
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(gamertag));

        DynamoDBQueryExpression<PlayerGame> queryExpression = new DynamoDBQueryExpression<PlayerGame>()
			.withKeyConditionExpression("Gamertag = :val1")
			.withExpressionAttributeValues(eav);
                    
		List<PlayerGame> games = dbMapper.query(PlayerGame.class, queryExpression);
		return games;
	}

}
