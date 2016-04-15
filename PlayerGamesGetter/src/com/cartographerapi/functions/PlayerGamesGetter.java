package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.List;
import com.cartographerapi.domain.Gamertag;
import com.cartographerapi.domain.PlayerGame;
import com.cartographerapi.domain.PlayerGamesReader;
import com.cartographerapi.domain.PlayerGamesDynamoReader;

public class PlayerGamesGetter implements RequestHandler<Gamertag, List<PlayerGame>> {

	private PlayerGamesReader cacheReader;

    @Override
    public List<PlayerGame> handleRequest(Gamertag input, Context context) {
		List<PlayerGame> games = cacheReader.getPlayerGamesByGamertag(input.getGamertag());

		return games;
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
