package com.cartographerapi.functions;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class PlayerGameCountPublisherTest {

    private static DynamodbEvent input;

    @BeforeClass
    public static void createInput() throws IOException {
        input = TestUtils.parse("dynamodb-update-event.json", DynamodbEvent.class);
    }

    private Context createContext() {
        TestContext ctx = new TestContext();

        ctx.setFunctionName("Your Function Name");

        return ctx;
    }

    @Test
    public void testPlayerGameCountPublisher() {
        PlayerGameCountsPublisher handler = new PlayerGameCountsPublisher();
        Context ctx = createContext();

        Boolean output = handler.handleRequest(input, ctx);

        if (output != null) {
            System.out.println(output.toString());
        }
    }
}
