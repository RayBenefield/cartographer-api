package com.cartographerapi.functions;

import java.io.IOException;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import com.cartographerapi.domain.ScheduledEvent;
import com.cartographerapi.domain.bareplayergames.BarePlayerGame;
import com.cartographerapi.domain.ExecutionTests;
import org.junit.experimental.categories.Category;

/**
 * Test the execution of the BarePlayerGamesAdder.
 *
 * @author GodlyPerfection
 *
 */
@Category(ExecutionTests.class)
public class BarePlayerGamesAdderExecutionTest {

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
        ctx.setFunctionName("BarePlayerGamesAdder");
        return ctx;
    }

    /**
     * Execute the BarePlayerGamesAdder.
     */
    @Test
    public void executeBarePlayerGamesAdder() {
        BarePlayerGamesAdder handler = new BarePlayerGamesAdder();
        Context ctx = createContext();

        List<BarePlayerGame> output = handler.handleRequest(input, ctx);

        if (output != null) {
			for (BarePlayerGame game : output) {
				System.out.println(game.getMatchId());
			}
        }
    }
}
