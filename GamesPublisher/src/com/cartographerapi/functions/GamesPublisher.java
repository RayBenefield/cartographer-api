package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;
import com.cartographerapi.domain.game.Game;
import com.cartographerapi.domain.game.GamesSnsWriter;
import com.cartographerapi.domain.game.GamesWriter;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;

public class GamesPublisher implements RequestHandler<DynamodbEvent, List<Game>> {

	private GamesWriter newWriter;

    @Override
    public List<Game> handleRequest(DynamodbEvent input, Context context) {
        context.getLogger().log("Input: " + input);
        List<Game> results = new ArrayList<Game>();

		for (DynamodbStreamRecord record : input.getRecords()) {
			Map<String, AttributeValue> newData = record.getDynamodb().getNewImage();
			Map<String, AttributeValue> oldData = record.getDynamodb().getOldImage();
			if (newData == null) continue; // Ignore deletes for now;
			if (oldData != null) continue; // Ignore updates for now;

			Item newItem = Item.fromMap(InternalUtils.toSimpleMapValue(newData));
			Game game = new Game(newItem);
			results.add(game);
		}
		newWriter.saveGames(results);

        return results;
    }
    
    /**
     * The lazy IOC constructor for Lambda to instantiate.
     */
    public GamesPublisher() {
    	this(
			new GamesSnsWriter("arn:aws:sns:us-west-2:789201490085:capi-games-new")
		);
    }

    /**
     * The real constructor that supports dependency injection.
     */
    public GamesPublisher(GamesWriter newWriter) {
    	this.newWriter = newWriter;
    }

}
