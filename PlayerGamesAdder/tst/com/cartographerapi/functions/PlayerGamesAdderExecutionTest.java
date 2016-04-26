package com.cartographerapi.functions;

import java.io.IOException;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.cartographerapi.domain.playergames.PlayerGame;

/**
 * Test the execution of the PlayerGamesAdder.
 * 
 * @author GodlyPerfection
 * 
 */
public class PlayerGamesAdderExecutionTest {

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
        ctx.setFunctionName("PlayerGamesAdder");
        return ctx;
    }

    /**
     * Execute the PlayerGamesAdder.
     */
    @Test
    public void executePlayerGamesAdder() {
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
