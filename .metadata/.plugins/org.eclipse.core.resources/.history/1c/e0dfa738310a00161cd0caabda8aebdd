package com.cartographerapi.domain;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import java.util.List;

/**
 * Writer repository for Games into a DynamoDB table.
 * 
 * @see GamesWriter
 * 
 * @author GodlyPerfection
 *
 */
public class GamesDynamoWriter implements GamesWriter {

	private AmazonDynamoDBClient client;
	private DynamoDBMapper dbMapper;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Game saveGame(Game game) {
		dbMapper.save(game);
		return game;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Game> saveGames(List<Game> games) {
		dbMapper.batchSave(games);
		return games;
	}
	
    /**
     * The lazy IOC constructor.
     */
	public GamesDynamoWriter() {
		client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(Regions.US_WEST_2));
		dbMapper = new DynamoDBMapper(client);
	}

}
