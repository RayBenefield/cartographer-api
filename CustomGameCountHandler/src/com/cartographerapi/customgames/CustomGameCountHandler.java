package com.cartographerapi.customgames;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class CustomGameCountHandler implements RequestHandler<Gamertag, PlayerGameCounts> {

	// Constructor Dependencies
	private PlayerGameCountsReader cacheReader;
	private PlayerGameCountsWriter cacheWriter;
	private PlayerGameCountsReader sourceReader;
	
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
    
    public CustomGameCountHandler() {
    	this(new PlayerGameCountsDynamoReader(), new PlayerGameCountsDynamoWriter(), new PlayerGameCountsHaloApiReader());
    }

    public CustomGameCountHandler(PlayerGameCountsReader cacheReader, PlayerGameCountsWriter cacheWriter, PlayerGameCountsReader sourceReader) {
    	this.cacheReader = cacheReader;
    	this.cacheWriter = cacheWriter;
    	this.sourceReader = sourceReader;
    }
}