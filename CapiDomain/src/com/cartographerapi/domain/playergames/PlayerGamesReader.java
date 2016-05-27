package com.cartographerapi.domain.playergames;

import java.util.List;
import java.util.Map;

import com.cartographerapi.domain.players.Player;

/**
 * Reader repository interface for PlayerGames. This provides access to a
 * data source that contains the PlayerGame object.
 *
 * @author GodlyPerfection
 *
 */
public interface PlayerGamesReader {

    /**
     * Get PlayerGame objects for a given Gamertag.
     *
     * @param gamertag
     * @return
     */
    public List<PlayerGame> getPlayerGamesByGamertag(String gamertag);

    /**
     * Get PlayerGame objects for a given Gamertag.
     *
     * @param gamertag
     * @return
     */
    public List<PlayerGame> getPlayerGamesByGamertag(String gamertag, Integer start, Integer count);

    /**
     * Get PlayerGame objects for each Player.
     *
     * @param players
     * @return
     */
    public Map<Player, List<PlayerGame>> getPlayerGamesByPlayers(List<Player> players);

}
