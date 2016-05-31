package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.util.List;
import java.util.Map;

import com.cartographerapi.domain.CapiUtils;
import com.cartographerapi.domain.mapgames.MapId;
import com.cartographerapi.domain.mapgames.MapGame;
import com.cartographerapi.domain.mapgames.MapGamesDynamoReader;
import com.cartographerapi.domain.mapgames.MapGamesGetterService;

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
public class MapGamesBulkGetter implements RequestStreamHandler {

    private ObjectMapper mapper;
    private MapGamesGetterService getterService;

    /**
     * Takes in the Lambda inputStream and outputStream in order to customize
     * how it is sent back to API Gateway.
     *
     * @param inputStream The input stream.
     * @param outputStream The output stream.
     * @param context The Lambda execution context.
    */
    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        List<MapId> input = mapper.readValue(
            CapiUtils.convertStreamToString(inputStream),
            new TypeReference<List<MapId>>(){}
        );

        Map<MapId, List<MapGame>> results =
            getterService.getMapGames(input);

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("Count", input.size());
        rootNode.put("ResultCount", results.size());

        Integer resultGameCount = 0;
        for (Map.Entry<MapId, List<MapGame>> entry : results.entrySet()) {
            resultGameCount += entry.getValue().size();
        }

        rootNode.put("ResultGameCount", resultGameCount);
        ArrayNode formattedResults = mapper.createArrayNode();

        for (Map.Entry<MapId, List<MapGame>> entry : results.entrySet()) {
            ObjectNode resultNode = mapper.valueToTree(entry.getKey());
            resultNode.put("GameCount", entry.getValue().size());
            resultNode.put("Games", mapper.valueToTree(entry.getValue()));
            formattedResults.add(resultNode);
        }

        rootNode.put("Results", formattedResults);

        outputStream.write(
            mapper.writeValueAsString(rootNode)
                .getBytes(java.nio.charset.Charset.forName("UTF-8"))
        );
    }

    public MapGamesBulkGetter() {
        this.mapper = CapiUtils.getPresentationMapper();
        this.getterService = new MapGamesGetterService(
            new MapGamesDynamoReader()
        );
    }

}
