package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.List;

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
public class BareGamesBulkGetter implements RequestHandler<List<MatchId>, List<BareGame>> {

    private BareGamesGetterService getterService;

    /**
     * Get BareGames that currently exist in the cache.
     *
     * @param input The MatchId sent in to execute the Lambda.
     * @param context The Lambda execution context.
     * @return The BareGame in the cache.
     */
    @Override
    public List<BareGame> handleRequest(List<MatchId> input, Context context) {
        List<BareGame> results =
            getterService.getBareGames(input);
        return results;
    }

    public BareGamesBulkGetter() {
        this.getterService = new BareGamesGetterService(
            new BareGamesDynamoReader()
        );
    }

}
