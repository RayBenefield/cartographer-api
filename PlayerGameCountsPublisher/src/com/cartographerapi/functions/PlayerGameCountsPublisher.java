package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;
import java.util.Map;
import com.cartographerapi.domain.PlayerGameCounts;
import com.cartographerapi.domain.PlayerGameCountsSnsWriter;
import com.cartographerapi.domain.PlayerGameCountsWriter;

public class PlayerGameCountsPublisher implements RequestHandler<DynamodbEvent, Boolean> {

	private PlayerGameCountsWriter newWriter;
	private PlayerGameCountsWriter updatedWriter;

    @Override
    public Boolean handleRequest(DynamodbEvent input, Context context) {
        context.getLogger().log("Input: " + input);

		for (DynamodbStreamRecord record : input.getRecords()) {
			Map<String, AttributeValue> newData = record.getDynamodb().getNewImage();
			if (newData == null) continue; // Ignore deletes for now;

			Item newItem = Item.fromMap(InternalUtils.toSimpleMapValue(newData));
			PlayerGameCounts counts = new PlayerGameCounts(newItem);
			Map<String, AttributeValue> oldData = record.getDynamodb().getOldImage();

			// This is new.
			if (oldData == null) {
				newWriter.savePlayerGameCounts(counts);
			}

			Item oldItem = Item.fromMap(InternalUtils.toSimpleMapValue(oldData));
			PlayerGameCounts oldCounts = new PlayerGameCounts(oldItem);

			// Not really an update.
			if (counts.getTotalGames().equals(oldCounts.getTotalGames())) continue;

			// This is an update.
			updatedWriter.savePlayerGameCounts(counts);
		}
		
        return true;
    }
    
    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public PlayerGameCountsPublisher() {
    	this(
			new PlayerGameCountsSnsWriter("arn:aws:sns:us-west-2:789201490085:capi-playergamecounts-new"),
			new PlayerGameCountsSnsWriter("arn:aws:sns:us-west-2:789201490085:capi-playergamecounts-updated")
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