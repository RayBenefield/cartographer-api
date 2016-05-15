package com.cartographerapi.functions;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import com.cartographerapi.domain.players.Player;
import com.cartographerapi.domain.playergamecounts.PlayerGameCounts;

import com.cartographerapi.functions.PlayerGameCountsUpdater;
import com.cartographerapi.domain.ExecutionTests;
import org.junit.experimental.categories.Category;

/**
 * Test the execution of the PlayerGameCountsUpdater.
 * 
 * @author GodlyPerfection
 * 
 */
@Category(ExecutionTests.class)
public class PlayerGameCountsUpdaterExecutionTest {

    private static Player input;

    /**
     * Setup the input for the function.
     */
    @BeforeClass
    public static void createInput() throws IOException {
        input = new Player("MythicFritz");
    }

    /**
     * Create a test context.
     * 
     * @return
     */
    private Context createContext() {
        TestContext ctx = new TestContext();
        ctx.setFunctionName("PlayerGameCountsUpdater");
        return new TestContext();
    }

    /**
     * Execute the GamesAdder.
     */
    @Test
    public void executePlayerGameCountsUpdater() {
        PlayerGameCountsUpdater updater = new PlayerGameCountsUpdater();
        Context ctx = createContext();

        PlayerGameCounts output = updater.handleRequest(input, ctx);

        if (output != null) {
            System.out.println(output.getGamertag());
            System.out.println(output.getGamesCompleted());
            System.out.println(output.getTotalGames());
            System.out.println(output.getLastUpdated());
        }
    }
}
