package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.util.List;
import java.util.Scanner;

import com.cartographerapi.domain.CapiUtils;
import com.cartographerapi.domain.game.MatchId;
import com.cartographerapi.domain.game.BareGame;
import com.cartographerapi.domain.game.BareGamesDynamoReader;
import com.cartographerapi.domain.game.BareGamesReader;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Check the BareGames that are stored in the cache.
 *
 * @author GodlyPerfection
 *
 */
public class BareGamesGetter implements RequestStreamHandler, RequestHandler<MatchId, BareGame> {

    private BareGamesReader cacheReader;

    /**
     * Get the BareGames that currently exist in the cache.
     *
     * @param input The MatchId sent in to execute the Lambda.
     * @param context The Lambda execution context.
     * @return The BareGame in the cache.
     */
    @Override
    public BareGame handleRequest(MatchId input, Context context) {
        BareGame game = cacheReader.getBareGameByMatchId(input);
        CapiUtils.logObject(context, game, "BareGame");
        return game;
    }

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
        ObjectMapper mapper = new ObjectMapper();

        MatchId input = mapper.readValue(convertStreamToString(inputStream), MatchId.class);
        BareGame game = cacheReader.getBareGameByMatchId(input);
        outputStream.write(mapper.writeValueAsString(game).getBytes(java.nio.charset.Charset.forName("UTF-8")));
    }

    /**
     * Converts an InputStream into a String.
     *
     * @param inputStream The inputStream that needs to be converted.
     * @return The stringified InputStream.
     */
    @SuppressWarnings("resource")
    // TODO Properly move over this code to a handy service.
    static String convertStreamToString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public BareGamesGetter() {
        this(new BareGamesDynamoReader());
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public BareGamesGetter(BareGamesReader cacheReader) {
        this.cacheReader = cacheReader;
    }

}
