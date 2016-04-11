package com.cartographerapi.functions;

import java.util.List;
import java.io.IOException;
import org.junit.BeforeClass;
import org.junit.Test;
import com.cartographerapi.domain.ScheduledEvent;
import com.cartographerapi.domain.PlayerGameCounts;

import com.amazonaws.services.lambda.runtime.Context;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class PlayerGameCountsRefresherTest {

    private static ScheduledEvent input;

    @BeforeClass
    public static void createInput() throws IOException {
        // TODO: set up your sample input object here.
        input = new ScheduledEvent("2016-04-09T23:59:00.000Z");
    }

    private Context createContext() {
        TestContext ctx = new TestContext();

        // TODO: customize your context here if needed.
        ctx.setFunctionName("Your Function Name");

        return ctx;
    }

    @Test
    public void testPlayerGameCountsRefresher() {
        PlayerGameCountsRefresher handler = new PlayerGameCountsRefresher();
        Context ctx = createContext();

        List<PlayerGameCounts> output = handler.handleRequest(input, ctx);

		if (output != null) {
			for (PlayerGameCounts counts : output) {
				System.out.println(counts.getGamertag());
			}
		}
    }
}
