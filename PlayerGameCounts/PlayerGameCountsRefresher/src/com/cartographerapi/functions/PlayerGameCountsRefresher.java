package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.cartographerapi.domain.CapiUtils;
import com.cartographerapi.domain.ScheduledEvent;
import com.cartographerapi.domain.ObjectWriter;
import com.cartographerapi.domain.ObjectSqsWriter;
import com.cartographerapi.domain.players.Player;
import com.cartographerapi.domain.playergamecounts.PlayerGameCounts;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsDynamoReader;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsUpdatedReader;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsWriter;

import java.util.List;
import java.util.ArrayList;

import org.joda.time.DateTimeZone;
import org.joda.time.DateTime;

/**
 * Refreshes the PlayerGameCounts for all gamertags that have not been updated
 * in X amount of time.
 * 
 * @author GodlyPerfection
 * 
 */
// TODO check this against the rate limit.
public class PlayerGameCountsRefresher implements RequestHandler<ScheduledEvent, List<PlayerGameCounts>> {

	private ObjectWriter updatePlayerCountsWriter;
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
		CapiUtils.logObject(context, input, "ScheduledEvent Input");
        
		String expireTime = new DateTime(input.getTime(), DateTimeZone.UTC).minusHours(24).toString();
		CapiUtils.logObject(context, expireTime, "Expire Time");
        List<PlayerGameCounts> results = cacheReader.getAllPlayerGameCountsNotUpdatedSince(expireTime);
		CapiUtils.logObject(context, results, "PlayerGameCounts that have expired");
        
        List<Player> players = new ArrayList<Player>();
        for (PlayerGameCounts counts : results) {
        	players.add(new Player(counts.getGamertag()));
        }

        updatePlayerCountsWriter.saveObjects(new ArrayList<Object>(players));
        
		CapiUtils.logObject(context, results.size(), "# of updated PlayerGameCounts");
        return results;
    }
    
    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public PlayerGameCountsRefresher() {
    	this(new ObjectSqsWriter("sqsCapiPlayersForPlayerGameCounts"), new PlayerGameCountsDynamoReader());
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public PlayerGameCountsRefresher(ObjectWriter updatePlayerCountsWriter, PlayerGameCountsUpdatedReader cacheReader) {
    	this.updatePlayerCountsWriter = updatePlayerCountsWriter;
    	this.cacheReader = cacheReader;
    }

}
