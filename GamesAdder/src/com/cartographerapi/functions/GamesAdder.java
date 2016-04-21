package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.cartographerapi.domain.Game;
import com.cartographerapi.domain.GamesReader;
import com.cartographerapi.domain.GamesDynamoReader;
import com.cartographerapi.domain.GamesWriter;
import com.cartographerapi.domain.GamesDynamoWriter;
import com.cartographerapi.domain.GamesHaloApiReader;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;

public class GamesAdder implements RequestHandler<SNSEvent, Game> {
	
	private GamesReader cacheReader;
	private GamesReader sourceReader;
	private GamesWriter cacheWriter;
    private ObjectMapper mapper = new ObjectMapper();

	@SuppressWarnings("unchecked")
    @Override
    public Game handleRequest(SNSEvent input, Context context) {
        context.getLogger().log("Input: " + input);

		Game game;
		try {
			Map<String, Object> gameMap = mapper.readValue(input.getRecords().get(0).getSNS().getMessage(), HashMap.class);
			game = new Game(gameMap);
		} catch (IOException exception) {
			return new Game("message", mapper.createObjectNode());
		}

        Game cachedGame = cacheReader.getGameByMatchId(game.getMatchId());
        if (cachedGame != null) {
        	return cachedGame;
        }

        Game foundGame = null;
		foundGame = sourceReader.getGameByMatchId(game.getMatchId());
        
        if (foundGame != null) {
			cacheWriter.saveGame(foundGame);
        }
        
        return foundGame;
    }
    
    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public GamesAdder() {
    	this(
    		new GamesDynamoReader(),
    		new GamesHaloApiReader(),
    		new GamesDynamoWriter()
		);
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public GamesAdder(GamesReader cacheReader, GamesReader sourceReader, GamesWriter cacheWriter) {
    	this.cacheReader = cacheReader;
    	this.sourceReader = sourceReader;
    	this.cacheWriter = cacheWriter;
    }

}
