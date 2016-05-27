package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.List;
import java.util.Map;

import com.cartographerapi.domain.mapgames.MapId;
import com.cartographerapi.domain.mapgames.MapGame;
import com.cartographerapi.domain.mapgames.MapGamesDynamoReader;
import com.cartographerapi.domain.mapgames.MapGamesGetterService;

/**
 * Check the MapGames that are stored in the cache.
 *
 * @author GodlyPerfection
 *
 */
public class MapGamesBulkGetter implements RequestHandler<List<MapId>, Map<MapId, List<MapGame>>> {

    private MapGamesGetterService getterService;

    /**
     * Get the MapGames that currently exist in the cache.
     *
     * @param input The MapId sent in to execute the Lambda.
     * @param context The Lambda execution context.
     * @return The MapGame in the cache.
     */
    @Override
    public Map<MapId, List<MapGame>> handleRequest(List<MapId> input, Context context) {
        Map<MapId, List<MapGame>> results =
            getterService.getMapGames(input);
        return results;
    }

    public MapGamesBulkGetter() {
        this.getterService = new MapGamesGetterService(
            new MapGamesDynamoReader()
        );
    }

}
