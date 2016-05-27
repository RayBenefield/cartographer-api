package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.cartographerapi.domain.players.Player;
import com.cartographerapi.domain.playergames.PlayerGame;
import com.cartographerapi.domain.playergames.PlayerGamesDynamoReader;
import com.cartographerapi.domain.playergames.PlayerGamesGetterService;

/**
 * Check the PlayerGames that are stored in the cache.
 *
 * @author GodlyPerfection
 *
 */
public class PlayerGamesGetter implements RequestHandler<Player, List<PlayerGame>> {

    private PlayerGamesGetterService getterService;

    /**
     * Get the PlayerGames that currently exist in the cache.
     *
     * @param input The Player sent in to execute the Lambda.
     * @param context The Lambda execution context.
     * @return The PlayerGames in the cache.
     */
    @Override
    public List<PlayerGame> handleRequest(Player input, Context context) {
        Map<Player, List<PlayerGame>> results =
            getterService.getPlayerGames(Arrays.asList(input));
        return results.get(input);
    }

    public PlayerGamesGetter() {
        this.getterService = new PlayerGamesGetterService(
            new PlayerGamesDynamoReader()
        );
    }

}
