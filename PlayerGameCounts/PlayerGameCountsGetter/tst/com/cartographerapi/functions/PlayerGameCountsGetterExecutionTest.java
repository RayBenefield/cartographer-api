package com.cartographerapi.functions;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import com.cartographerapi.domain.Gamertag;
import com.cartographerapi.domain.playergamecounts.PlayerGameCounts;

import com.cartographerapi.functions.PlayerGameCountsGetter;

/**
 * Test the functionality of the PlayerGameCountsGetter function from
 * end-to-end with no mocking.
 * 
 * @author GodlyPerfection
 * 
 */
public class PlayerGameCountsGetterExecutionTest {

    private static Gamertag input;

    /**
     * Setup the input for the function.
     */
    @BeforeClass
    public static void createInput() throws IOException {
        input = new Gamertag("GodlyPerfection");
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