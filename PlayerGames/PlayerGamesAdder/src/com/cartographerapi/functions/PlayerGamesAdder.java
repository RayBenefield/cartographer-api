package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import com.cartographerapi.domain.CapiUtils;
import com.cartographerapi.domain.players.Player;
import com.cartographerapi.domain.ObjectSnsWriter;
import com.cartographerapi.domain.ObjectWriter;
import com.cartographerapi.domain.ScheduledEvent;
import com.cartographerapi.domain.playergamecounts.PlayerGameCounts;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsQueueReader;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsSnsWriter;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsSqsReader;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsWriter;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsWriter;
import com.cartographerapi.domain.playergames.PlayerGame;
import com.cartographerapi.domain.playergames.PlayerGamesDynamoWriter;
import com.cartographerapi.domain.playergames.PlayerGamesHaloApiReader;
import com.cartographerapi.domain.playergames.PlayerGamesWriter;
import com.cartographerapi.domain.playergamescheckpoints.PlayerGamesCheckpoint;
import com.cartographerapi.domain.playergamescheckpoints.PlayerGamesCheckpointDynamoReader;
import com.cartographerapi.domain.playergamescheckpoints.PlayerGamesCheckpointDynamoWriter;
import com.cartographerapi.domain.playergamescheckpoints.PlayerGamesCheckpointReader;
import com.cartographerapi.domain.playergamescheckpoints.PlayerGamesCheckpointWriter;

/**
 * Pull a batch of 24 games to be saved into our PlayerGames cache.
 * 
 * @author GodlyPerfection
 * 
 */
public class PlayerGamesAdder implements RequestHandler<ScheduledEvent, List<PlayerGame>> {
    
    private PlayerGameCountsQueueReader queueReader;
    private PlayerGamesHaloApiReader gameReader;
    private PlayerGamesWriter gameWriter;
    private PlayerGamesCheckpointReader checkpointReader;
    private PlayerGamesCheckpointWriter checkpointWriter;
    private PlayerGameCountsWriter continueWriter;
    private ObjectWriter updatedTotalGamesWriter;

    /**
     * Pull a batch of PlayerGames from the Halo API starting from a
     * PlayerGamesCheckpoint if one exists. If games are found then save them
     * and update the checkpoint. If we did not find the full batch of games
     * (results were maxed at 24), then we have more games to pull so published
     * to an SNS topic to trigger another batch pull.
     * 
     * @param input The SNS event that triggered this.
     * @param context The Lambda execution context.
     * @return The newly added PlayerGames.
     */
    @Override
    public List<PlayerGame> handleRequest(ScheduledEvent input, Context context) {
        CapiUtils.logObject(context, input, "ScheduledEvent Input");
        List<PlayerGame> results = new ArrayList<PlayerGame>();

        // Pull games from the queue to inspect
        List<PlayerGameCounts> queuedCounts = queueReader.getNumberOfPlayerGameCounts(1);

        if (queuedCounts.size() <= 0) {
            return results;
        }

        // Figure out who this is for.
        PlayerGameCounts counts = queuedCounts.get(0);
        String gamertag = counts.getGamertag();
        CapiUtils.logObject(context, counts, "PlayerGameCounts from SNSEvent");

        // If there is a checkpoint then start there and load up to the games possible.
        PlayerGamesCheckpoint checkpoint = checkpointReader.getPlayerGamesCheckpointWithDefault(gamertag);
        gameReader.setTotal(counts.getTotalGames());

        while (context.getRemainingTimeInMillis() > 30000) {
            CapiUtils.logObject(context, context.getRemainingTimeInMillis(), "Remaining milliseconds");
            CapiUtils.logObject(context, checkpoint, "PlayerGamesCheckpoint for " + gamertag);
            gameReader.setLastMatch(checkpoint.getLastMatch());

            results = gameReader.getPlayerGamesByGamertag(gamertag, checkpoint.getTotalGamesLoaded(), 25);

            if (results == null) {
                CapiUtils.logObject(context, results, "Results have come back null.");
                updatedTotalGamesWriter.saveObject(new Player(gamertag));
                queueReader.processedPlayerGameCounts(counts);
                return new ArrayList<PlayerGame>();
            }
            CapiUtils.logObject(context, results.size(), "# of PlayerGames from the Halo API");

            // If we found some results then save the found results and save the checkpoint.
            if (results.size() > 0) {
                gameWriter.savePlayerGames(results);
                checkpoint.setLastMatch(results.get(results.size() - 1).getMatchId());
                checkpoint.setTotalGamesLoaded(checkpoint.getTotalGamesLoaded() + results.size());
                checkpointWriter.savePlayerGamesCheckpoint(checkpoint);

                if (checkpoint.getTotalGamesLoaded().equals(counts.getTotalGames())) {
                    queueReader.processedPlayerGameCounts(counts);
                    break;
                }
            }
        }
        
        return results;
    }
    
    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public PlayerGamesAdder() {
        this(
            new PlayerGameCountsSqsReader("sqsCapiPlayerGameCountsForPlayerGames"),
            new PlayerGamesHaloApiReader(),
            new PlayerGamesDynamoWriter(),
            new PlayerGamesCheckpointDynamoReader(),
            new PlayerGamesCheckpointDynamoWriter(),
            new PlayerGameCountsSnsWriter("snsCapiPlayerGameCountsContinue"),
            new ObjectSnsWriter("snsCapiPlayerUpdatedTotalGames")
        );
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public PlayerGamesAdder(
        PlayerGameCountsQueueReader queueReader,
        PlayerGamesHaloApiReader gameReader,
        PlayerGamesWriter gameWriter,
        PlayerGamesCheckpointReader checkpointReader,
        PlayerGamesCheckpointWriter checkpointWriter,
        PlayerGameCountsWriter continueWriter,
        ObjectWriter updatedTotalGamesWriter
    ) {
        this.queueReader = queueReader;
        this.gameReader = gameReader;
        this.gameWriter = gameWriter;
        this.checkpointReader = checkpointReader;
        this.checkpointWriter = checkpointWriter;
        this.continueWriter = continueWriter;
        this.updatedTotalGamesWriter = updatedTotalGamesWriter;
    }

}
