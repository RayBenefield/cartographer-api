package com.cartographerapi.functions;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;
import com.amazonaws.regions.Region;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;

import java.util.Map;

public class PlayerGameCountsCacher implements RequestHandler<DynamodbEvent, Boolean> {

	private AmazonDynamoDBClient client;
	private DynamoDBMapper dbMapper;

    @Override
    public Boolean handleRequest(DynamodbEvent input, Context context) {
        context.getLogger().log("Input: " + input);

		client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(Regions.US_WEST_2));
		dbMapper = new DynamoDBMapper(client);
		
		Integer i = 50;
		for (DynamodbStreamRecord record : input.getRecords()) {
			Map<String, AttributeValue> newData = record.getDynamodb().getNewImage();
			if (newData == null) continue; // Ignore deletes.
			Item item = Item.fromMap(InternalUtils.toSimpleMapValue(newData));

			PlayerGameCountCacheEntry entry = new PlayerGameCountCacheEntry("Today", i, item.getString("Gamertag"));
			dbMapper.save(entry);
			
			i++;
		}
		
        return true;
    }

}
