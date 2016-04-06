package com.cartographerapi.customgames;

import java.io.IOException;
import org.junit.BeforeClass;
import org.junit.Test;
import com.amazonaws.services.lambda.runtime.Context;

/**
 * Test the functionality of the PlayerGameCountsUpdater function from
 * end-to-end with no mocking.
 * 
 * @author GodlyPerfection
 * 
 */
public class PlayerGameCountsUpdaterExecutionTest {

    private static Gamertag input;

    @BeforeClass
    public static void createInput() throws IOException {
        input = new Gamertag("MythicFritz");
    }

    private Context createContext() {
        return new TestContext();
    }

    @Test
    public void testCustomGameCountHandler() {
        PlayerGameCountsUpdater updater = new PlayerGameCountsUpdater();
        Context ctx = createContext();

        PlayerGameCounts output = updater.handleRequest(input, ctx);

        // Output the results of the execution
        if (output != null) {
            System.out.println(output.getGamertag());
            System.out.println(output.getGamesCompleted());
            System.out.println(output.getTotalGames());
        }
    }
}