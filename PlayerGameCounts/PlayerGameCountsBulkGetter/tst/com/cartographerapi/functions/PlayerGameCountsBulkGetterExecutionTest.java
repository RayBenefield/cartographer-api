package com.cartographerapi.functions;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import com.cartographerapi.domain.players.Player;
import com.cartographerapi.domain.playergamecounts.PlayerGameCounts;
import com.cartographerapi.domain.ExecutionTests;
import com.cartographerapi.functions.PlayerGameCountsBulkGetter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import org.junit.experimental.categories.Category;

/**
 * Test the functionality of the PlayerGameCountsBulkGetter function from
 * end-to-end with no mocking.
 *
 * @author GodlyPerfection
 *
 */
@Category(ExecutionTests.class)
public class PlayerGameCountsBulkGetterExecutionTest {

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
        ctx.setFunctionName("PlayerGameCountsBulkGetter");
        return new TestContext();
    }

    /**
     * Execute the PlayerGameCountsBulkGetter.
     */
    @Test
    public void executePlayerGameCountsBulkGetter() {
        try {
            PlayerGameCountsBulkGetter getter = new PlayerGameCountsBulkGetter();
            Context ctx = createContext();
            ObjectMapper mapper = new ObjectMapper();

            InputStream inputStream = new ByteArrayInputStream(
                mapper.writeValueAsString(input).getBytes("UTF-8")
            );
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            getter.handleRequest(inputStream, outputStream, ctx);
            String outputString = new String(outputStream.toByteArray(), java.nio.charset.StandardCharsets.UTF_8);
            List<PlayerGameCounts> output = mapper.readValue(outputString, new TypeReference<List<PlayerGameCounts>>(){});

            if (output != null) {
                for (PlayerGameCounts counts : output) {
                    System.out.println(counts.getGamertag());
                    System.out.println(counts.getGamesCompleted());
                    System.out.println(counts.getTotalGames());
                }
            }
        } catch (IOException exception) {
        }
    }
}
