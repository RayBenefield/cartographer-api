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
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SegmentScanner implements RequestHandler<SNSEvent, List<Object>> {

	private DomainObjectReader sourceReader;
	private ObjectWriter continueWriter;
	private ObjectSqsWriter queueWriter;
	private ObjectMapper mapper;

    @Override
    @SuppressWarnings("unchecked")
    public List<Object> handleRequest(SNSEvent input, Context context) {
        context.getLogger().log("Input: " + input);
        List<Object> results = new ArrayList<Object>();
        
        // Figure out what the request is
		SegmentScannerRequest request;
		try {
			context.getLogger().log(mapper.writeValueAsString(input));
			Map<String, Object> requestMap = mapper.readValue(input.getRecords().get(0).getSNS().getMessage(), HashMap.class);
			request = new SegmentScannerRequest(requestMap);
		} catch (IOException exception) {
			return new ArrayList<Object>();
		}
		
		// Set the target queue from the request
		queueWriter.setQueueUrl(request.getQueueUrlKey());

		// Get all of the results for the page and save them to the queue
		List<Object> pageResults = sourceReader.getAPageOfDomainObjects(request);
		results.addAll(pageResults);
		queueWriter.saveObjects(pageResults);

		// If a LastEvaluatedKey exists we need to continue.
		Map<String, AttributeValue> lastEvaluatedKey = sourceReader.getLastEvaluatedKey();
		if (lastEvaluatedKey != null) {
			context.getLogger().log("LastEvalKey: " + lastEvaluatedKey);
			request.setLastEvaluatedKey(InternalUtils.toSimpleMapValue(lastEvaluatedKey));

			continueWriter.saveObject(request);
		}

        return results;
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
