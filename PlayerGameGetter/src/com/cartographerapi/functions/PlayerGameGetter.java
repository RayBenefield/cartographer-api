package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.List;

public class PlayerGameGetter implements RequestHandler<Gamertag, List<PlayerGames>> {

    @Override
    public List<PlayerGames> handleRequest(Gamertag input, Context context) {
        context.getLogger().log("Input: " + input);

        // TODO: implement your handler
        return null;
    }

}
