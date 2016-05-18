package com.cartographerapi.functions;

import com.cartographerapi.domain.DomainObjectReader;
import com.cartographerapi.domain.CapiUtils;
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

import java.util.Map;
import java.util.List;

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
    public Boolean handleRequest(SNSEvent input, Context context) {
        CapiUtils.logObject(context, input, "SNSEvent Input");
        
        // Figure out what the request is
        Map<String, Object> requestMap = CapiUtils.getObjectFromSnsEvent(input);
        SegmentScannerRequest request = new SegmentScannerRequest(requestMap);
        CapiUtils.logObject(context, request, "SegmentScannerRequest");
        
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
            CapiUtils.logObject(context, lastEvaluatedKey, "LastEvaluatedKey");
            request.setLastEvaluatedKey(InternalUtils.toSimpleMapValue(lastEvaluatedKey));
        }

        // We've run out of time (< 30 seconds) so we need to clone this and continue
        CapiUtils.logObject(context, request, "Continuing with a new SegmentScannerRequest");
        continueWriter.saveObject(request);
        return false;
    }
    
    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public SegmentScanner() {
        this(
            new DomainObjectDynamoReader(),
            new ObjectSnsWriter("snsSegmentScannerRequestContinue"),
            new ObjectSqsWriter()
        );
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public SegmentScanner(DomainObjectReader sourceReader, ObjectWriter continueWriter, ObjectSqsWriter queueWriter) {
        this.sourceReader = sourceReader;
        this.continueWriter = continueWriter;
        this.queueWriter = queueWriter;
    }

}
