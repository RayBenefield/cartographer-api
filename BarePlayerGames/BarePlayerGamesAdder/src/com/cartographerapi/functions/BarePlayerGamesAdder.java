package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.cartographerapi.domain.CapiUtils;
import com.cartographerapi.domain.ScheduledEvent;
import com.cartographerapi.domain.bareplayergames.BarePlayerGame;
import com.cartographerapi.domain.bareplayergames.BarePlayerGamesWriter;
import com.cartographerapi.domain.bareplayergames.BarePlayerGamesDynamoWriter;
import com.cartographerapi.domain.playergames.PlayerGame;
import com.cartographerapi.domain.playergames.PlayerGamesQueueReader;
import com.cartographerapi.domain.playergames.PlayerGamesSqsReader;

import java.util.List;
import java.util.ArrayList;

/**
 * Go through every player game in the queue and store the bare version of it.
 *
 * @author GodlyPerfection
 *
 */
public class BarePlayerGamesAdder implements RequestHandler<ScheduledEvent, List<BarePlayerGame>> {

    private PlayerGamesQueueReader queueReader;
    private BarePlayerGamesWriter cacheWriter;

    /**
     * Pulls player games from the queue and transforms them into BarePlayerGames to save in
     * the cache.
     *
     * @param input The Cloudwatch scheduled event that triggered this.
     * @param context The Lambda execution context.
     * @return The newly added BarePlayerGames.
     */
    @Override
    public List<BarePlayerGame> handleRequest(ScheduledEvent input, Context context) {
        CapiUtils.logObject(context, input, "ScheduledEvent Input");
        List<BarePlayerGame> results = new ArrayList<BarePlayerGame>();

        while (context.getRemainingTimeInMillis() > 30000) {
            // Pull games from the queue to inspect
            List<PlayerGame> games = queueReader.getNumberOfPlayerGames(10);

            if (games.size() <= 0) {
                break;
            }

            // Transform each game into a map game.
            for (PlayerGame game : games) {
                BarePlayerGame foundBarePlayerGame = new BarePlayerGame(game);
                cacheWriter.saveBarePlayerGame(foundBarePlayerGame);
                results.add(foundBarePlayerGame);
                queueReader.processedPlayerGame(game);
            }
        }

        CapiUtils.logObject(context, results.size(), "# of BarePlayerGames Added");
        return results;
    }

    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public BarePlayerGamesAdder() {
        this(
            new PlayerGamesSqsReader("QueueUrlPlayerGamesForBarePlayerGames"),
            new BarePlayerGamesDynamoWriter()
        );
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public BarePlayerGamesAdder(PlayerGamesQueueReader queueReader, BarePlayerGamesWriter cacheWriter) {
        this.queueReader = queueReader;
        this.cacheWriter = cacheWriter;
    }

}
