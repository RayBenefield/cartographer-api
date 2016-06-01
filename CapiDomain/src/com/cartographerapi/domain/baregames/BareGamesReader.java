package com.cartographerapi.domain.baregames;

import java.util.List;

import com.cartographerapi.domain.game.MatchId;

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

    /**
     * Get BareGames with the given MatchIds.
     *
     * @param matchIds
     * @return
     */
    public List<BareGame> getBareGamesByMatchIds(List<MatchId> matchIds);

}
