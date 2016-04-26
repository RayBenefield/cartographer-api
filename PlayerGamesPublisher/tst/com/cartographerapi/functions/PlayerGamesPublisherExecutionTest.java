package com.cartographerapi.functions;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;

import com.cartographerapi.domain.playergames.PlayerGame;

import java.util.List;

/**
 * Test the execution of the PlayerGamesPublisher.
 * 
 * @author GodlyPerfection
 * 
 */
public class PlayerGamesPublisherExecutionTest {

    private static DynamodbEvent input;

    /**
     * Setup the input for the function.
     */
    @BeforeClass
    public static void createInput() throws IOException {
        input = TestUtils.parse("dynamodb-update-event.json", DynamodbEvent.class);
    }

    /**
     * Create a test context.
     * 
     * @return
     */
    private Context createContext() {
        TestContext ctx = new TestContext();
        ctx.setFunctionName("Your Function Name");
        return ctx;
    }

    /**
     * Execute the PlayerGamesPublisher.
     */
    @Test
    public void executePlayerGamesPublisher() {
        PlayerGamesPublisher handler = new PlayerGamesPublisher();
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
