package com.cartographerapi.functions;

import java.io.IOException;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import com.cartographerapi.domain.SegmentScannerRequest;

/**
 * Test the functionality of the SegmentScanner function from
 * end-to-end with no mocking.
 * 
 * @author GodlyPerfection
 * 
 */
public class SegmentScannerExecutionTest {

    private static SegmentScannerRequest input;

    /**
     * Setup the input for the function.
     */
    @BeforeClass
    public static void createInput() throws IOException {
        input = new SegmentScannerRequest(0, 2, "com.cartographerapi.domain.game.Game", "sqsTestScan");
    }

    /**
     * Create a test context.
     * 
     * @return
     */
    private Context createContext() {
        TestContext ctx = new TestContext();
        ctx.setFunctionName("SegmentScanner");
        return ctx;
    }

    /**
     * Execute the PlayerGameCountsGetter.
     */
    @Test
    public void executeSegmentScanner() {
        SegmentScanner handler = new SegmentScanner();
        Context ctx = createContext();

        List<Object> output = handler.handleRequest(input, ctx);

        // TODO: validate output here if needed.
        if (output != null) {
            System.out.println(output.toString());
        }
    }
}
