package com.cartographerapi.domain.game;

import java.util.List;
import java.util.Map;

import com.cartographerapi.domain.CapiUtils;
import com.cartographerapi.domain.game.MatchId;
import com.cartographerapi.domain.game.BareGame;
import com.cartographerapi.domain.game.BareGamesReader;

/**
 * Gets the BareGames from the cache.
 *
 * @author GodlyPerfection
 *
 */
public class BareGamesGetterService {

    private BareGamesReader cacheReader;

    /**
     * Gets the BareGames for all matchIds requested.
     *
     * @param matchIds
     * @return The BareGames for all of the MatchIds.
     */
    public List<BareGame> getBareGames(List<MatchId> matchIds) {
        List<BareGame> games = cacheReader.getBareGamesByMatchIds(matchIds);
        CapiUtils.logObject(games, "BareGames in the cache");
        return games;
    }

    public BareGamesGetterService(BareGamesReader cacheReader) {
        this.cacheReader = cacheReader;
    }
}
