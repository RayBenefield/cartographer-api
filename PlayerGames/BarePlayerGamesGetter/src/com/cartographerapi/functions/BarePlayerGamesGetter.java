package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.cartographerapi.domain.players.Player;
import com.cartographerapi.domain.playergames.BarePlayerGame;
import com.cartographerapi.domain.playergames.BarePlayerGamesGetterService;
import com.cartographerapi.domain.playergames.BarePlayerGamesDynamoReader;

/**
 * Check the BarePlayerGames that are stored in the cache.
 *
 * @author GodlyPerfection
 *
 */
public class BarePlayerGamesGetter implements RequestHandler<Player, List<BarePlayerGame>> {

    private BarePlayerGamesGetterService getterService;

    /**
     * Get the BarePlayerGames that currently exist in the cache.
     *
     * @param input The Player sent in to execute the Lambda.
     * @param context The Lambda execution context.
     * @return The BarePlayerGames in the cache.
     */
    @Override
    public List<BarePlayerGame> handleRequest(Player input, Context context) {
        Map<Player, List<BarePlayerGame>> results =
            getterService.getBarePlayerGames(Arrays.asList(input));
        return results.get(input);
    }

    public BarePlayerGamesGetter() {
        this.getterService = new BarePlayerGamesGetterService(
            new BarePlayerGamesDynamoReader()
        );
    }

}
