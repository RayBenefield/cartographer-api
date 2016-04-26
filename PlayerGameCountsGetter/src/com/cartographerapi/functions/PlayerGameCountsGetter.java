package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.cartographerapi.domain.Gamertag;
import com.cartographerapi.domain.playergamecounts.PlayerGameCounts;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsCapiWriter;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsDynamoReader;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsReader;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsWriter;

/**
 * Gets the PlayerGameCounts from the cache, and adds to the cache if it doesn't
 * exist.
 * 
 * @author GodlyPerfection
 * 
 */
public class PlayerGameCountsGetter implements RequestHandler<Gamertag, PlayerGameCounts> {

	private PlayerGameCountsReader cacheReader;
	private PlayerGameCountsWriter sourceWriter;
	
	/**
	 * Checks the cache to see if PlayerGameCounts already exist. If it doesn't
	 * exist then it calls the updater to try to add it to the cache.
	 * 
	 * @param gamertag The Gamertag given as input to the Lambda function.
	 * @param context The Lambda execution context.
	 * @return The cached PlayerGameCounts for a Gamertag.
	 */
    @Override
    public PlayerGameCounts handleRequest(Gamertag input, Context context) {
        context.getLogger().log("Input: " + input.getGamertag());

		PlayerGameCounts counts = cacheReader.getPlayerGameCountsByGamertag(input.getGamertag());
		
		if (counts == null) {
			counts = sourceWriter.savePlayerGameCounts(new PlayerGameCounts(input.getGamertag()));
		}

		return counts;
    }
    
    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public PlayerGameCountsGetter() {
    	this(new PlayerGameCountsDynamoReader(), new PlayerGameCountsCapiWriter());
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public PlayerGameCountsGetter(PlayerGameCountsReader cacheReader, PlayerGameCountsWriter sourceWriter) {
    	this.cacheReader = cacheReader;
    	this.sourceWriter = sourceWriter;
    }
}
