package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.cartographerapi.domain.CapiUtils;
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
public class DomainQueueFiller implements RequestHandler<DomainQueueFillRequest, Boolean> {

	/**
	 * Figure out the number of segments to run in parallel and fire off the
	 * stuff in charge or queuing the entries in the segments.
	 * 
	 * @param input The request that triggered this.
	 * @param context The Lambda execution context.
	 * @return 
	 */
    @Override
    public Boolean handleRequest(DomainQueueFillRequest input, Context context) {
		CapiUtils.logObject(context, input, "DomainQueueFillRequest Input");
        
		Integer totalSegments = 2;
		CapiUtils.logObject(context, totalSegments, "# of Segments for Scan");
		
		// For each segment, build the request and trigger the scanner
		for (Integer segmentId = 0; segmentId < totalSegments; segmentId++) {
			SegmentScannerRequest scannerInput = new SegmentScannerRequest(segmentId, totalSegments, input.getDomainObject(), input.getQueueUrlKey());
			ObjectSnsWriter continueWriter = new ObjectSnsWriter("snsSegmentScannerRequestContinue");
			continueWriter.saveObject(scannerInput);
		}

        return true;
    }

}
