package com.cartographerapi.domain.playergamescheckpoints;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

/**
 * Reader repository for PlayerGamesCheckpoint from a DynamoDB table.
 * 
 * @see PlayerGamesCheckpointReader
 * 
 * @author GodlyPerfection
 *
 */
public class PlayerGamesCheckpointDynamoReader implements PlayerGamesCheckpointReader {

	private AmazonDynamoDBClient client;
	private DynamoDBMapper dbMapper;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerGamesCheckpoint getPlayerGamesCheckpointWithDefault(String gamertag) {
		PlayerGamesCheckpoint checkpoint = dbMapper.load(PlayerGamesCheckpoint.class, gamertag);
		
		if (checkpoint == null) {
			checkpoint = new PlayerGamesCheckpoint(gamertag);
		}

		return checkpoint;
	}

    /**
     * The lazy IOC constructor.
     */
	public PlayerGamesCheckpointDynamoReader() {
		client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(Regions.US_WEST_2));
		dbMapper = new DynamoDBMapper(client);
	}

}
