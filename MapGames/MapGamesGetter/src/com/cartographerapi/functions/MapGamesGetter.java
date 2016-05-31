package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.util.List;
import java.util.Arrays;
import java.util.Map;

import com.cartographerapi.domain.CapiUtils;
import com.cartographerapi.domain.mapgames.MapId;
import com.cartographerapi.domain.mapgames.MapGame;
import com.cartographerapi.domain.mapgames.MapGamesDynamoReader;
import com.cartographerapi.domain.mapgames.MapGamesGetterService;
import com.cartographerapi.domain.exceptions.NotFoundException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * Check the MapGames that are stored in the cache.
 *
 * @author GodlyPerfection
 *
 */
public class MapGamesGetter implements RequestStreamHandler {

    private ObjectMapper mapper;
    private MapGamesGetterService getterService;

    /**
     * Takes in the Lambda inputStream and outputStream in order to customize
     * how it is sent back to API Gateway.
     *
     * @param input The Cloudwatch scheduled event that triggered this.
     * @param context The Lambda execution context.
     * @return The BareGame in the cache.
    */
    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        MapId input = mapper.readValue(
            CapiUtils.convertStreamToString(inputStream),
            MapId.class
        );

        Map<MapId, List<MapGame>> results =
            getterService.getMapGames(Arrays.asList(input));

        if (results.get(input) == null) {
            throw new NotFoundException(
                "The map with the map ID of `" + input.getMapId() + "` was not found."
            );
        }

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("ResultGameCount", results.get(input).size());
        rootNode.put("Results", mapper.valueToTree(results.get(input)));

        outputStream.write(
            mapper.writeValueAsString(rootNode)
                .getBytes(java.nio.charset.Charset.forName("UTF-8"))
        );
    }

    public MapGamesGetter() {
        this.mapper = CapiUtils.getPresentationMapper();
        this.getterService = new MapGamesGetterService(
            new MapGamesDynamoReader()
        );
    }

}
