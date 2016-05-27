package com.cartographerapi.domain.playergamecounts;

import java.util.List;

import com.cartographerapi.domain.CapiUtils;
import com.cartographerapi.domain.players.Player;
import com.cartographerapi.domain.playergamecounts.PlayerGameCounts;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsReader;

/**
 * Gets the PlayerGameCounts from the cache, and adds to the cache if it doesn't
 * exist.
 *
 * @author GodlyPerfection
 *
 */
public class PlayerGameCountsGetterService {

    private PlayerGameCountsReader cacheReader;

    /**
     * Gets the PlayerGameCounts for all players requested.
     *
     * @param players
     * @return The PlayerGameCounts for all of the Players.
     */
    public List<PlayerGameCounts> getPlayerGameCounts(List<Player> players) {
        List<PlayerGameCounts> counts = cacheReader.getPlayerGameCountsByPlayers(players);
        CapiUtils.logObject(counts, "PlayerGameCounts from cache");
        return counts;
    }

    public PlayerGameCountsGetterService(PlayerGameCountsReader cacheReader) {
        this.cacheReader = cacheReader;
    }
}
