package com.cartographerapi.customgames;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.io.IOException;

public class CustomGameCountHandler implements RequestHandler<Gamertag, Integer> {
	
    @Override
    public Integer handleRequest(Gamertag input, Context context) {
        context.getLogger().log("Input: " + input.getGamertag());
        
        try {
			Halo5ApiWrapper api = new Halo5ApiWrapper("ae4df7c91357455ea30be2d7bdf15522");
			String result = api.serviceRecord(input.getGamertag());
			return result.length();
        } catch (IOException exception) {
        	return -1;
        }

    }

}