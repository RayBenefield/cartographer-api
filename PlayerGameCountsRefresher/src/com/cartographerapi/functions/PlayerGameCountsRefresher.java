package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.cartographerapi.domain.ScheduledEvent;
import com.cartographerapi.domain.playergamecounts.PlayerGameCounts;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsCapiWriter;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsDynamoReader;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsUpdatedReader;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsWriter;

import java.util.List;

import org.joda.time.DateTimeZone;
import org.joda.time.DateTime;

/**
 * Refreshes the PlayerGameCounts for all gamertags that have not been updated
 * in X amount of time.
 * 
 * @author GodlyPerfection
 * 
 */
// TODO Move into using SNS publishing to trigger a refresh.
// TODO check this against the rate limit.
public class PlayerGameCountsRefresher implements RequestHandler<ScheduledEvent, List<PlayerGameCounts>> {

	private PlayerGameCountsWriter cacheWriter;
	private PlayerGameCountsUpdatedReader cacheReader;

	/**
	 * Find all cached PlayerGameCounts that are older than 24 hours and send
	 * them in to get refreshed.
	 * 
	 * @param input The Cloudwatch scheduled event that triggered this.
	 * @param context The Lambda execution context.
	 * @return The updated PlayerGameCounts.
	 */
    @Override
    public List<PlayerGameCounts> handleRequest(ScheduledEvent input, Context context) {
        context.getLogger().log("Input: " + input);
        
		String expireTime = new DateTime(input.getTime(), DateTimeZone.UTC).minusHours(24).toString();
        List<PlayerGameCounts> result = cacheReader.getAllPlayerGameCountsNotUpdatedSince(expireTime);
        
        for (PlayerGameCounts counts : result) {
        	cacheWriter.savePlayerGameCounts(counts);
        }
        
        return result;
    }
    
    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public PlayerGameCountsRefresher() {
    	this(new PlayerGameCountsCapiWriter(), new PlayerGameCountsDynamoReader());
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public PlayerGameCountsRefresher(PlayerGameCountsWriter cacheWriter, PlayerGameCountsUpdatedReader cacheReader) {
    	this.cacheWriter = cacheWriter;
    	this.cacheReader = cacheReader;
    }

}
