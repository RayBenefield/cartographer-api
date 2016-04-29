package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.List;
import java.util.ArrayList;

import com.cartographerapi.domain.DomainQueueFillRequest;
import com.cartographerapi.domain.ObjectSnsWriter;
import com.cartographerapi.domain.SegmentScannerRequest;

public class DomainQueueFiller implements RequestHandler<DomainQueueFillRequest, List<Object>> {

    @Override
    public List<Object> handleRequest(DomainQueueFillRequest input, Context context) {
        context.getLogger().log("Input: " + input);
        List<Object> results = new ArrayList<Object>();
        
		Integer totalSegments = 2;
		for (Integer segment = 0; segment < totalSegments; segment++) {
			SegmentScannerRequest scannerInput = new SegmentScannerRequest(segment, totalSegments, input.getDomainObject(), input.getQueueUrlKey());
			ObjectSnsWriter continueWriter = new ObjectSnsWriter("snsSegmentScannerRequestContinue");
			continueWriter.saveObject(scannerInput);
		}

        return results;
    }

}
