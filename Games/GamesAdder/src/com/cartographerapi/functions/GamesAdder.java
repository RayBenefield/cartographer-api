package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.cartographerapi.domain.CapiUtils;
import com.cartographerapi.domain.ScheduledEvent;
import com.cartographerapi.domain.game.Game;
import com.cartographerapi.domain.game.MatchId;
import com.cartographerapi.domain.game.GamesDynamoReader;
import com.cartographerapi.domain.game.GamesHaloApiReader;
import com.cartographerapi.domain.game.GamesReader;
import com.cartographerapi.domain.game.GamesWriter;
import com.cartographerapi.domain.game.GamesDynamoWriter;
import com.cartographerapi.domain.playergames.PlayerGame;
import com.cartographerapi.domain.playergames.PlayerGamesQueueReader;
import com.cartographerapi.domain.playergames.PlayerGamesSqsReader;

import java.util.List;
import java.util.ArrayList;

/**
 * Check the new PlayerGames that could have Games not loaded in our cache.
 *
 * @author GodlyPerfection
 *
 */
public class GamesAdder implements RequestHandler<ScheduledEvent, List<Game>> {

    private PlayerGamesQueueReader queueReader;
    private GamesReader cacheReader;
    private GamesReader sourceReader;
    private GamesWriter cacheWriter;

    /**
     * Pulls a number of queue payloads and then for each it checks the cache to
     * see if the Game in the payload already exist. If not then it finds the
     * game from the HaloAPI and adds it to the cache.
     *
     * @param input The Cloudwatch scheduled event that triggered this.
     * @param context The Lambda execution context.
     * @return The newly added Games.
     */
    @Override
    public List<Game> handleRequest(ScheduledEvent input, Context context) {
        CapiUtils.logObject(context, input, "ScheduledEvent Input");
        List<Game> results = new ArrayList<Game>();

        while (context.getRemainingTimeInMillis() > 30000) {
            // Pull games from the queue to inspect
            List<PlayerGame> games = queueReader.getNumberOfPlayerGames(10);

            if (games.size() <= 0) {
                break;
            }

            // For each game, check the cache and if it isn't there find it and save it
            for (PlayerGame game : games) {
                Game cachedGame = cacheReader.getGameByMatchId(new MatchId(game.getMatchId()));
                if (cachedGame != null) {
                    queueReader.processedPlayerGame(game);
                    continue;
                }

                Game foundGame = null;
                foundGame = sourceReader.getGameByMatchId(new MatchId(game.getMatchId()));

                if (foundGame != null) {
                    cacheWriter.saveGame(foundGame);
                    queueReader.processedPlayerGame(game);
                    results.add(foundGame);
                }
            }
        }

        CapiUtils.logObject(context, results.size(), "# of GamesAdded");
        return results;
    }

    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public GamesAdder() {
        this(
            new GamesDynamoReader(),
            new GamesHaloApiReader(),
            new GamesDynamoWriter(),
            new PlayerGamesSqsReader("QueueUrlPlayerGamesForGames")
        );
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public GamesAdder(GamesReader cacheReader, GamesReader sourceReader, GamesWriter cacheWriter, PlayerGamesQueueReader queueReader) {
        this.cacheReader = cacheReader;
        this.sourceReader = sourceReader;
        this.cacheWriter = cacheWriter;
        this.queueReader = queueReader;
    }

}
