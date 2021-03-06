package com.cartographerapi.domain.playergamecounts;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import java.util.Date;

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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerGameCounts savePlayerGameCounts(PlayerGameCounts counts) {
		if (counts.getTotalGames() > 0) {
			counts.setLastUpdated(new Date());
			dbMapper.save(counts);
		}
		return counts;
	}
	
    /**
     * The lazy IOC constructor.
     */
	public PlayerGameCountsDynamoWriter() {
		client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(Regions.US_WEST_2));
		dbMapper = new DynamoDBMapper(client);
	}

}
