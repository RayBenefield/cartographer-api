package com.cartographerapi.functions;

import java.io.IOException;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import com.amazonaws.services.lambda.runtime.Context;
import com.cartographerapi.domain.PlayerGame;
import com.cartographerapi.domain.Gamertag;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class PlayerGamesGetterTest {

    private static Gamertag input;

    @BeforeClass
    public static void createInput() throws IOException {
        // TODO: set up your sample input object here.
        input = new Gamertag("GodlyPerfection");
    }

    private Context createContext() {
        TestContext ctx = new TestContext();

        // TODO: customize your context here if needed.
        ctx.setFunctionName("Your Function Name");

        return ctx;
    }

    @Test
    public void testPlayerGamesGetter() {
        PlayerGamesGetter handler = new PlayerGamesGetter();
        Context ctx = createContext();

        List<PlayerGame> output = handler.handleRequest(input, ctx);

        if (output != null) {
        	for (PlayerGame game : output) {
				System.out.println(game.getGamertag());
				System.out.println(game.getGameNumber());
				System.out.println(game.getGameData().toString());
        	}
        }
    }
}
