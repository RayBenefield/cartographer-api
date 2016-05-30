package com.cartographerapi.functions;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import com.cartographerapi.domain.players.Player;
import com.cartographerapi.domain.playergames.BarePlayerGame;
import com.cartographerapi.domain.ExecutionTests;
import org.junit.experimental.categories.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        try {
            BarePlayerGamesGetter getter = new BarePlayerGamesGetter();
            Context ctx = createContext();
            ObjectMapper mapper = new ObjectMapper();

            InputStream inputStream = new ByteArrayInputStream(
                mapper.writeValueAsString(input).getBytes("UTF-8")
            );
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            getter.handleRequest(inputStream, outputStream, ctx);
            String outputString = new String(outputStream.toByteArray(), java.nio.charset.StandardCharsets.UTF_8);
            List<BarePlayerGame> output = mapper.readValue(outputString, new TypeReference<List<BarePlayerGame>>(){});

            if (output != null) {
                for (BarePlayerGame game : output) {
                    System.out.println(game.getGamertag());
                    System.out.println(game.getGameNumber());
                }
            }
        } catch (IOException exception) {
        }
    }
}
