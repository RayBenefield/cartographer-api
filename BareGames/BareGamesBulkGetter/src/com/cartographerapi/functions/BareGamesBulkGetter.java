package com.cartographerapi.functions;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.util.List;

import com.cartographerapi.domain.CapiUtils;
import com.cartographerapi.domain.game.MatchId;
import com.cartographerapi.domain.baregames.BareGame;
import com.cartographerapi.domain.baregames.BareGamesDynamoReader;
import com.cartographerapi.domain.baregames.BareGamesGetterService;

/**
 * Check the BareGames that are stored in the cache.
 *
 * @author GodlyPerfection
 *
 */
public class BareGamesBulkGetter implements RequestStreamHandler {

    private ObjectMapper mapper;
    private BareGamesGetterService getterService;

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

        List<BareGame> results =
            getterService.getBareGames(input);

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("Count", input.size());
        rootNode.put("ResultCount", results.size());
        rootNode.put("Results", mapper.valueToTree(results));

        outputStream.write(
            mapper.writeValueAsString(rootNode)
                .getBytes(java.nio.charset.Charset.forName("UTF-8"))
        );
    }

    public BareGamesBulkGetter() {
        this.mapper = CapiUtils.getPresentationMapper();
        this.getterService = new BareGamesGetterService(
            new BareGamesDynamoReader()
        );
    }

}
