package com.cartographerapi.functions;

import java.io.IOException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import com.cartographerapi.domain.ScheduledEvent;
import com.cartographerapi.domain.game.Game;
import com.cartographerapi.domain.ExecutionTests;
import org.junit.experimental.categories.Category;

/**
 * Test the execution of the GamesAdder.
 * 
 * @author GodlyPerfection
 * 
 */
@Category(ExecutionTests.class)
public class GamesAdderExecutionTest {

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
        ctx.setFunctionName("GamesAdder");
        return ctx;
    }

    /**
     * Execute the GamesAdder.
     */
    @Test
    public void executeGamesAdder() {
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
