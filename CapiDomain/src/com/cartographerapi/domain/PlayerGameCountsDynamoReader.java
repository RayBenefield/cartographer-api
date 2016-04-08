package com.cartographerapi.domain;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

/**
 * Reader repository for PlayerGameCounts from a DynamoDB table.
 * 
 * @see PlayerGameCountsReader
 * 
 * @author GodlyPerfection
 *
 */
public class PlayerGameCountsDynamoReader implements PlayerGameCountsReader {

	private AmazonDynamoDBClient client;
	private DynamoDBMapper dbMapper;

	public PlayerGameCountsDynamoReader() {
		client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(Regions.US_WEST_2));
		dbMapper = new DynamoDBMapper(client);
	}

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
	public PlayerGameCounts getPlayerGameCountsByPlayerGameCounts(PlayerGameCounts counts) {
		PlayerGameCounts foundCounts = dbMapper.load(PlayerGameCounts.class, counts.getGamertag());
		if (foundCounts == null) {
			return counts;
		}
		
		return foundCounts;
	}

}