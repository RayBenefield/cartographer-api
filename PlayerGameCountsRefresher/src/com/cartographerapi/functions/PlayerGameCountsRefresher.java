package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.cartographerapi.domain.ScheduledEvent;
import com.cartographerapi.domain.PlayerGameCounts;
import com.cartographerapi.domain.PlayerGameCountsWriter;
import com.cartographerapi.domain.PlayerGameCountsUpdatedReader;
import com.cartographerapi.domain.PlayerGameCountsCapiWriter;
import com.cartographerapi.domain.PlayerGameCountsDynamoReader;
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
public class PlayerGameCountsRefresher implements RequestHandler<ScheduledEvent, List<PlayerGameCounts>> {

	private PlayerGameCountsWriter cacheWriter;
	private PlayerGameCountsUpdatedReader cacheReader;

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
