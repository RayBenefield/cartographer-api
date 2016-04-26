package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

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
        context.getLogger().log("Input: " + input);
        List<Game> results = new ArrayList<Game>();

        // For each record changed, if it is a new item then publish it
		for (DynamodbStreamRecord record : input.getRecords()) {
			Map<String, AttributeValue> newData = record.getDynamodb().getNewImage();
			Map<String, AttributeValue> oldData = record.getDynamodb().getOldImage();
			if (newData == null) continue; // Ignore deletes for now;
			if (oldData != null) continue; // Ignore updates for now;

			Item newItem = Item.fromMap(InternalUtils.toSimpleMapValue(newData));
			Game game = new Game(newItem);
			results.add(game);
		}
		newWriter.saveGames(results);

        return results;
    }
    
    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public GamesPublisher() {
    	this(
			new GamesSnsWriter("capiGamesNew")
		);
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public GamesPublisher(GamesWriter newWriter) {
    	this.newWriter = newWriter;
    }

}
