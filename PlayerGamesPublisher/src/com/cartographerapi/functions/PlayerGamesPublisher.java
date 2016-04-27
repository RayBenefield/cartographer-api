package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;

import com.cartographerapi.domain.playergames.PlayerGame;
import com.cartographerapi.domain.playergames.PlayerGamesSnsWriter;
import com.cartographerapi.domain.playergames.PlayerGamesWriter;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Publish all new PlayerGames to an SNS topic.
 * 
 * @author GodlyPerfection
 * 
 */
public class PlayerGamesPublisher implements RequestHandler<DynamodbEvent, List<PlayerGame>> {

	private PlayerGamesWriter newWriter;

	/**
	 * For every new PlayerGame added to DynamoDB publish that game to the
	 * respective SNS topic.
	 * 
	 * @param input The Dynamo event that triggered this.
	 * @param context The Lambda execution context.
	 * @return The published PlayerGames.
	 */
    @Override
    public List<PlayerGame> handleRequest(DynamodbEvent input, Context context) {
        context.getLogger().log("Input: " + input);
        List<PlayerGame> results = new ArrayList<PlayerGame>();

        // For each Dynamo Record, publish it to the SNS topic
		for (DynamodbStreamRecord record : input.getRecords()) {
			Map<String, AttributeValue> newData = record.getDynamodb().getNewImage();
			Map<String, AttributeValue> oldData = record.getDynamodb().getOldImage();
			if (newData == null) continue; // Ignore deletes for now;
			if (oldData != null) continue; // Ignore updates for now;

			Item newItem = Item.fromMap(InternalUtils.toSimpleMapValue(newData));
			PlayerGame game = new PlayerGame(newItem);
			results.add(game);
		}
		newWriter.savePlayerGames(results);

        return results;
    }
    
    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public PlayerGamesPublisher() {
    	this(
			new PlayerGamesSnsWriter("snsCapiPlayerGamesNew")
		);
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public PlayerGamesPublisher(PlayerGamesWriter newWriter) {
    	this.newWriter = newWriter;
    }

}
