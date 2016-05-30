package com.cartographerapi.functions;

import java.util.List;
import java.util.Arrays;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import com.cartographerapi.domain.CapiUtils;
import com.cartographerapi.domain.players.Player;
import com.cartographerapi.domain.playergamecounts.PlayerGameCounts;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsDynamoReader;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsGetterService;
import com.cartographerapi.domain.exceptions.NotFoundException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Gets the PlayerGameCounts from the cache.
 *
 * @author GodlyPerfection
 *
 */
public class PlayerGameCountsGetter implements RequestStreamHandler {

    private ObjectMapper mapper;
    private PlayerGameCountsGetterService getterService;

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

        List<PlayerGameCounts> results =
            getterService.getPlayerGameCounts(Arrays.asList(input));

        if (results.get(0) == null) {
            throw new NotFoundException(
                "The player with the gamertag of `" + input.getGamertag() + "` was not found."
            );
        }

        outputStream.write(
            mapper.writeValueAsString(results.get(0))
                .getBytes(java.nio.charset.Charset.forName("UTF-8"))
        );
    }

    public PlayerGameCountsGetter() {
        this.mapper = CapiUtils.getPresentationMapper();
        this.getterService = new PlayerGameCountsGetterService(
            new PlayerGameCountsDynamoReader()
        );
    }
}
