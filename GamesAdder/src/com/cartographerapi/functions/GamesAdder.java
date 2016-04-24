package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.cartographerapi.domain.ScheduledEvent;
import com.cartographerapi.domain.game.Game;
import com.cartographerapi.domain.game.GamesDynamoReader;
import com.cartographerapi.domain.game.GamesDynamoWriter;
import com.cartographerapi.domain.game.GamesHaloApiReader;
import com.cartographerapi.domain.game.GamesReader;
import com.cartographerapi.domain.game.GamesWriter;
import com.cartographerapi.domain.playergames.PlayerGame;
import com.cartographerapi.domain.playergames.PlayerGamesQueueReader;
import com.cartographerapi.domain.playergames.PlayerGamesSqsReader;

import java.util.List;
import java.util.ArrayList;

public class GamesAdder implements RequestHandler<ScheduledEvent, List<Game>> {
	
	private PlayerGamesQueueReader queueReader;
	private GamesReader cacheReader;
	private GamesReader sourceReader;
	private GamesWriter cacheWriter;

    @Override
    public List<Game> handleRequest(ScheduledEvent input, Context context) {
        context.getLogger().log("Input: " + input);
        List<Game> results = new ArrayList<Game>();

        List<PlayerGame> games = queueReader.getNumberOfPlayerGames(10);
        
        for (PlayerGame game : games) {
			Game cachedGame = cacheReader.getGameByMatchId(game.getMatchId());
			if (cachedGame != null) {
				queueReader.processedPlayerGame(game);
				continue;
			}

			Game foundGame = null;
			foundGame = sourceReader.getGameByMatchId(game.getMatchId());
			
			if (foundGame != null) {
				cacheWriter.saveGame(foundGame);
				queueReader.processedPlayerGame(game);
				results.add(foundGame);
			}
        }

        return results;
    }
    
    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public GamesAdder() {
    	this(
    		new GamesDynamoReader(),
    		new GamesHaloApiReader(),
    		new GamesDynamoWriter(),
    		new PlayerGamesSqsReader("https://sqs.us-west-2.amazonaws.com/789201490085/test-playergamepublisher")
		);
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public GamesAdder(GamesReader cacheReader, GamesReader sourceReader, GamesWriter cacheWriter, PlayerGamesQueueReader queueReader) {
    	this.cacheReader = cacheReader;
    	this.sourceReader = sourceReader;
    	this.cacheWriter = cacheWriter;
    	this.queueReader = queueReader;
    }

}
