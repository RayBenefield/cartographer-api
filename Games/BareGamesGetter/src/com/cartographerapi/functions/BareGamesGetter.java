package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.List;
import java.util.Arrays;

import com.cartographerapi.domain.game.MatchId;
import com.cartographerapi.domain.game.BareGame;
import com.cartographerapi.domain.game.BareGamesDynamoReader;
import com.cartographerapi.domain.game.BareGamesGetterService;

/**
 * Check the BareGames that are stored in the cache.
 *
 * @author GodlyPerfection
 *
 */
public class BareGamesGetter implements RequestHandler<MatchId, BareGame> {

    private BareGamesGetterService getterService;

    /**
     * Get a BareGame that currently exist in the cache.
     *
     * @param input The MatchId sent in to execute the Lambda.
     * @param context The Lambda execution context.
     * @return The BareGame in the cache.
     */
    @Override
    public BareGame handleRequest(MatchId input, Context context) {
        List<BareGame> results =
            getterService.getBareGames(Arrays.asList(input));
        return results.get(0);
    }

    public BareGamesGetter() {
        this.getterService = new BareGamesGetterService(
            new BareGamesDynamoReader()
        );
    }

}
