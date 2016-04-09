package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.cartographerapi.domain.ScheduledEvent;
import com.cartographerapi.domain.PlayerGameCounts;
import com.cartographerapi.domain.PlayerGameCountsWriter;
import com.cartographerapi.domain.PlayerGameCountsCapiWriter;
import java.util.List;
import java.util.ArrayList;

public class PlayerGameCountsRefresher implements RequestHandler<ScheduledEvent, List<PlayerGameCounts>> {

	private PlayerGameCountsWriter sourceWriter;

    @Override
    public List<PlayerGameCounts> handleRequest(ScheduledEvent input, Context context) {
        context.getLogger().log("Input: " + input);
        
        List<PlayerGameCounts> result = new ArrayList<PlayerGameCounts>();
        result.add(new PlayerGameCounts("GodlyPerfection"));
		
		PlayerGameCounts counts = sourceWriter.savePlayerGameCounts(new PlayerGameCounts("GodlyPerfection"));
		result.add(counts);

        return result;
    }
    
    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public PlayerGameCountsRefresher() {
    	this(new PlayerGameCountsCapiWriter());
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public PlayerGameCountsRefresher(PlayerGameCountsWriter sourceWriter) {
    	this.sourceWriter = sourceWriter;
    }

}
