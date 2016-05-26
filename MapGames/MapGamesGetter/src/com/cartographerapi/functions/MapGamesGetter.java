package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.util.List;
import java.util.Scanner;

import com.cartographerapi.domain.CapiUtils;
import com.cartographerapi.domain.mapgames.MapId;
import com.cartographerapi.domain.mapgames.MapGame;
import com.cartographerapi.domain.mapgames.MapGamesDynamoReader;
import com.cartographerapi.domain.mapgames.MapGamesReader;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Check the MapGames that are stored in the cache.
 *
 * @author GodlyPerfection
 *
 */
public class MapGamesGetter implements RequestStreamHandler, RequestHandler<MapId, List<MapGame>> {

    private MapGamesReader cacheReader;

    /**
     * Get the MapGames that currently exist in the cache.
     *
     * @param input The MapId sent in to execute the Lambda.
     * @param context The Lambda execution context.
     * @return The MapGame in the cache.
     */
    @Override
    public List<MapGame> handleRequest(MapId input, Context context) {
        List<MapGame> game = cacheReader.getMapGamesByMapId(input);
        CapiUtils.logObject(context, game, "MapGame");
        return game;
    }

    /**
     * Takes in the Lambda inputStream and outputStream in order to customize
     * how it is sent back to API Gateway.
     *
     * @param input The Cloudwatch scheduled event that triggered this.
     * @param context The Lambda execution context.
     * @return The MapGame in the cache.
     */
    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        MapId input = mapper.readValue(convertStreamToString(inputStream), MapId.class);
        List<MapGame> games = cacheReader.getMapGamesByMapId(input);
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
    public MapGamesGetter() {
        this(new MapGamesDynamoReader());
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public MapGamesGetter(MapGamesReader cacheReader) {
        this.cacheReader = cacheReader;
    }

}
