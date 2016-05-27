package com.cartographerapi.domain.mapgames;

import java.util.List;
import java.util.Map;

import com.cartographerapi.domain.CapiUtils;
import com.cartographerapi.domain.mapgames.MapId;
import com.cartographerapi.domain.mapgames.MapGame;
import com.cartographerapi.domain.mapgames.MapGamesReader;

/**
 * Gets the MapGames from the cache.
 *
 * @author GodlyPerfection
 *
 */
public class MapGamesGetterService {

    private MapGamesReader cacheReader;

    /**
     * Gets the MapGames for all MapIds requested.
     *
     * @param players
     * @return The MapGames for all of the MapIds.
     */
    public Map<MapId, List<MapGame>> getMapGames(List<MapId> mapIds) {
        Map<MapId, List<MapGame>> games = cacheReader.getMapGamesByMapIds(mapIds);
        CapiUtils.logObject(games.size(), "# of MapGames in the cache");
        return games;
    }

    public MapGamesGetterService(MapGamesReader cacheReader) {
        this.cacheReader = cacheReader;
    }
}
