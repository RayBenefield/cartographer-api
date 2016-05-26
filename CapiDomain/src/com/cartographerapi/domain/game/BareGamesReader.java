package com.cartographerapi.domain.game;

/**
 * Reader repository interface for BareGames. This provides access to a
 * data source that contains the BareGame object.
 *
 * @author GodlyPerfection
 *
 */
public interface BareGamesReader {

    /**
     * Get BareGame with the given MatchId.
     *
     * @param matchId
     * @return
     */
    public BareGame getBareGameByMatchId(MatchId matchId);

}
