package com.cartographerapi.functions;

import java.io.IOException;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import com.cartographerapi.domain.ScheduledEvent;
import com.cartographerapi.domain.baregames.BareGame;
import com.cartographerapi.domain.ExecutionTests;
import org.junit.experimental.categories.Category;

/**
 * Test the execution of the BareGamesAdder.
 *
 * @author GodlyPerfection
 *
 */
@Category(ExecutionTests.class)
public class BareGamesAdderExecutionTest {

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
        ctx.setFunctionName("BareGamesAdder");
        return ctx;
    }

    /**
     * Execute the BareGamesAdder.
     */
    @Test
    public void executeBareGamesAdder() {
        BareGamesAdder handler = new BareGamesAdder();
        Context ctx = createContext();

        List<BareGame> output = handler.handleRequest(input, ctx);

        if (output != null) {
			for (BareGame game : output) {
				System.out.println(game.getMatchId());
			}
        }
    }
}
