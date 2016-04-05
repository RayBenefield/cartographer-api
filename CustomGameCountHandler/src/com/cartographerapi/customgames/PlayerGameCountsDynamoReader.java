package com.cartographerapi.customgames;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

public class PlayerGameCountsDynamoReader implements PlayerGameCountsReader {

	AmazonDynamoDBClient client = new AmazonDynamoDBClient();
	DynamoDBMapper dbMapper;
	
	public PlayerGameCountsDynamoReader() {
		client.setRegion(Region.getRegion(Regions.US_WEST_2));
		dbMapper = new DynamoDBMapper(client);
	}

	@Override
	public PlayerGameCounts getPlayerGameCountsByGamertag(String gamertag) {
		return dbMapper.load(PlayerGameCounts.class, gamertag);
	}

	@Override
	public PlayerGameCounts getPlayerGameCountsByPlayerGameCounts(PlayerGameCounts counts) {
		PlayerGameCounts foundCounts = dbMapper.load(PlayerGameCounts.class, counts.getGamertag());
		if (foundCounts == null) {
			return counts;
		}
		
		return foundCounts;
	}

}
