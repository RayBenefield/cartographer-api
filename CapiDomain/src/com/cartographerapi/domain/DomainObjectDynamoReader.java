package com.cartographerapi.domain;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.ScanResultPage;
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient; 

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

/**
 * Reader repository for domain Objects from a DynamoDB table.
 * 
 * @see DomainObjectReader
 * 
 * @author GodlyPerfection
 *
 */
public class DomainObjectDynamoReader implements DomainObjectReader {

	private AmazonDynamoDBClient client;
	private DynamoDBMapper mapper;
	private Map<String, AttributeValue> lastEvaluatedKey;

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Object> getAPageOfDomainObjects(SegmentScannerRequest request) {
		try {
			DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withSegment(request.getSegmentId())
				.withTotalSegments(request.getTotalSegments());
			
			if (request.getLastEvaluatedKey() != null) {
				scanExpression.withExclusiveStartKey(InternalUtils.fromSimpleMap(request.getLastEvaluatedKey()));
			}

			ScanResultPage page = mapper.scanPage(Class.forName(request.getDomainObject()), scanExpression);
			this.lastEvaluatedKey = page.getLastEvaluatedKey();
			return new ArrayList<Object>(page.getResults());
		} catch (ClassNotFoundException exception) {
			return new ArrayList<Object>();
		}
	}

	/**
	 * Get the last evaluated key from the last page read.
	 * 
	 * @return
	 */
	@Override
	public Map<String, AttributeValue> getLastEvaluatedKey() {
		return lastEvaluatedKey;
	}
	
    /**
     * The lazy IOC constructor.
     */
	public DomainObjectDynamoReader() {
        client = new AmazonDynamoDBClient();		                           
        client.setRegion(Region.getRegion(Regions.US_WEST_2));
        mapper = new DynamoDBMapper(client);
	}

}
