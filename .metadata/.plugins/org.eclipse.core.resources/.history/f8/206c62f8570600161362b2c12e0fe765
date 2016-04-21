package com.cartographerapi.functions;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.cartographerapi.domain.Game;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class GamesAdderTest {

    private static SNSEvent input;

    @BeforeClass
    public static void createInput() throws IOException {
        input = TestUtils.parse("sns-event.json", SNSEvent.class);
    }

    private Context createContext() {
        TestContext ctx = new TestContext();

        // TODO: customize your context here if needed.
        ctx.setFunctionName("Your Function Name");

        return ctx;
    }

    @Test
    public void testGamesAdder() {
        GamesAdder handler = new GamesAdder();
        Context ctx = createContext();

        Game output = handler.handleRequest(input, ctx);

        // TODO: validate output here if needed.
        if (output != null) {
            System.out.println(output.toString());
        }
    }
}
