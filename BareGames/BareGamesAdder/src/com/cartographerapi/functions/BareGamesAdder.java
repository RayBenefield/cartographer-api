package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.cartographerapi.domain.CapiUtils;
import com.cartographerapi.domain.ScheduledEvent;
import com.cartographerapi.domain.baregames.BareGame;
import com.cartographerapi.domain.baregames.BareGamesWriter;
import com.cartographerapi.domain.baregames.BareGamesDynamoWriter;
import com.cartographerapi.domain.game.Game;
import com.cartographerapi.domain.game.GamesQueueReader;
import com.cartographerapi.domain.game.GamesSqsReader;

import java.util.List;
import java.util.ArrayList;

/**
 * Go through every player game in the queue and store the bare version of it.
 *
 * @author GodlyPerfection
 *
 */
public class BareGamesAdder implements RequestHandler<ScheduledEvent, List<BareGame>> {

    private GamesQueueReader queueReader;
    private BareGamesWriter cacheWriter;

    /**
     * Pulls player games from the queue and transforms them into BareGames to save in
     * the cache.
     *
     * @param input The Cloudwatch scheduled event that triggered this.
     * @param context The Lambda execution context.
     * @return The newly added BareGames.
     */
    @Override
    public List<BareGame> handleRequest(ScheduledEvent input, Context context) {
        CapiUtils.logObject(context, input, "ScheduledEvent Input");
        List<BareGame> results = new ArrayList<BareGame>();

        while (context.getRemainingTimeInMillis() > 30000) {
            // Pull games from the queue to inspect
            List<Game> games = queueReader.getNumberOfGames(10);

            if (games.size() <= 0) {
                break;
            }

            // Transform each game into a map game.
            for (Game game : games) {
                BareGame foundBareGame = new BareGame(game);
                cacheWriter.saveBareGame(foundBareGame);
                results.add(foundBareGame);
                queueReader.processedGame(game);
            }
        }

        CapiUtils.logObject(context, results.size(), "# of BareGames Added");
        return results;
    }

    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public BareGamesAdder() {
        this(
            new GamesSqsReader("QueueUrlGamesForBareGames"),
            new BareGamesDynamoWriter()
        );
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public BareGamesAdder(GamesQueueReader queueReader, BareGamesWriter cacheWriter) {
        this.queueReader = queueReader;
        this.cacheWriter = cacheWriter;
    }

}
