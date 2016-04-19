package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;
import com.cartographerapi.domain.PlayerGame;
import com.cartographerapi.domain.PlayerGamesWriter;
import com.cartographerapi.domain.PlayerGamesSnsWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class PlayerGamesPublisher implements RequestHandler<DynamodbEvent, List<PlayerGame>> {

	private PlayerGamesWriter newWriter;

    @Override
    public List<PlayerGame> handleRequest(DynamodbEvent input, Context context) {
        context.getLogger().log("Input: " + input);
        List<PlayerGame> results = new ArrayList<PlayerGame>();

		for (DynamodbStreamRecord record : input.getRecords()) {
			Map<String, AttributeValue> newData = record.getDynamodb().getNewImage();
			Map<String, AttributeValue> oldData = record.getDynamodb().getOldImage();
			if (newData == null) continue; // Ignore deletes for now;
			if (oldData != null) continue; // Ignore updates for now;

			Item newItem = Item.fromMap(InternalUtils.toSimpleMapValue(newData));
			PlayerGame game = new PlayerGame(newItem);
			results.add(game);
		}
		newWriter.savePlayerGames(results);

        return results;
    }
    
    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public PlayerGamesPublisher() {
    	this(
			new PlayerGamesSnsWriter("arn:aws:sns:us-west-2:789201490085:capi-playergames-new")
		);
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public PlayerGamesPublisher(PlayerGamesWriter newWriter) {
    	this.newWriter = newWriter;
    }

}