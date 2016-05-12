package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.cartographerapi.domain.CapiUtils;
import com.cartographerapi.domain.ScheduledEvent;
import com.cartographerapi.domain.mapgames.MapGame;
import com.cartographerapi.domain.mapgames.MapGamesWriter;
import com.cartographerapi.domain.mapgames.MapGamesDynamoWriter;
import com.cartographerapi.domain.game.Game;
import com.cartographerapi.domain.game.GamesQueueReader;
import com.cartographerapi.domain.game.GamesSqsReader;

import java.util.List;
import java.util.ArrayList;

/**
 * Go through every game in the queue and store the Map to Game relationship.
 * 
 * @author GodlyPerfection
 * 
 */
public class MapGamesAdder implements RequestHandler<ScheduledEvent, List<MapGame>> {

	private GamesQueueReader queueReader;
	private MapGamesWriter cacheWriter;

	/**
	 * Pulls games from the queue and transforms them into MapGames to save in
	 * the cache.
	 * 
	 * @param input The Cloudwatch scheduled event that triggered this.
	 * @param context The Lambda execution context.
	 * @return The newly added MapGames.
	 */
    @Override
    public List<MapGame> handleRequest(ScheduledEvent input, Context context) {
		CapiUtils.logObject(context, input, "ScheduledEvent Input");
        List<MapGame> results = new ArrayList<MapGame>();

        // Pull games from the queue to inspect
        List<Game> games = queueReader.getNumberOfGames(10);

        // Transform each game into a map game.
        for (Game game : games) {
			MapGame foundMapGame = new MapGame(game);
			cacheWriter.saveMapGame(foundMapGame);
			results.add(foundMapGame);
			queueReader.processedGame(game);
        }

		CapiUtils.logObject(context, results.size(), "# of MapGames Added");
        return results;
    }
    
    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public MapGamesAdder() {
    	this(
    		new GamesSqsReader("sqsCapiGamesForMapGames"),
    		new MapGamesDynamoWriter()
		);
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public MapGamesAdder(GamesQueueReader queueReader, MapGamesWriter cacheWriter) {
    	this.queueReader = queueReader;
    	this.cacheWriter = cacheWriter;
    }

}
