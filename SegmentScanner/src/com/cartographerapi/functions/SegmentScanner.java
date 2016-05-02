package com.cartographerapi.functions;

import com.cartographerapi.domain.DomainObjectReader;
import com.cartographerapi.domain.DomainObjectDynamoReader;
import com.cartographerapi.domain.ObjectSqsWriter;
import com.cartographerapi.domain.ObjectSnsWriter;
import com.cartographerapi.domain.ObjectWriter;
import com.cartographerapi.domain.SegmentScannerRequest;

import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;

import java.io.IOException;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Responsible for scanning a single segment until near the end of timeout, then
 * spin up clones if need be.
 * 
 * @author GodlyPerfection
 * 
 */
public class SegmentScanner implements RequestHandler<SNSEvent, Boolean> {

	private DomainObjectReader sourceReader;
	private ObjectWriter continueWriter;
	private ObjectSqsWriter queueWriter;
	private ObjectMapper mapper;

	/**
	 * Grab pages of results from the segment starting from the lastEvaluatedKey
	 * if available, then send them to the queue, and if we run out of time
	 * clone this and do it some more.
	 * 
	 * @param input The SNS event that triggered this.
	 * @param context The Lambda execution context.
	 * @return 
	 */
    @Override
    @SuppressWarnings("unchecked")
    public Boolean handleRequest(SNSEvent input, Context context) {
        context.getLogger().log("Input: " + input);
        
        // Figure out what the request is
		SegmentScannerRequest request;
		try {
			context.getLogger().log(mapper.writeValueAsString(input));
			Map<String, Object> requestMap = mapper.readValue(input.getRecords().get(0).getSNS().getMessage(), HashMap.class);
			request = new SegmentScannerRequest(requestMap);
		} catch (IOException exception) {
			return true;
		}
		
		// Set the target queue from the request, and prepare the lastEvaluatedKey
		queueWriter.setQueueUrl(request.getQueueUrlKey());
		Map<String, AttributeValue> lastEvaluatedKey = null;

		// While we still have more than 30 seconds
		while (context.getRemainingTimeInMillis() > 30000) {
			// Get all of the results for the page and save them to the queue
			List<Object> pageResults = sourceReader.getAPageOfDomainObjects(request);
			queueWriter.saveObjects(pageResults);

			// If a LastEvaluatedKey doesn't exist then we are done
			lastEvaluatedKey = sourceReader.getLastEvaluatedKey();
			if (lastEvaluatedKey == null) {
				return true;
			}

			// Otherwise prepare for the next page
			context.getLogger().log("LastEvalKey: " + lastEvaluatedKey);
			request.setLastEvaluatedKey(InternalUtils.toSimpleMapValue(lastEvaluatedKey));
		}

		// We've run out of time (< 30 seconds) so we need to clone this and continue
		continueWriter.saveObject(request);
        return false;
    }
    
    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public SegmentScanner() {
    	this(
			new ObjectMapper(),
    		new DomainObjectDynamoReader(),
			new ObjectSnsWriter("snsSegmentScannerRequestContinue"),
			new ObjectSqsWriter()
		);
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public SegmentScanner(ObjectMapper mapper, DomainObjectReader sourceReader, ObjectWriter continueWriter, ObjectSqsWriter queueWriter) {
    	this.mapper = mapper;
    	this.sourceReader = sourceReader;
    	this.continueWriter = continueWriter;
    	this.queueWriter = queueWriter;
    }

}