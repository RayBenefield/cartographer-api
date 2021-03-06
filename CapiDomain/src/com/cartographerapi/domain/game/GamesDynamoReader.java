package com.cartographerapi.domain.game;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

/**
 * Reader repository for Games from a DynamoDB table.
 * 
 * @see GamesReader
 * 
 * @author GodlyPerfection
 *
 */
public class GamesDynamoReader implements GamesReader {

	private AmazonDynamoDBClient client;
	private DynamoDBMapper dbMapper;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Game getGameByMatchId(MatchId matchId) {
		return dbMapper.load(Game.class, matchId.getMatchId());
	}

    /**
     * The lazy IOC constructor.
     */
	public GamesDynamoReader() {
		client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(Regions.US_WEST_2));
		dbMapper = new DynamoDBMapper(client);
	}

}
