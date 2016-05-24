package com.cartographerapi.functions;

import java.io.IOException;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import com.cartographerapi.domain.ScheduledEvent;
import com.cartographerapi.domain.mapgames.MapGame;
import com.cartographerapi.domain.ExecutionTests;
import org.junit.experimental.categories.Category;

/**
 * Test the execution of the MapGamesAdder.
 * 
 * @author GodlyPerfection
 * 
 */
@Category(ExecutionTests.class)
public class MapGamesAdderExecutionTest {

    private static ScheduledEvent input;

    /**
     * Setup the input for the function.
     */
    @BeforeClass
    public static void createInput() throws IOException {
        input = new ScheduledEvent();
    }

    /**
     * Create a test context.
     * 
     * @return
     */
    private Context createContext() {
        TestContext ctx = new TestContext();
        ctx.setFunctionName("MapGamesAdder");
        return ctx;
    }

    /**
     * Execute the MapGamesAdder.
     */
    @Test
    public void executeMapGamesAdder() {
        MapGamesAdder handler = new MapGamesAdder();
        Context ctx = createContext();

        List<MapGame> output = handler.handleRequest(input, ctx);

        if (output != null) {
			for (MapGame mapGame : output) {
				System.out.println(mapGame.getMapId() + " -> " + mapGame.getMatchId());
			}
        }
    }
}
