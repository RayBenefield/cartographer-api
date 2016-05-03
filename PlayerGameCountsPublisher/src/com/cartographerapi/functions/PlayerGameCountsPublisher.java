package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;

import com.cartographerapi.domain.CapiUtils;
import com.cartographerapi.domain.playergamecounts.PlayerGameCounts;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsSnsWriter;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsWriter;

import com.amazonaws.services.dynamodbv2.document.Item;

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
		CapiUtils.logObject(context, input, "DynamdbEvent Input");
        List<PlayerGameCounts> results = new ArrayList<PlayerGameCounts>();
        
        Map<String, List<Map<String, Item>>> itemRecords = CapiUtils.sortRecordsFromDynamoEvent(input);

        // For each inserted record, publish to the new SNS topic.
        for (Map<String, Item> insertedRecord : itemRecords.get("inserted")) {
			PlayerGameCounts counts = new PlayerGameCounts(insertedRecord.get("new"));
			newWriter.savePlayerGameCounts(counts);
			results.add(counts);
        }

        // For each updated record, if it is an actual update then publish to the updated SNS topic.
        for (Map<String, Item> updatedRecord : itemRecords.get("inserted")) {
			PlayerGameCounts newCounts = new PlayerGameCounts(updatedRecord.get("new"));
			PlayerGameCounts oldCounts = new PlayerGameCounts(updatedRecord.get("old"));

			// Not really an update.
			if (newCounts.getTotalGames().equals(oldCounts.getTotalGames())) continue;

			// This is an update.
			updatedWriter.savePlayerGameCounts(newCounts);
			results.add(newCounts);
        }

		CapiUtils.logObject(context, results.size(), "# of PlayerGameCounts published");
        return results;
    }
    
    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public PlayerGameCountsPublisher() {
    	this(
			new PlayerGameCountsSnsWriter("snsCapiPlayerGameCountsNew"),
			new PlayerGameCountsSnsWriter("snsCapiPlayerGameCountsUpdated")
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
