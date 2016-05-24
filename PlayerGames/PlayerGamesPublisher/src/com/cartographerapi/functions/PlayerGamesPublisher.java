package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;

import com.cartographerapi.domain.CapiUtils;
import com.cartographerapi.domain.playergames.PlayerGame;
import com.cartographerapi.domain.playergames.PlayerGamesSnsWriter;
import com.cartographerapi.domain.playergames.PlayerGamesWriter;

import com.amazonaws.services.dynamodbv2.document.Item;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Publish all new PlayerGames to an SNS topic.
 * 
 * @author GodlyPerfection
 * 
 */
public class PlayerGamesPublisher implements RequestHandler<DynamodbEvent, List<PlayerGame>> {

	private PlayerGamesWriter newWriter;

	/**
	 * For every new PlayerGame added to DynamoDB publish that game to the
	 * respective SNS topic.
	 * 
	 * @param input The Dynamo event that triggered this.
	 * @param context The Lambda execution context.
	 * @return The published PlayerGames.
	 */
    @Override
    public List<PlayerGame> handleRequest(DynamodbEvent input, Context context) {
		CapiUtils.logObject(context, input, "DynamodbEvent Input");
        List<PlayerGame> results = new ArrayList<PlayerGame>();

        Map<String, List<Map<String, Item>>> itemRecords = CapiUtils.sortRecordsFromDynamoEvent(input);

        // For each inserted record, publish to the new SNS topic.
        for (Map<String, Item> insertedRecord : itemRecords.get("inserted")) {
			PlayerGame game = new PlayerGame(insertedRecord.get("new"));
			results.add(game);
        }
		newWriter.savePlayerGames(results);

		CapiUtils.logObject(context, results.size(), "# of PlayerGames Published");
        return results;
    }
    
    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public PlayerGamesPublisher() {
    	this(
			new PlayerGamesSnsWriter("TopicArnNewPlayerGames")
		);
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public PlayerGamesPublisher(PlayerGamesWriter newWriter) {
    	this.newWriter = newWriter;
    }

}
