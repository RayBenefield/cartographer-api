package com.cartographerapi.functions;

import java.io.IOException;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import com.cartographerapi.domain.Gamertag;
import com.cartographerapi.domain.playergames.PlayerGame;

/**
 * Test the execution of the PlayerGamesGetter.
 * 
 * @author GodlyPerfection
 * 
 */
public class PlayerGamesGetterExecutionTest {

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
        ctx.setFunctionName("PlayerGamesGetter");
        return ctx;
    }

    /**
     * Execute the PlayerGamesGetter.
     */
    @Test
    public void executePlayerGamesGetter() {
        PlayerGamesGetter handler = new PlayerGamesGetter();
        Context ctx = createContext();

        List<PlayerGame> output = handler.handleRequest(input, ctx);

        if (output != null) {
        	for (PlayerGame game : output) {
				System.out.println(game.getGamertag());
				System.out.println(game.getGameNumber());
				System.out.println(game.getGameData().toString());
        	}
        }
    }
}
