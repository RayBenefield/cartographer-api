package com.cartographerapi.functions;

import java.io.IOException;

import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import com.cartographerapi.domain.players.Player;
import com.cartographerapi.domain.playergamecounts.PlayerGameCounts;

import com.cartographerapi.functions.PlayerGameCountsBulkGetter;
import com.cartographerapi.domain.ExecutionTests;
import org.junit.experimental.categories.Category;

/**
 * Test the functionality of the PlayerGameCountsBulkGetter function from
 * end-to-end with no mocking.
 *
 * @author GodlyPerfection
 *
 */
@Category(ExecutionTests.class)
public class PlayerGameCountsBulkGetterExecutionTest {

    private static List<Player> input;

    /**
     * Setup the input for the function.
     */
    @BeforeClass
    public static void createInput() throws IOException {
        input = Arrays.asList(new Player("Ray Benefield"), new Player("MythicFritz"));
    }

    /**
     * Create a test context.
     *
     * @return
     */
    private Context createContext() {
        TestContext ctx = new TestContext();
        ctx.setFunctionName("PlayerGameCountsBulkGetter");
        return new TestContext();
    }

    /**
     * Execute the PlayerGameCountsBulkGetter.
     */
    @Test
    public void executePlayerGameCountsUpdater() {
        PlayerGameCountsBulkGetter getter = new PlayerGameCountsBulkGetter();
        Context ctx = createContext();

        List<PlayerGameCounts> output = getter.handleRequest(input, ctx);

        if (output != null) {
            for (PlayerGameCounts counts : output) {
                System.out.println(counts.getGamertag());
                System.out.println(counts.getGamesCompleted());
                System.out.println(counts.getTotalGames());
            }
        }
    }
}
