package com.cartographerapi.functions;

import java.io.IOException;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import com.cartographerapi.domain.game.MatchId;
import com.cartographerapi.domain.baregames.BareGame;
import com.cartographerapi.domain.ExecutionTests;
import org.junit.experimental.categories.Category;

/**
 * Test the execution of the BareGamesGetter.
 *
 * @author GodlyPerfection
 *
 */
@Category(ExecutionTests.class)
public class BareGamesGetterExecutionTest {

    private static MatchId input;

    /**
     * Setup the input for the function.
     */
    @BeforeClass
    public static void createInput() throws IOException {
        input = new MatchId("9158fdf0-259f-4386-b824-2842ca822fbb");
    }

    /**
     * Create a test context.
     *
     * @return
     */
    private Context createContext() {
        TestContext ctx = new TestContext();
        ctx.setFunctionName("BareGamesGetter");
        return ctx;
    }

    /**
     * Execute the BareGamesGetter.
     */
    @Test
    public void executeBareGamesGetter() {
    }
}
