package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.cartographerapi.domain.PlayerGame;
import java.util.List;
import java.util.ArrayList;

public class PlayerGamesPublisher implements RequestHandler<DynamodbEvent, List<PlayerGame>> {

    @Override
    public List<PlayerGame> handleRequest(DynamodbEvent input, Context context) {
        context.getLogger().log("Input: " + input);
        List<PlayerGame> results = new ArrayList<PlayerGame>();

        return results;
    }

}
