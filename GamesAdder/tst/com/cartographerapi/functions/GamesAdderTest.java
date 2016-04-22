package com.cartographerapi.functions;

import java.io.IOException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import com.cartographerapi.domain.Game;
import com.cartographerapi.domain.ScheduledEvent;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class GamesAdderTest {

    private static ScheduledEvent input;

    @BeforeClass
    public static void createInput() throws IOException {
//        input = TestUtils.parse("sns-event.json", SNSEvent.class);
        input = new ScheduledEvent();
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

        List<Game> output = handler.handleRequest(input, ctx);

        if (output != null) {
			for (Game game : output) {
				System.out.println(game.getMatchId());
				System.out.println(game.getGameData().toString());
			}
        }
    }
}
