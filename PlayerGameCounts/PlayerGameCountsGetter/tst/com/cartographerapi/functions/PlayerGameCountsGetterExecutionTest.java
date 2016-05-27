package com.cartographerapi.functions;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import com.cartographerapi.domain.players.Player;
import com.cartographerapi.domain.playergamecounts.PlayerGameCounts;

import com.cartographerapi.functions.PlayerGameCountsGetter;
import com.cartographerapi.domain.ExecutionTests;
import org.junit.experimental.categories.Category;

/**
 * Test the functionality of the PlayerGameCountsGetter function from
 * end-to-end with no mocking.
 * 
 * @author GodlyPerfection
 * 
 */
@Category(ExecutionTests.class)
public class PlayerGameCountsGetterExecutionTest {

    private static Player input;

    /**
     * Setup the input for the function.
     */
    @BeforeClass
    public static void createInput() throws IOException {
        input = new Player("Ray Benefield");
    }

    /**
     * Create a test context.
     * 
     * @return
     */
    private Context createContext() {
        TestContext ctx = new TestContext();
        ctx.setFunctionName("PlayerGameCountsGetter");
        return new TestContext();
    }

    /**
     * Execute the PlayerGameCountsGetter.
     */
    @Test
    public void executePlayerGameCountsUpdater() {
        PlayerGameCountsGetter getter = new PlayerGameCountsGetter();
        Context ctx = createContext();

        PlayerGameCounts output = getter.handleRequest(input, ctx);

        if (output != null) {
            System.out.println(output.getGamertag());
            System.out.println(output.getGamesCompleted());
            System.out.println(output.getTotalGames());
        }
    }
}
