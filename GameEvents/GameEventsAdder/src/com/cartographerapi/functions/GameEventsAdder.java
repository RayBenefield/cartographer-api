package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.cartographerapi.domain.CapiUtils;
import com.cartographerapi.domain.ScheduledEvent;
import com.cartographerapi.domain.gameevents.GameEvents;
import com.cartographerapi.domain.gameevents.GameEventsWriter;
import com.cartographerapi.domain.gameevents.GameEventsReader;
import com.cartographerapi.domain.gameevents.GameEventsDynamoReader;
import com.cartographerapi.domain.gameevents.GameEventsHaloApiReader;
import com.cartographerapi.domain.gameevents.GameEventsDynamoWriter;
import com.cartographerapi.domain.game.MatchId;
import com.cartographerapi.domain.game.Game;
import com.cartographerapi.domain.game.GamesQueueReader;
import com.cartographerapi.domain.game.GamesSqsReader;

import java.util.List;
import java.util.ArrayList;

/**
 * Go through every game in the queue and store the Events for that Game.
 *
 * @author GodlyPerfection
 *
 */
public class GameEventsAdder implements RequestHandler<ScheduledEvent, List<GameEvents>> {

    private GamesQueueReader queueReader;
    private GameEventsReader sourceReader;
    private GameEventsReader cacheReader;
    private GameEventsWriter cacheWriter;

    /**
     * Pulls games from the queue and transforms them into GameEvents to save in
     * the cache.
     *
     * @param input The Cloudwatch scheduled event that triggered this.
     * @param context The Lambda execution context.
     * @return The newly added GameEvents.
     */
    @Override
    public List<GameEvents> handleRequest(ScheduledEvent input, Context context) {
        CapiUtils.logObject(context, input, "ScheduledEvent Input");
        List<GameEvents> results = new ArrayList<GameEvents>();

        while (context.getRemainingTimeInMillis() > 30000) {
            // Pull games from the queue to inspect
            List<Game> games = queueReader.getNumberOfGames(10);

            if (games.size() <= 0) {
                break;
            }

            // Transform each game into a map game.
            for (Game game : games) {
                GameEvents cachedGameEvents = cacheReader.getGameEventsByMatchId(new MatchId(game.getMatchId()));
                if (cachedGameEvents != null) {
                    queueReader.processedGame(game);
                    continue;
                }

                GameEvents foundEvents = null;
                foundEvents = sourceReader.getGameEventsByMatchId(new MatchId(game.getMatchId()));

                if (foundEvents != null) {
                    cacheWriter.saveGameEvents(foundEvents);
                    queueReader.processedGame(game);
                    results.add(foundEvents);
                }
            }
        }

        CapiUtils.logObject(context, results.size(), "# of GameEvents Added");
        return results;
    }

    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public GameEventsAdder() {
        this(
            new GamesSqsReader("QueueUrlGamesForGameEvents"),
            new GameEventsHaloApiReader(),
            new GameEventsDynamoReader(),
            new GameEventsDynamoWriter()
        );
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public GameEventsAdder(GamesQueueReader queueReader, GameEventsReader sourceReader, GameEventsReader cacheReader, GameEventsWriter cacheWriter) {
        this.queueReader = queueReader;
        this.sourceReader = sourceReader;
        this.cacheReader = cacheReader;
        this.cacheWriter = cacheWriter;
    }

}
