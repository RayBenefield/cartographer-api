package com.cartographerapi.functions;

import java.io.IOException;
import org.junit.BeforeClass;
import org.junit.Test;
import com.amazonaws.services.lambda.runtime.Context;
import com.cartographerapi.domain.Gamertag;
import com.cartographerapi.domain.PlayerGameCounts;
import com.cartographerapi.functions.PlayerGameCountsUpdater;

/**
 * Test the functionality of the PlayerGameCountsUpdater function from
 * end-to-end with no mocking.
 * 
 * @author GodlyPerfection
 * 
 */
public class PlayerGameCountsUpdaterFunctionalTest {

    private static Gamertag input;

    @BeforeClass
    public static void createInput() throws IOException {
        input = new Gamertag("MythicFritz");
    }

    private Context createContext() {
        return new TestContext();
    }

    @Test
    public void executePlayerGameCountsUpdater() {
        PlayerGameCountsUpdater updater = new PlayerGameCountsUpdater();
        Context ctx = createContext();

        PlayerGameCounts output = updater.handleRequest(input, ctx);

        // Output the results of the execution
        if (output != null) {
            System.out.println(output.getGamertag());
            System.out.println(output.getGamesCompleted());
            System.out.println(output.getTotalGames());
            System.out.println(output.getLastUpdated());
        }
    }
}
