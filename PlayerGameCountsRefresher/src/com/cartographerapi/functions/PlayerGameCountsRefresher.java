package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.cartographerapi.domain.ScheduledEvent;
import com.cartographerapi.domain.PlayerGameCounts;
import com.cartographerapi.domain.PlayerGameCountsWriter;
import com.cartographerapi.domain.PlayerGameCountsCapiWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.DateTimeZone;
import org.joda.time.DateTime;

public class PlayerGameCountsRefresher implements RequestHandler<ScheduledEvent, List<PlayerGameCounts>> {

	private PlayerGameCountsWriter sourceWriter;

    @Override
    public List<PlayerGameCounts> handleRequest(ScheduledEvent input, Context context) {
        context.getLogger().log("Input: " + input);
        
		DateTimeFormatter dateFormatter = ISODateTimeFormat.dateTime().withZoneUTC();
		DateTime currentTime = new DateTime(input.getTime(), DateTimeZone.UTC);
		DateTime expireTime = currentTime.minusMinutes(5);
		String isoDate = dateFormatter.print(expireTime);
        List<PlayerGameCounts> result = new ArrayList<PlayerGameCounts>();
        result.add(new PlayerGameCounts(input.getTime()));
        result.add(new PlayerGameCounts(isoDate));
        
		AmazonDynamoDBClient client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(Regions.US_WEST_2));
        Map<String, AttributeValue> lastKeyEvaluated = null;
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>();
        expressionAttributeValues.put(":val", new AttributeValue().withS(isoDate)); 
        do {
            ScanRequest scanRequest = new ScanRequest()
                .withTableName("PlayerGameCounts")
                .withFilterExpression("LastUpdated < :val")
				.withExpressionAttributeValues(expressionAttributeValues)
                .withLimit(10)
                .withExclusiveStartKey(lastKeyEvaluated);

            ScanResult scanResult = client.scan(scanRequest);
            for (Map<String, AttributeValue> scanItem : scanResult.getItems()){
				Item item = Item.fromMap(InternalUtils.toSimpleMapValue(scanItem));
				PlayerGameCounts counts = sourceWriter.savePlayerGameCounts(new PlayerGameCounts(item.getString("Gamertag")));
				result.add(counts);
            }
            lastKeyEvaluated = scanResult.getLastEvaluatedKey();
        } while (lastKeyEvaluated != null);
		

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
