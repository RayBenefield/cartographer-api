package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import java.util.List;
import com.cartographerapi.domain.Gamertag;
import com.cartographerapi.domain.PlayerGame;
import com.cartographerapi.domain.PlayerGamesReader;
import com.cartographerapi.domain.PlayerGamesDynamoReader;
import java.io.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PlayerGamesGetter implements RequestStreamHandler, RequestHandler<Gamertag, List<PlayerGame>> {

	private PlayerGamesReader cacheReader;

    @Override
    public List<PlayerGame> handleRequest(Gamertag input, Context context) {
		List<PlayerGame> games = cacheReader.getPlayerGamesByGamertag(input.getGamertag());

		return games;
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();

        Gamertag req = mapper.readValue(convertStreamToString(inputStream), Gamertag.class);
		List<PlayerGame> games = cacheReader.getPlayerGamesByGamertag(req.getGamertag());
        outputStream.write(mapper.writeValueAsString(games).getBytes(java.nio.charset.Charset.forName("UTF-8")));
    }

    // TODO Properly move over this code to a handy service.
    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
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