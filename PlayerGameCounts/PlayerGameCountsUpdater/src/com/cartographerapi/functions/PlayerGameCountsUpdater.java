package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.cartographerapi.domain.CapiUtils;
import com.cartographerapi.domain.ScheduledEvent;
import com.cartographerapi.domain.players.Player;
import com.cartographerapi.domain.players.PlayersQueueReader;
import com.cartographerapi.domain.players.PlayersSqsReader;
import com.cartographerapi.domain.playergamecounts.PlayerGameCounts;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsDynamoReader;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsDynamoWriter;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsHaloApiReader;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsReader;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsWriter;

import java.util.List;
import java.util.ArrayList;

/**
 * Refreshes the PlayerGameCounts for a given Player.
 * 
 * @author GodlyPerfection
 * 
 */
public class PlayerGameCountsUpdater implements RequestHandler<ScheduledEvent, PlayerGameCounts> {

    private PlayersQueueReader queueReader;
    private PlayerGameCountsReader cacheReader;
    private PlayerGameCountsWriter cacheWriter;
    private PlayerGameCountsReader sourceReader;
    
    /**
     * Checks the cache to see if PlayerGameCounts already exist. Then uses that
     * (if exists) to update the current game counts for a Player. Then saves
     * the refresh in the cache.
     * 
     * @param input The Player given as input to the Lambda function.
     * @param context The Lambda execution context.
     * @return The newly refreshed PlayerGameCounts for a Player.
     */
    @Override
    public PlayerGameCounts handleRequest(ScheduledEvent input, Context context) {
        CapiUtils.logObject(context, input, "Execution Time");

        List<Player> queuedPlayers = queueReader.getNumberOfPlayers(1);

        if (queuedPlayers.size() <= 0) {
            return null;
        }

        Player player = queuedPlayers.get(0);
        CapiUtils.logObject(context, player, "Player to update");
        PlayerGameCounts counts = cacheReader.getPlayerGameCountsByGamertag(player.getGamertag());
        
        if (counts == null) {
            counts = sourceReader.getPlayerGameCountsByGamertag(player.getGamertag());
        } else {
            counts = sourceReader.getPlayerGameCountsByPlayerGameCounts(counts);
        }

        if (counts != null) {
            cacheWriter.savePlayerGameCounts(counts);
            queueReader.processedPlayer(player);
            CapiUtils.logObject(context, counts, "The new PlayerGameCounts");
        }

        return counts;
    }
    
    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public PlayerGameCountsUpdater() {
        this(
            new PlayersSqsReader("QueueUrlPlayersForPlayerGameCounts"),
            new PlayerGameCountsDynamoReader(),
            new PlayerGameCountsDynamoWriter(),
            new PlayerGameCountsHaloApiReader()
        );
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public PlayerGameCountsUpdater(PlayersQueueReader queueReader, PlayerGameCountsReader cacheReader, PlayerGameCountsWriter cacheWriter, PlayerGameCountsReader sourceReader) {
        this.queueReader = queueReader;
        this.cacheReader = cacheReader;
        this.cacheWriter = cacheWriter;
        this.sourceReader = sourceReader;
    }
}
