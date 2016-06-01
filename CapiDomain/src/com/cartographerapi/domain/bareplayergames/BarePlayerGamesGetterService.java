package com.cartographerapi.domain.bareplayergames;

import java.util.List;
import java.util.Map;

import com.cartographerapi.domain.CapiUtils;
import com.cartographerapi.domain.players.Player;
import com.cartographerapi.domain.bareplayergames.BarePlayerGame;
import com.cartographerapi.domain.bareplayergames.BarePlayerGamesReader;

/**
 * Gets the BarePlayerGames from the cache.
 *
 * @author GodlyPerfection
 *
 */
public class BarePlayerGamesGetterService {

    private BarePlayerGamesReader cacheReader;

    /**
     * Gets the BarePlayerGames for all players requested.
     *
     * @param players
     * @return The BarePlayerGames for all of the Players.
     */
    public Map<Player, List<BarePlayerGame>> getBarePlayerGames(List<Player> players) {
        Map<Player, List<BarePlayerGame>> games = cacheReader.getBarePlayerGamesByPlayers(players);
        CapiUtils.logObject(games.size(), "# of BarePlayerGames in the cache");
        return games;
    }

    public BarePlayerGamesGetterService(BarePlayerGamesReader cacheReader) {
        this.cacheReader = cacheReader;
    }
}
