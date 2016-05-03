package com.cartographerapi.domain.mapgames;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import java.util.List;

/**
 * Writer repository for MapGames into a DynamoDB table.
 * 
 * @see MapGamesWriter
 * 
 * @author GodlyPerfection
 *
 */
public class MapGamesDynamoWriter implements MapGamesWriter {

	private AmazonDynamoDBClient client;
	private DynamoDBMapper dbMapper;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MapGame saveMapGame(MapGame mapGame) {
		dbMapper.save(mapGame);
		return mapGame;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MapGame> saveMapGames(List<MapGame> mapGames) {
		dbMapper.batchSave(mapGames);
		return mapGames;
	}
	
    /**
     * The lazy IOC constructor.
     */
	public MapGamesDynamoWriter() {
		client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(Regions.US_WEST_2));
		dbMapper = new DynamoDBMapper(client);
	}

}