package com.cartographerapi.functions;

import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import com.cartographerapi.domain.players.Player;
import com.cartographerapi.domain.bareplayergames.BarePlayerGame;
import com.cartographerapi.domain.ExecutionTests;
import org.junit.experimental.categories.Category;

/**
 * Test the execution of the BarePlayerGamesBulkGetter.
 *
 * @author GodlyPerfection
 *
 */
@Category(ExecutionTests.class)
public class BarePlayerGamesBulkGetterExecutionTest {

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
        ctx.setFunctionName("BarePlayerGamesBulkGetter");
        return ctx;
    }

    /**
     * Execute the BarePlayerGamesBulkGetter.
     */
    @Test
    public void executeBarePlayerGamesBulkGetter() {
    }
}
