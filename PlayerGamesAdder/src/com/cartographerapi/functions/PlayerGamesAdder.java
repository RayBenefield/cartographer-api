package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;

import com.cartographerapi.domain.playergamecounts.PlayerGameCounts;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsSnsWriter;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import java.io.IOException;

/**
 * Pull a batch of 24 games to be saved into our PlayerGames cache.
 * 
 * @author GodlyPerfection
 * 
 */
public class PlayerGamesAdder implements RequestHandler<SNSEvent, List<PlayerGame>> {
	
	private PlayerGamesHaloApiReader gameReader;
	private PlayerGamesWriter gameWriter;
	private PlayerGamesCheckpointReader checkpointReader;
	private PlayerGamesCheckpointWriter checkpointWriter;
	private PlayerGameCountsWriter continueWriter;

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
	@SuppressWarnings("unchecked")
    @Override
    public List<PlayerGame> handleRequest(SNSEvent input, Context context) {
        context.getLogger().log("Input: " + input);
        List<PlayerGame> results = new ArrayList<PlayerGame>();
        ObjectMapper mapper = new ObjectMapper();
        
        // Figure out who this is for.
		PlayerGameCounts counts;
		try {
			Map<String, Object> countsMap = mapper.readValue(input.getRecords().get(0).getSNS().getMessage(), HashMap.class);
			counts = new PlayerGameCounts(countsMap);
		} catch (IOException exception) {
			return results;
		}
		
		String gamertag = counts.getGamertag();
		PlayerGamesCheckpoint checkpoint = checkpointReader.getPlayerGamesCheckpointWithDefault(gamertag);
		
		// If there is a checkpoint then start there and load up to the games possible.
		if (checkpoint.getLastMatch().equals("")) {
			gameReader.setLastMatch(checkpoint.getLastMatch());
		}
		gameReader.setTotal(counts.getTotalGames());
		results = gameReader.getPlayerGamesByGamertag(gamertag, checkpoint.getTotalGamesLoaded(), 25);

		// If we found some results then save the found results and save the checkpoint.
		if (results.size() > 0) {
			gameWriter.savePlayerGames(results);
			checkpoint.setLastMatch(results.get(results.size() - 1).getMatchId());
			checkpoint.setTotalGamesLoaded(checkpoint.getTotalGamesLoaded() + results.size());
			checkpointWriter.savePlayerGamesCheckpoint(checkpoint);
		}

		// If we found the max number of games possible, then we need to continue.
		if (results.size() == 24) {
			continueWriter.savePlayerGameCounts(counts);
		}
        
        return results;
    }
    
    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public PlayerGamesAdder() {
    	this(
    		new PlayerGamesHaloApiReader(),
    		new PlayerGamesDynamoWriter(),
    		new PlayerGamesCheckpointDynamoReader(),
    		new PlayerGamesCheckpointDynamoWriter(),
			new PlayerGameCountsSnsWriter("capiPlayerGameCountsContinue")
		);
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public PlayerGamesAdder(
    	PlayerGamesHaloApiReader gameReader,
    	PlayerGamesWriter gameWriter,
    	PlayerGamesCheckpointReader checkpointReader,
    	PlayerGamesCheckpointWriter checkpointWriter,
    	PlayerGameCountsWriter continueWriter
	) {
    	this.gameReader = gameReader;
    	this.gameWriter = gameWriter;
    	this.checkpointReader = checkpointReader;
    	this.checkpointWriter = checkpointWriter;
    	this.continueWriter = continueWriter;
    }

}
