package com.cartographerapi.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.cartographerapi.domain.PlayerGame;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.DeleteTopicRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cartographerapi.domain.PlayerGameCounts;
import java.io.IOException;

public class PlayerGamesAdder implements RequestHandler<SNSEvent, List<PlayerGame>> {

	@SuppressWarnings("unchecked")
    @Override
    public List<PlayerGame> handleRequest(SNSEvent input, Context context) {
        context.getLogger().log("Input: " + input);
        List<PlayerGame> results = new ArrayList<PlayerGame>();
        ObjectMapper mapper = new ObjectMapper();
        
		String message = input.getRecords().get(0).getSNS().getMessage();
		PlayerGameCounts counts;
		Map<String, Object> countsMap = new HashMap<String, Object>();
		try {
			countsMap = mapper.readValue(message, HashMap.class);
			counts = new PlayerGameCounts(countsMap);
		} catch (IOException exception) {
			counts = new PlayerGameCounts("Fake");
		}
		
		results.add(new PlayerGame(counts.getGamertag(), counts.getTotalGames(), message));
        
        return results;
    }

}
