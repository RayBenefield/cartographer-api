package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;

import com.amazonaws.services.dynamodbv2.document.Item;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import com.cartographerapi.domain.CapiUtils;
import com.cartographerapi.domain.game.Game;
import com.cartographerapi.domain.game.GamesSnsWriter;
import com.cartographerapi.domain.game.GamesWriter;

/**
 * Publish any new games added to the database to a SNS topic.
 * 
 * @author GodlyPerfection
 * 
 */
public class GamesPublisher implements RequestHandler<DynamodbEvent, List<Game>> {

	private GamesWriter newWriter;
	
	/**
	 * For every new DynamoDB PlayerGame entry publish it to the appropriate SNS
	 * topic.
	 * 
	 * @param input The DynamoDB change event that triggered this.
	 * @param context The Lambda execution context.
	 * @return The newly added Games published to SNS.
	 */
    @Override
    public List<Game> handleRequest(DynamodbEvent input, Context context) {
		CapiUtils.logObject(context, input, "DynamdbEvent Input");
        List<Game> results = new ArrayList<Game>();
        
        Map<String, List<Map<String, Item>>> itemRecords = CapiUtils.sortRecordsFromDynamoEvent(input);
        
        // For each inserted record, add it to the results.
        for (Map<String, Item> insertedRecord : itemRecords.get("inserted")) {
			Game game = new Game(insertedRecord.get("new"));
			results.add(game);
        }
		newWriter.saveGames(results);

		CapiUtils.logObject(context, results.size(), "# of Games published");
        return results;
    }
    
    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public GamesPublisher() {
    	this(
			new GamesSnsWriter("snsCapiGamesNew")
		);
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public GamesPublisher(GamesWriter newWriter) {
    	this.newWriter = newWriter;
    }

}
