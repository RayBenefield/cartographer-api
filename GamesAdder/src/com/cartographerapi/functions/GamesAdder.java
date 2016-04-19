package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.cartographerapi.domain.Game;

public class GamesAdder implements RequestHandler<SNSEvent, Game> {

    @Override
    public Game handleRequest(SNSEvent input, Context context) {
        context.getLogger().log("Input: " + input);

        // TODO: implement your handler
        return null;
    }

}