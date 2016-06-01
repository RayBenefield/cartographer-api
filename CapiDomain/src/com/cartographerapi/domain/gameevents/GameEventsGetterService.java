package com.cartographerapi.domain.gameevents;

import java.util.List;

import com.cartographerapi.domain.CapiUtils;
import com.cartographerapi.domain.game.MatchId;

/**
 * Gets the GameEvents from the cache.
 *
 * @author GodlyPerfection
 *
 */
public class GameEventsGetterService {

    private GameEventsReader cacheReader;

    /**
     * Gets the GameEvents for all MatchIds requested.
     *
     * @param players
     * @return The GameEvents for all of the MatchIds.
     */
    public List<GameEvents> getGameEvents(List<MatchId> matchIds) {
        List<GameEvents> events = cacheReader.getGameEventsByMatchIds(matchIds);
        CapiUtils.logObject(events.size(), "# of GameEvents in the cache");
        return events;
    }

    public GameEventsGetterService(GameEventsReader cacheReader) {
        this.cacheReader = cacheReader;
    }
}
