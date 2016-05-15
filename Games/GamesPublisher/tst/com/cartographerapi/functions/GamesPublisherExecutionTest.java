package com.cartographerapi.functions;

import java.io.IOException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;

import com.cartographerapi.domain.game.Game;
import com.cartographerapi.domain.ExecutionTests;
import org.junit.experimental.categories.Category;

/**
 * Test the execution of the GamesPublisher.
 * 
 * @author GodlyPerfection
 * 
 */
@Category(ExecutionTests.class)
public class GamesPublisherExecutionTest {

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
        ctx.setFunctionName("GamesPublisher");
        return ctx;
    }

    /**
     * Execute the GamesAdder.
     */
    @Test
    public void executeGamesPublisher() {
        GamesPublisher handler = new GamesPublisher();
        Context ctx = createContext();

        List<Game> output = handler.handleRequest(input, ctx);

        if (output != null) {
        	for (Game game : output) {
				System.out.println(game.getMatchId());
				System.out.println(game.getGameData().asText());
        	}
        }
    }
}
