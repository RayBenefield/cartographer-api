package com.cartographerapi.domain.playergames;

import java.util.List;
import java.util.Map;

import com.cartographerapi.domain.CapiUtils;
import com.cartographerapi.domain.players.Player;
import com.cartographerapi.domain.playergames.PlayerGame;
import com.cartographerapi.domain.playergames.PlayerGamesReader;

/**
 * Gets the PlayerGames from the cache.
 *
 * @author GodlyPerfection
 *
 */
public class PlayerGamesGetterService {

    private PlayerGamesReader cacheReader;

    /**
     * Gets the PlayerGames for all players requested.
     *
     * @param players
     * @return The PlayerGames for all of the Players.
     */
    public Map<Player, List<PlayerGame>> getPlayerGames(List<Player> players) {
        Map<Player, List<PlayerGame>> games = cacheReader.getPlayerGamesByPlayers(players);
        CapiUtils.logObject(games.size(), "# of PlayerGames in the cache");
        return games;
    }

    public PlayerGamesGetterService(PlayerGamesReader cacheReader) {
        this.cacheReader = cacheReader;
    }
}
