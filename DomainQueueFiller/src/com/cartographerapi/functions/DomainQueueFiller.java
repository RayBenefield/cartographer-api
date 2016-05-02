package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.List;
import java.util.ArrayList;

import com.cartographerapi.domain.DomainQueueFillRequest;
import com.cartographerapi.domain.ObjectSnsWriter;
import com.cartographerapi.domain.SegmentScannerRequest;

/**
 * Responsible for scanning a single segment until near the end of timeout, then
 * spin up clones if need be.
 * 
 * @author GodlyPerfection
 * 
 */
public class DomainQueueFiller implements RequestHandler<DomainQueueFillRequest, List<Object>> {

	/**
	 * Figure out the number of segments to run in parallel and fire off the
	 * stuff in charge or queuing the entries in the segments.
	 * 
	 * @param input The request that triggered this.
	 * @param context The Lambda execution context.
	 * @return 
	 */
    @Override
    public List<Object> handleRequest(DomainQueueFillRequest input, Context context) {
        context.getLogger().log("Input: " + input);
        List<Object> results = new ArrayList<Object>();
        
		Integer totalSegments = 2;
		
		// For each segment, build the request and trigger the scanner
		for (Integer segmentId = 0; segmentId < totalSegments; segmentId++) {
			SegmentScannerRequest scannerInput = new SegmentScannerRequest(segmentId, totalSegments, input.getDomainObject(), input.getQueueUrlKey());
			ObjectSnsWriter continueWriter = new ObjectSnsWriter("snsSegmentScannerRequestContinue");
			continueWriter.saveObject(scannerInput);
		}

        return results;
    }

}
