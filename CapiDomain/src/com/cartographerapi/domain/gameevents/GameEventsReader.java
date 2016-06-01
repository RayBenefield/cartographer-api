package com.cartographerapi.domain.gameevents;

import java.util.List;

import com.cartographerapi.domain.game.MatchId;

/**
 * Reader repository interface for GameEvents. This provides access to a
 * data source that contains the GameEvents object.
 *
 * @author GodlyPerfection
 *
 */
public interface GameEventsReader {

    /**
     * Get GameEvents objects for a given MatchId.
     *
     * @param matchId
     * @return
     */
    public GameEvents getGameEventsByMatchId(MatchId matchId);

    /**
     * Get GameEvents objects for all given MatchIds.
     *
     * @param matchIds
     * @return
     */
    public List<GameEvents> getGameEventsByMatchIds(List<MatchId> matchIds);

}
