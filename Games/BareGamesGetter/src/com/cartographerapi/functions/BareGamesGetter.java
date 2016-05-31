package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.util.List;
import java.util.Arrays;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.cartographerapi.domain.CapiUtils;
import com.cartographerapi.domain.game.MatchId;
import com.cartographerapi.domain.game.BareGame;
import com.cartographerapi.domain.game.BareGamesDynamoReader;
import com.cartographerapi.domain.game.BareGamesGetterService;
import com.cartographerapi.domain.exceptions.NotFoundException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Check the BareGames that are stored in the cache.
 *
 * @author GodlyPerfection
 *
 */
public class BareGamesGetter implements RequestStreamHandler {

    private ObjectMapper mapper;
    private BareGamesGetterService getterService;

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
        MatchId input = mapper.readValue(
            CapiUtils.convertStreamToString(inputStream),
            MatchId.class
        );

        List<BareGame> results =
            getterService.getBareGames(Arrays.asList(input));

        if (results.size() == 0) {
            throw new NotFoundException(
                "The game with the match ID of `" + input.getMatchId() + "` was not found."
            );
        }

        outputStream.write(
            mapper.writeValueAsString(results.get(0))
                .getBytes(java.nio.charset.Charset.forName("UTF-8"))
        );
    }

    public BareGamesGetter() {
        this.mapper = CapiUtils.getPresentationMapper();
        this.getterService = new BareGamesGetterService(
            new BareGamesDynamoReader()
        );
    }

}
