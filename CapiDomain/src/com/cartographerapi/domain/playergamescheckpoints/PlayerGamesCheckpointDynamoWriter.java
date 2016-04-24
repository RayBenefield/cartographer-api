package com.cartographerapi.domain.playergamescheckpoints;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import java.util.Date;

/**
 * Writer repository for PlayerGamesCheckpoint to a DynamoDB table.
 * 
 * @see PlayerGamesCheckpointWriter
 * 
 * @author GodlyPerfection
 *
 */
public class PlayerGamesCheckpointDynamoWriter implements PlayerGamesCheckpointWriter {

	private AmazonDynamoDBClient client;
	private DynamoDBMapper dbMapper;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerGamesCheckpoint savePlayerGamesCheckpoint(PlayerGamesCheckpoint checkpoint) {
		checkpoint.setLastUpdated(new Date());
		dbMapper.save(checkpoint);
		return checkpoint;
	}

    /**
     * The lazy IOC constructor.
     */
	public PlayerGamesCheckpointDynamoWriter() {
		client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(Regions.US_WEST_2));
		dbMapper = new DynamoDBMapper(client);
	}

}
