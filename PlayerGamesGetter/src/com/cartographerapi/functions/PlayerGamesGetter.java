package com.cartographerapi.functions;

import java.io.IOException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.List;
import java.util.ArrayList;
import com.cartographerapi.domain.Gamertag;
import com.cartographerapi.domain.PlayerGame;
import com.cartographerapi.domain.PlayerGamesReader;
import com.cartographerapi.domain.PlayerGamesWriter;
import com.cartographerapi.domain.Halo5ApiWrapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

public class PlayerGamesGetter implements RequestHandler<Gamertag, List<PlayerGame>> {

	private PlayerGamesReader cacheReader;
//	private PlayerGamesWriter sourceWriter;

    @Override
    public List<PlayerGame> handleRequest(Gamertag input, Context context) {

//		AmazonDynamoDBClient client;
//		DynamoDBMapper dbMapper;
//	
//		client = new AmazonDynamoDBClient();
//		client.setRegion(Region.getRegion(Regions.US_WEST_2));
//		dbMapper = new DynamoDBMapper(client);

//        context.getLogger().log("Input: " + input);
//        
//        List<PlayerGame> result = new ArrayList<PlayerGame>();
//		Halo5ApiWrapper api = new Halo5ApiWrapper("ae4df7c91357455ea30be2d7bdf15522");
//
//		PlayerGame game;
//		try {
//			String totalResult = api.customGames(input.getGamertag());
//			game = new PlayerGame(input.getGamertag(), 1, totalResult);
//		} catch (IOException exception) {
//			game = new PlayerGame(input.getGamertag(), 0, "");
//		}
//
//		dbMapper.save(game);
//		PlayerGame game = dbMapper.load(PlayerGame.class, "godlyperfection", 1);
//		result.add(game);
//        return result;
		List<PlayerGame> games = cacheReader.getPlayerGamesByGamertag(input.getGamertag());
		
		if (games.isEmpty()) {
//			games = sourceWriter.savePlayerGames(new ArrayList<PlayerGame>());
		}

		return games;
    }
    
    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public PlayerGamesGetter() {
    	this(new PlayerGamesDynamoReader());
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public PlayerGamesGetter(PlayerGamesReader cacheReader) {
    	this.cacheReader = cacheReader;
//    	this.sourceWriter = sourceWriter;
    }

}
