package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.cartographerapi.domain.CapiUtils;
import com.cartographerapi.domain.Gamertag;
import com.cartographerapi.domain.playergamecounts.PlayerGameCounts;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsDynamoReader;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsReader;

/**
 * Gets the PlayerGameCounts from the cache, and adds to the cache if it doesn't
 * exist.
 * 
 * @author GodlyPerfection
 * 
 */
public class PlayerGameCountsGetter implements RequestHandler<Gamertag, PlayerGameCounts> {

	private PlayerGameCountsReader cacheReader;
	
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
		PlayerGameCounts counts = cacheReader.getPlayerGameCountsByGamertag(input.getGamertag());
		CapiUtils.logObject(context, counts, "PlayerGameCounts for " + counts.getGamertag());
		return counts;
    }
    
    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public PlayerGameCountsGetter() {
    	this(new PlayerGameCountsDynamoReader());
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public PlayerGameCountsGetter(PlayerGameCountsReader cacheReader) {
    	this.cacheReader = cacheReader;
    }
}
