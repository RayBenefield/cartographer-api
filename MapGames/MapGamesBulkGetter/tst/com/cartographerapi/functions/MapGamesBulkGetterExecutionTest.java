package com.cartographerapi.functions;

import java.io.IOException;

import java.util.List;
import java.util.Arrays;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import com.cartographerapi.domain.mapgames.MapId;
import com.cartographerapi.domain.mapgames.MapGame;
import com.cartographerapi.domain.ExecutionTests;
import org.junit.experimental.categories.Category;

/**
 * Test the execution of the MapGamesBulkGetter.
 *
 * @author GodlyPerfection
 *
 */
@Category(ExecutionTests.class)
public class MapGamesBulkGetterExecutionTest {

    private static List<MapId> input;

    /**
     * Setup the input for the function.
     */
    @BeforeClass
    public static void createInput() throws IOException {
        input = Arrays.asList(new MapId("9bd908fc-4dfa-4ffe-859b-7d28e4147302"), new MapId("9bd908fc-4dfa-4ffe-859b-7d28e4147302"));
    }

    /**
     * Create a test context.
     *
     * @return
     */
    private Context createContext() {
        TestContext ctx = new TestContext();
        ctx.setFunctionName("MapGamesBulkGetter");
        return ctx;
    }

    /**
     * Execute the MapGamesBulkGetter.
     */
    @Test
    public void executeMapGamesBulkGetter() {
    }
}
