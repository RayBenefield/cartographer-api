package com.cartographerapi.functions;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.cartographerapi.domain.ExecutionTests;
import org.junit.experimental.categories.Category;

/**
 * Test the functionality of the SegmentScanner function from
 * end-to-end with no mocking.
 * 
 * @author GodlyPerfection
 * 
 */
@Category(ExecutionTests.class)
public class SegmentScannerExecutionTest {

    private static SNSEvent input;

    /**
     * Setup the input for the function.
     */
    @BeforeClass
    public static void createInput() throws IOException {
        input = TestUtils.parse("sns-event.json", SNSEvent.class);
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

        Boolean output = handler.handleRequest(input, ctx);

        if (output != null) {
            System.out.println(output.toString());
        }
    }
}
