package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;

import java.io.IOException;
import java.util.List;

import com.cartographerapi.domain.ExecutionTests;
import com.cartographerapi.domain.ScheduledEvent;
import com.cartographerapi.domain.playergames.PlayerGame;

import org.junit.experimental.categories.Category;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test the execution of the PlayerGamesAdder.
 * 
 * @author GodlyPerfection
 * 
 */
@Category(ExecutionTests.class)
public class PlayerGamesAdderExecutionTest {

    private static ScheduledEvent input;

    /**
     * Setup the input for the function.
     */
    @BeforeClass
    public static void createInput() throws IOException {
        input = new ScheduledEvent();
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
