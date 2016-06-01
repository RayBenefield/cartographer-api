package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.util.List;

import com.cartographerapi.domain.CapiUtils;
import com.cartographerapi.domain.game.MatchId;
import com.cartographerapi.domain.gameevents.GameEvents;
import com.cartographerapi.domain.gameevents.GameEventsDynamoReader;
import com.cartographerapi.domain.gameevents.GameEventsGetterService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * Check the GameEvents that are stored in the cache.
 *
 * @author GodlyPerfection
 *
 */
public class GameEventsBulkGetter implements RequestStreamHandler {

    private ObjectMapper mapper;
    private GameEventsGetterService getterService;

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
        List<MatchId> input = mapper.readValue(
            CapiUtils.convertStreamToString(inputStream),
            new TypeReference<List<MatchId>>(){}
        );

        List<GameEvents> results =
            getterService.getGameEvents(input);

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("Count", input.size());
        rootNode.put("ResultCount", results.size());

        Integer eventCount = 0;
        for (GameEvents events : results) {
            eventCount += events.getEventCount();
        }

        rootNode.put("ResultEventCount", eventCount);
        rootNode.put("Results", mapper.valueToTree(results));

        outputStream.write(
            mapper.writeValueAsString(rootNode)
                .getBytes(java.nio.charset.Charset.forName("UTF-8"))
        );
    }

    public GameEventsBulkGetter() {
        this.mapper = CapiUtils.getPresentationMapper();
        this.getterService = new GameEventsGetterService(
            new GameEventsDynamoReader()
        );
    }

}
