package com.cartographerapi.customgames;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

public class PlayerGameCountsDynamoWriter implements PlayerGameCountsWriter {

	AmazonDynamoDBClient client = new AmazonDynamoDBClient();
	DynamoDBMapper dbMapper;
	
	public PlayerGameCountsDynamoWriter() {
		client.setRegion(Region.getRegion(Regions.US_WEST_2));
		dbMapper = new DynamoDBMapper(client);
	}

	@Override
	public PlayerGameCounts savePlayerGameCounts(PlayerGameCounts counts) {
		dbMapper.save(counts);
		return counts;
	}

}
