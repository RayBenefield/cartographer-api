package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.util.List;
import java.util.Scanner;

import com.cartographerapi.domain.CapiUtils;
import com.cartographerapi.domain.players.Player;
import com.cartographerapi.domain.playergames.BarePlayerGame;
import com.cartographerapi.domain.playergames.BarePlayerGamesDynamoReader;
import com.cartographerapi.domain.playergames.BarePlayerGamesReader;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Check the BarePlayerGames that are stored in the cache.
 *
 * @author GodlyPerfection
 *
 */
public class BarePlayerGamesGetter implements RequestStreamHandler, RequestHandler<Player, List<BarePlayerGame>> {

    private BarePlayerGamesReader cacheReader;

    /**
     * Get the BarePlayerGames that currently exist in the cache.
     *
     * @param input The Player sent in to execute the Lambda.
     * @param context The Lambda execution context.
     * @return The BarePlayerGames in the cache.
     */
    @Override
    // TODO Accept JSON view names in order to determine what data to show.
    public List<BarePlayerGame> handleRequest(Player input, Context context) {
        List<BarePlayerGame> games = cacheReader.getBarePlayerGamesByGamertag(input.getGamertag());
        CapiUtils.logObject(context, games.size(), "# of BarePlayerGames in the cache");
        return games;
    }

    /**
     * Takes in the Lambda inputStream and outputStream in order to customize
     * how it is sent back to API Gateway.
     *
     * @param input The Cloudwatch scheduled event that triggered this.
     * @param context The Lambda execution context.
     * @return The newly added Games.
     */
    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        Player req = mapper.readValue(convertStreamToString(inputStream), Player.class);
        List<BarePlayerGame> games = cacheReader.getBarePlayerGamesByGamertag(req.getGamertag());
        outputStream.write(mapper.writeValueAsString(games).getBytes(java.nio.charset.Charset.forName("UTF-8")));
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
    public BarePlayerGamesGetter() {
        this(new BarePlayerGamesDynamoReader());
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public BarePlayerGamesGetter(BarePlayerGamesReader cacheReader) {
        this.cacheReader = cacheReader;
    }

}
