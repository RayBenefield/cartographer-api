package com.cartographerapi.functions;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import com.cartographerapi.domain.players.Player;
import com.cartographerapi.domain.playergamecounts.PlayerGameCounts;
import com.cartographerapi.domain.ExecutionTests;
import com.cartographerapi.functions.PlayerGameCountsGetter;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.experimental.categories.Category;

/**
 * Test the functionality of the PlayerGameCountsGetter function from
 * end-to-end with no mocking.
 *
 * @author GodlyPerfection
 *
 */
@Category(ExecutionTests.class)
public class PlayerGameCountsGetterExecutionTest {

    private static Player input;

    /**
     * Setup the input for the function.
     */
    @BeforeClass
    public static void createInput() throws IOException {
        input = new Player("Ray Benefield");
    }

    /**
     * Create a test context.
     *
     * @return
     */
    private Context createContext() {
        TestContext ctx = new TestContext();
        ctx.setFunctionName("PlayerGameCountsGetter");
        return new TestContext();
    }

    /**
     * Execute the PlayerGameCountsGetter.
     */
    @Test
    public void executePlayerGameCountsUpdater() {
        try {
            PlayerGameCountsGetter getter = new PlayerGameCountsGetter();
            Context ctx = createContext();
            ObjectMapper mapper = new ObjectMapper();

            InputStream inputStream = new ByteArrayInputStream(
                mapper.writeValueAsString(input).getBytes("UTF-8")
            );
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            getter.handleRequest(inputStream, outputStream, ctx);
            String outputString = new String(outputStream.toByteArray(), java.nio.charset.StandardCharsets.UTF_8);
            PlayerGameCounts output = mapper.readValue(outputString, PlayerGameCounts.class);

            if (output != null) {
                System.out.println(output.getGamertag());
                System.out.println(output.getGamesCompleted());
                System.out.println(output.getTotalGames());
            }
        } catch (IOException exception) {
        }
    }
}
