package com.cartographerapi.functions;

import java.io.IOException;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.cartographerapi.domain.playergames.PlayerGame;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class PlayerGamesAdderTest {

    private static SNSEvent input;

    @BeforeClass
    public static void createInput() throws IOException {
        input = TestUtils.parse("sns-event.json", SNSEvent.class);
    }

    private Context createContext() {
        TestContext ctx = new TestContext();

        ctx.setFunctionName("Your Function Name");

        return ctx;
    }

    @Test
    public void testPlayerGamesAdder() {
        PlayerGamesAdder handler = new PlayerGamesAdder();
        Context ctx = createContext();

        List<PlayerGame> output = handler.handleRequest(input, ctx);

        if (output != null) {
        	for (PlayerGame game : output) {
				System.out.println(game.getGamertag());
				System.out.println(game.getGameNumber());
				System.out.println(game.getGameData().toString());
        	}
        }
    }
}
