package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;

import com.cartographerapi.domain.playergamecounts.PlayerGameCounts;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsSnsWriter;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsWriter;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Publishes updates and new PlayerGameCounts to respective SNS topics.
 * 
 * @author GodlyPerfection
 * 
 */
public class PlayerGameCountsPublisher implements RequestHandler<DynamodbEvent, List<PlayerGameCounts>> {

	private PlayerGameCountsWriter newWriter;
	private PlayerGameCountsWriter updatedWriter;

	/**
	 * Any new or updated DynamoDB entries are published to the respective SNS
	 * topics.
	 * 
	 * @param input DyanmoDB event that triggered this.
	 * @param context The Lambda execution context.
	 * @return The newly added Games.
	 */
    @Override
    public List<PlayerGameCounts> handleRequest(DynamodbEvent input, Context context) {
        context.getLogger().log("Input: " + input);
        List<PlayerGameCounts> results = new ArrayList<PlayerGameCounts>();

		for (DynamodbStreamRecord record : input.getRecords()) {
			Map<String, AttributeValue> newData = record.getDynamodb().getNewImage();
			if (newData == null) continue; // Ignore deletes for now;

			Item newItem = Item.fromMap(InternalUtils.toSimpleMapValue(newData));
			PlayerGameCounts counts = new PlayerGameCounts(newItem);
			Map<String, AttributeValue> oldData = record.getDynamodb().getOldImage();

			// This is new.
			if (oldData == null) {
				newWriter.savePlayerGameCounts(counts);
				results.add(counts);
				continue;
			}

			Item oldItem = Item.fromMap(InternalUtils.toSimpleMapValue(oldData));
			PlayerGameCounts oldCounts = new PlayerGameCounts(oldItem);

			// Not really an update.
			if (counts.getTotalGames().equals(oldCounts.getTotalGames())) continue;

			// This is an update.
			updatedWriter.savePlayerGameCounts(counts);
			results.add(counts);
		}
		
        return results;
    }
    
    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public PlayerGameCountsPublisher() {
    	this(
			new PlayerGameCountsSnsWriter("capiPlayerGameCountsNew"),
			new PlayerGameCountsSnsWriter("capiPlayerGameCountsUpdated")
		);
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public PlayerGameCountsPublisher(PlayerGameCountsWriter newWriter, PlayerGameCountsWriter updatedWriter) {
    	this.newWriter = newWriter;
    	this.updatedWriter = updatedWriter;
    }

}
