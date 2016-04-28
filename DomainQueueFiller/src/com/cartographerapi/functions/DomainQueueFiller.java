package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.List;
import java.util.ArrayList;

import com.cartographerapi.domain.DomainQueueFillRequest;
import com.cartographerapi.domain.ObjectSqsWriter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.ScanResultPage;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient; 

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

public class DomainQueueFiller implements RequestHandler<DomainQueueFillRequest, List<Object>> {

    @Override
    public List<Object> handleRequest(DomainQueueFillRequest input, Context context) {
        context.getLogger().log("Input: " + input);

        List<Object> results = new ArrayList<Object>();
		try {
			AmazonDynamoDBClient client = new AmazonDynamoDBClient();
			client.setRegion(Region.getRegion(Regions.US_WEST_2));
			DynamoDBMapper dbMapper = new DynamoDBMapper(client);
			
			DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withSegment(1).withTotalSegments(2);
			ScanResultPage page = dbMapper.scanPage(Class.forName(input.getDomainObject()), scanExpression);
//			results = new ArrayList<Object>(page.getResults());

			results.add(page.getLastEvaluatedKey());
			scanExpression = new DynamoDBScanExpression().withSegment(1).withTotalSegments(2).withExclusiveStartKey(page.getLastEvaluatedKey());
			page = dbMapper.scanPage(Class.forName(input.getDomainObject()), scanExpression);

//			results.clear();
//			results.add(page.getLastEvaluatedKey());
			
//			ObjectSqsWriter writer = new ObjectSqsWriter(input.getQueueUrlKey());
//			writer.saveObjects(results);
		} catch (ClassNotFoundException exception) {
			System.out.println("oops");
		}

        return results;
    }

}
