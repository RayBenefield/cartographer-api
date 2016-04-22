package com.cartographerapi.functions;

import java.io.IOException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.cartographerapi.domain.Game;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class GamesPublisherTest {

    private static DynamodbEvent input;

    @BeforeClass
    public static void createInput() throws IOException {
        input = TestUtils.parse("dynamodb-update-event.json", DynamodbEvent.class);
    }

    private Context createContext() {
        TestContext ctx = new TestContext();

        // TODO: customize your context here if needed.
        ctx.setFunctionName("Your Function Name");

        return ctx;
    }

    @Test
    public void testGamesPublisher() {
        GamesPublisher handler = new GamesPublisher();
        Context ctx = createContext();

        List<Game> output = handler.handleRequest(input, ctx);

        // TODO: validate output here if needed.
        if (output != null) {
        	for (Game game : output) {
				System.out.println(game.getMatchId());
				System.out.println(game.getGameData().asText());
        	}
        }
    }
}
