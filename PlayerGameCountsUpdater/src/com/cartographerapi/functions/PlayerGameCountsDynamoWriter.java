package com.cartographerapi.functions;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

/**
 * Writer repository for PlayerGameCounts into a DynamoDB table.
 * 
 * @see PlayerGameCountsWriter
 * 
 * @author GodlyPerfection
 *
 */
public class PlayerGameCountsDynamoWriter implements PlayerGameCountsWriter {

	private AmazonDynamoDBClient client;
	private DynamoDBMapper dbMapper;
	
	public PlayerGameCountsDynamoWriter() {
		client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(Regions.US_WEST_2));
		dbMapper = new DynamoDBMapper(client);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerGameCounts savePlayerGameCounts(PlayerGameCounts counts) {
		dbMapper.save(counts);
		return counts;
	}

}
