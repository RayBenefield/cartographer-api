package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.util.List;
import java.util.Scanner;

import com.cartographerapi.domain.Gamertag;
import com.cartographerapi.domain.playergames.PlayerGame;
import com.cartographerapi.domain.playergames.PlayerGamesDynamoReader;
import com.cartographerapi.domain.playergames.PlayerGamesReader;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Check the PlayerGames that are stored in the cache.
 * 
 * @author GodlyPerfection
 * 
 */
public class PlayerGamesGetter implements RequestStreamHandler, RequestHandler<Gamertag, List<PlayerGame>> {

	private PlayerGamesReader cacheReader;

	/**
	 * Get the PlayerGames that currently exist in the cache.
	 * 
	 * @param input The Gamertag sent in to execute the Lambda.
	 * @param context The Lambda execution context.
	 * @return The PlayerGames in the cache.
	 */
    @Override
    // TODO Accept JSON view names in order to determine what data to show.
    public List<PlayerGame> handleRequest(Gamertag input, Context context) {
		List<PlayerGame> games = cacheReader.getPlayerGamesByGamertag(input.getGamertag());

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

        Gamertag req = mapper.readValue(convertStreamToString(inputStream), Gamertag.class);
		List<PlayerGame> games = cacheReader.getPlayerGamesByGamertag(req.getGamertag());
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
    public PlayerGamesGetter() {
    	this(new PlayerGamesDynamoReader());
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public PlayerGamesGetter(PlayerGamesReader cacheReader) {
    	this.cacheReader = cacheReader;
    }

}
