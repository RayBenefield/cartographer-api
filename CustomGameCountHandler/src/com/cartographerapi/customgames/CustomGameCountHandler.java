package com.cartographerapi.customgames;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class CustomGameCountHandler implements RequestHandler<Gamertag, Integer> {
	
    @Override
    public Integer handleRequest(Gamertag input, Context context) {
        context.getLogger().log("Input: " + input.getGamertag());

        return input.getGamertag().length();
    }

}