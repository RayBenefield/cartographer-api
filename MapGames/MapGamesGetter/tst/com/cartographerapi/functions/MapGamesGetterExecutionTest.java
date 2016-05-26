package com.cartographerapi.functions;

import java.io.IOException;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import com.cartographerapi.domain.mapgames.MapId;
import com.cartographerapi.domain.mapgames.MapGame;
import com.cartographerapi.domain.ExecutionTests;
import org.junit.experimental.categories.Category;

/**
 * Test the execution of the MapGamesGetter.
 *
 * @author GodlyPerfection
 *
 */
@Category(ExecutionTests.class)
public class MapGamesGetterExecutionTest {

    private static MapId input;

    /**
     * Setup the input for the function.
     */
    @BeforeClass
    public static void createInput() throws IOException {
        input = new MapId("9bd908fc-4dfa-4ffe-859b-7d28e4147302");
    }

    /**
     * Create a test context.
     *
     * @return
     */
    private Context createContext() {
        TestContext ctx = new TestContext();
        ctx.setFunctionName("MapGamesGetter");
        return ctx;
    }

    /**
     * Execute the MapGamesGetter.
     */
    @Test
    public void executeMapGamesGetter() {
        MapGamesGetter handler = new MapGamesGetter();
        Context ctx = createContext();

        List<MapGame> output = handler.handleRequest(input, ctx);

        if (output != null) {
            for (MapGame game : output) {
                System.out.println(game.getMapId());
                System.out.println(game.getMatchId());
                System.out.println(game.getOwner());
                System.out.println(game.getUrl());
            }
        }
    }
}
