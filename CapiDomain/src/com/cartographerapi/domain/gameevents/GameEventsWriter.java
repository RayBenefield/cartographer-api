package com.cartographerapi.domain.gameevents;

import java.util.List;

/**
 * Writer repository interface for GameEvents. This provides access to a
 * data source that contains the GameEvents object.
 *
 * @author GodlyPerfection
 *
 */
public interface GameEventsWriter {

    /**
     * Save GameEvents object.
     *
     * @param mapGame
     * @return
     */
    public GameEvents saveGameEvents(GameEvents gameEvents);

    /**
     * Save multiple Game objects.
     *
     * @param mapGames
     * @return
     */
    public List<GameEvents> saveGameEvents(List<GameEvents> gameEvents);

}
