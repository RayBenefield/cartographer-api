package com.cartographerapi.functions;

import java.io.IOException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.cartographerapi.domain.playergamecounts.PlayerGameCounts;
import com.cartographerapi.domain.ExecutionTests;
import org.junit.experimental.categories.Category;

/**
 * Test the execution of the PlayerGameCountsPublisher.
 * 
 * @author GodlyPerfection
 * 
 */
@Category(ExecutionTests.class)
public class PlayerGameCountsPublisherExecutionTest {

    private static DynamodbEvent input;

    /**
     * Setup the input for the function.
     */
    @BeforeClass
    public static void createInput() throws IOException {
    	//TODO Get an actual DynamodbEvent parsed since TestUtils has a broken parse function.
        input = TestUtils.parse("dynamodb-update-event.json", DynamodbEvent.class);
    }

    /**
     * Create a test context.
     * 
     * @return
     */
    private Context createContext() {
        TestContext ctx = new TestContext();
        ctx.setFunctionName("PlayerGameCountsPublisher");
        return ctx;
    }

    /**
     * Execute the PlayerGameCountsPublisher.
     */
    @Test
    public void executePlayerGameCountPublisher() {
        PlayerGameCountsPublisher handler = new PlayerGameCountsPublisher();
        Context ctx = createContext();

        List<PlayerGameCounts> output = handler.handleRequest(input, ctx);

        if (output != null) {
        	for (PlayerGameCounts counts : output) {
				System.out.println(counts.getGamertag().toString());
				System.out.println(counts.getTotalGames().toString());
				System.out.println(counts.getGamesCompleted().toString());
				System.out.println(counts.getLastUpdated().toString());
        	}
        }
    }
}
