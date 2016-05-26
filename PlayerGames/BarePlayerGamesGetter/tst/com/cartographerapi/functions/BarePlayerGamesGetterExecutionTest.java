package com.cartographerapi.functions;

import java.io.IOException;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import com.cartographerapi.domain.players.Player;
import com.cartographerapi.domain.playergames.BarePlayerGame;
import com.cartographerapi.domain.ExecutionTests;
import org.junit.experimental.categories.Category;

/**
 * Test the execution of the BarePlayerGamesGetter.
 *
 * @author GodlyPerfection
 *
 */
@Category(ExecutionTests.class)
public class BarePlayerGamesGetterExecutionTest {

    private static Player input;

    /**
     * Setup the input for the function.
     */
    @BeforeClass
    public static void createInput() throws IOException {
        input = new Player("GodlyPerfection");
    }

    /**
     * Create a test context.
     *
     * @return
     */
    private Context createContext() {
        TestContext ctx = new TestContext();
        ctx.setFunctionName("BarePlayerGamesGetter");
        return ctx;
    }

    /**
     * Execute the BarePlayerGamesGetter.
     */
    @Test
    public void executeBarePlayerGamesGetter() {
        BarePlayerGamesGetter handler = new BarePlayerGamesGetter();
        Context ctx = createContext();

        List<BarePlayerGame> output = handler.handleRequest(input, ctx);

        if (output != null) {
            for (BarePlayerGame game : output) {
                System.out.println(game.getGamertag());
                System.out.println(game.getGameNumber());
            }
        }
    }
}
