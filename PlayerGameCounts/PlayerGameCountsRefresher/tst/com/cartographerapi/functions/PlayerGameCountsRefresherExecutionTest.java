package com.cartographerapi.functions;

import java.util.List;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cartographerapi.domain.ScheduledEvent;
import com.cartographerapi.domain.playergamecounts.PlayerGameCounts;
import com.amazonaws.services.lambda.runtime.Context;
import com.cartographerapi.domain.ExecutionTests;
import org.junit.experimental.categories.Category;

/**
 * Test the execution of the PlayerGameCountsRefresher.
 * 
 * @author GodlyPerfection
 * 
 */
@Category(ExecutionTests.class)
public class PlayerGameCountsRefresherExecutionTest {

    private static ScheduledEvent input;

    /**
     * Setup the input for the function.
     */
    @BeforeClass
    public static void createInput() throws IOException {
    	// TODO Change this to handle based on the current date.
        input = new ScheduledEvent("2016-04-11T00:00:00.000Z");
    }

    /**
     * Create a test context.
     * 
     * @return
     */
    private Context createContext() {
        TestContext ctx = new TestContext();
        ctx.setFunctionName("PlayerGameCountsRefresher");
        return ctx;
    }

    /**
     * Execute the PlayerGameCountsRefresher.
     */
    @Test
    public void executePlayerGameCountsRefresher() {
        PlayerGameCountsRefresher handler = new PlayerGameCountsRefresher();
        Context ctx = createContext();

        List<PlayerGameCounts> output = handler.handleRequest(input, ctx);

		if (output != null) {
			for (PlayerGameCounts counts : output) {
				System.out.println(counts.getGamertag());
			}
		}
    }
}
