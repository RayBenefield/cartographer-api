package com.cartographerapi.domain.playergamecounts;

import java.util.List;

import com.cartographerapi.domain.players.Player;

/**
 * Reader repository interface for PlayerGameCounts. This provides access to a
 * data source that contains the PlayerGameCounts object.
 *
 * @author GodlyPerfection
 *
 */
public interface PlayerGameCountsReader {

    /**
     * Get a PlayerGameCounts object for a given Gamertag.
     *
     * @param gamertag
     * @return
     */
    public PlayerGameCounts getPlayerGameCountsByGamertag(String gamertag);

    /**
     * Get a PlayerGameCounts object based on an existing PlayerGameCounts object.
     *
     * @param counts
     * @return
     */
    public PlayerGameCounts getPlayerGameCountsByPlayerGameCounts(PlayerGameCounts counts);

    /**
     * Get PlayerGameCounts objects based on Players.
     *
     * @param players
     * @return
     */
    public List<PlayerGameCounts> getPlayerGameCountsByPlayers(List<Player> players);

}
