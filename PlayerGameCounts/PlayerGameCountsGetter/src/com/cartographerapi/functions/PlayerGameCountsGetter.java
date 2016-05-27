package com.cartographerapi.functions;

import java.util.List;
import java.util.Arrays;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.cartographerapi.domain.players.Player;
import com.cartographerapi.domain.playergamecounts.PlayerGameCounts;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsDynamoReader;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsGetterService;

/**
 * Gets the PlayerGameCounts from the cache.
 *
 * @author GodlyPerfection
 *
 */
public class PlayerGameCountsGetter implements RequestHandler<Player, PlayerGameCounts> {

    private PlayerGameCountsGetterService getterService;

    /**
     * Checks the cache for the current PlayerGameCounts for the Player.
     *
     * @param input The Player given as input to the Lambda function.
     * @param context The Lambda execution context.
     * @return The cached PlayerGameCounts for a Player.
     */
    @Override
    public PlayerGameCounts handleRequest(Player input, Context context) {
        List<PlayerGameCounts> results =
            getterService.getPlayerGameCounts(Arrays.asList(input));

        return results.get(0);
    }

    public PlayerGameCountsGetter() {
        this.getterService = new PlayerGameCountsGetterService(
            new PlayerGameCountsDynamoReader()
        );
    }
}
