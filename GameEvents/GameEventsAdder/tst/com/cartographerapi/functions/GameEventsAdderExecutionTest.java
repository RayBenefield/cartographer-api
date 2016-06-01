package com.cartographerapi.functions;

import java.io.IOException;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import com.cartographerapi.domain.ScheduledEvent;
import com.cartographerapi.domain.gameevents.GameEvents;
import com.cartographerapi.domain.ExecutionTests;
import org.junit.experimental.categories.Category;

/**
 * Test the execution of the GameEventsAdder.
 *
 * @author GodlyPerfection
 *
 */
@Category(ExecutionTests.class)
public class GameEventsAdderExecutionTest {

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
        ctx.setFunctionName("GameEventsAdder");
        return ctx;
    }

    /**
     * Execute the GameEventsAdder.
     */
    @Test
    public void executeGameEventsAdder() {
    }
}
