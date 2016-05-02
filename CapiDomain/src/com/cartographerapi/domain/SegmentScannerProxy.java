package com.cartographerapi.domain;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.amazonaws.services.lambda.model.LogType;

public interface SegmentScannerProxy {
    @LambdaFunction(functionName="SegmentScanner", logType=LogType.Tail)
    boolean scanSegment(SegmentScannerRequest input);
}