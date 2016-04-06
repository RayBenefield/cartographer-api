package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.cartographerapi.domain.Gamertag;
import com.cartographerapi.domain.PlayerGameCounts;
import com.cartographerapi.domain.PlayerGameCountsDynamoReader;
import com.cartographerapi.domain.PlayerGameCountsDynamoWriter;
import com.cartographerapi.domain.PlayerGameCountsHaloApiReader;
import com.cartographerapi.domain.PlayerGameCountsReader;
import com.cartographerapi.domain.PlayerGameCountsWriter;

/**
 * Refreshes the PlayerGameCounts for a given Gamertag.
 * 
 * @author GodlyPerfection
 * 
 */
public class PlayerGameCountsUpdater implements RequestHandler<Gamertag, PlayerGameCounts> {

	private PlayerGameCountsReader cacheReader;
	private PlayerGameCountsWriter cacheWriter;
	private PlayerGameCountsReader sourceReader;
	
	/**
	 * Checks the cache to see if PlayerGameCounts already exist. Then uses that
	 * (if exists) to update the current game counts for a Gamertag. Then saves
	 * the refresh in the cache.
	 * 
	 * @param gamertag The Gamertag given as input to the Lambda function.
	 * @param context The Lambda execution context.
	 * @return The newly refreshed PlayerGameCounts for a Gamertag.
	 */
    @Override
    public PlayerGameCounts handleRequest(Gamertag input, Context context) {
        context.getLogger().log("Input: " + input.getGamertag());

		PlayerGameCounts counts = cacheReader.getPlayerGameCountsByGamertag(input.getGamertag());
		
		if (counts == null) {
			counts = sourceReader.getPlayerGameCountsByGamertag(input.getGamertag());
		} else {
			counts = sourceReader.getPlayerGameCountsByPlayerGameCounts(counts);
		}

		cacheWriter.savePlayerGameCounts(counts);
		return counts;
    }
    
    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public PlayerGameCountsUpdater() {
    	this(new PlayerGameCountsDynamoReader(), new PlayerGameCountsDynamoWriter(), new PlayerGameCountsHaloApiReader());
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public PlayerGameCountsUpdater(PlayerGameCountsReader cacheReader, PlayerGameCountsWriter cacheWriter, PlayerGameCountsReader sourceReader) {
    	this.cacheReader = cacheReader;
    	this.cacheWriter = cacheWriter;
    	this.sourceReader = sourceReader;
    }
}