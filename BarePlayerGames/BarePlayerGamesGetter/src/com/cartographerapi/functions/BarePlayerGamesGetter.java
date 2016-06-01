package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.cartographerapi.domain.CapiUtils;
import com.cartographerapi.domain.players.Player;
import com.cartographerapi.domain.bareplayergames.BarePlayerGame;
import com.cartographerapi.domain.bareplayergames.BarePlayerGamesGetterService;
import com.cartographerapi.domain.bareplayergames.BarePlayerGamesDynamoReader;
import com.cartographerapi.domain.exceptions.NotFoundException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Check the BarePlayerGames that are stored in the cache.
 *
 * @author GodlyPerfection
 *
 */
public class BarePlayerGamesGetter implements RequestStreamHandler {

    private ObjectMapper mapper;
    private BarePlayerGamesGetterService getterService;

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
        Player input = mapper.readValue(
            CapiUtils.convertStreamToString(inputStream),
            Player.class
        );

        Map<Player, List<BarePlayerGame>> results =
            getterService.getBarePlayerGames(Arrays.asList(input));

        if (results.get(input) == null) {
            throw new NotFoundException(
                "The player with the gamertag of `" + input.getGamertag() + "` was not found."
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

    public BarePlayerGamesGetter() {
        this.mapper = CapiUtils.getPresentationMapper();
        this.getterService = new BarePlayerGamesGetterService(
            new BarePlayerGamesDynamoReader()
        );
    }

}
