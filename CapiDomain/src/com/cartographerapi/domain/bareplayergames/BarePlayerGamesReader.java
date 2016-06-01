package com.cartographerapi.domain.bareplayergames;

import java.util.List;
import java.util.Map;

import com.cartographerapi.domain.players.Player;

/**
 * Reader repository interface for BarePlayerGames. This provides access to a
 * data source that contains the BarePlayerGame object.
 *
 * @author GodlyPerfection
 *
 */
public interface BarePlayerGamesReader {

    /**
     * Get BarePlayerGame objects for a given Gamertag.
     *
     * @param gamertag
     * @return
     */
    public List<BarePlayerGame> getBarePlayerGamesByGamertag(String gamertag);

    /**
     * Get BarePlayerGame objects for a given Gamertag.
     *
     * @param gamertag
     * @return
     */
    public List<BarePlayerGame> getBarePlayerGamesByGamertag(String gamertag, Integer start, Integer count);

    /**
     * Get BarePlayerGame objects for each Player.
     *
     * @param players
     * @return
     */
    public Map<Player, List<BarePlayerGame>> getBarePlayerGamesByPlayers(List<Player> players);

}
