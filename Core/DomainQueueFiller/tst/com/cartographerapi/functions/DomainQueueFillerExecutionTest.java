package com.cartographerapi.functions;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import com.cartographerapi.domain.DomainQueueFillRequest;
import com.cartographerapi.domain.ExecutionTests;
import org.junit.experimental.categories.Category;

/**
 * Test the functionality of the DomainQueueFiller function from
 * end-to-end with no mocking.
 * 
 * @author GodlyPerfection
 * 
 */
@Category(ExecutionTests.class)
public class DomainQueueFillerExecutionTest {

    private static DomainQueueFillRequest input;

    /**
     * Setup the input for the function.
     */
    @BeforeClass
    public static void createInput() throws IOException {
        input = new DomainQueueFillRequest("com.cartographerapi.domain.game.Game", "QueueUrlGamesForMapGames");
    }

    /**
     * Create a test context.
     * 
     * @return
     */
    private Context createContext() {
        TestContext ctx = new TestContext();
        ctx.setFunctionName("DomainQueueFiller");
        return ctx;
    }

    /**
     * Execute the PlayerGameCountsGetter.
     */
    @Test
    public void executeDomainQueueFiller() {
        DomainQueueFiller handler = new DomainQueueFiller();
        Context ctx = createContext();

        Boolean output = handler.handleRequest(input, ctx);

        if (output != null) {
			System.out.println(output.toString());
        }
    }
}
