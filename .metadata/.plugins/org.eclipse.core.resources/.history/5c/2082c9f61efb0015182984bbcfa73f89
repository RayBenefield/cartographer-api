package com.cartographerapi.customgames;

import java.io.IOException;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class CustomGameCountHandlerTest {

    private static Gamertag input;

    @BeforeClass
    public static void createInput() throws IOException {
        input = new Gamertag("Black+Picture");
    }

    private Context createContext() {
        TestContext ctx = new TestContext();

        // TODO: customize your context here if needed.
        ctx.setFunctionName("Your Function Name");

        return ctx;
    }

    @Test
    public void testCustomGameCountHandler() {
        CustomGameCountHandler handler = new CustomGameCountHandler();
        Context ctx = createContext();

        PlayerGameCounts output = handler.handleRequest(input, ctx);

        // TODO: validate output here if needed.
        if (output != null) {
            System.out.println(output.getGamertag());
            System.out.println(output.getGamesCompleted());
            System.out.println(output.getTotalGames());
        }
    }
}
