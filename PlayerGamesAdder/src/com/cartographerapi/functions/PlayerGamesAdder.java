package com.cartographerapi.functions;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
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
import com.cartographerapi.domain.PlayerGamesCheckpoint;
import com.cartographerapi.domain.PlayerGamesHaloApiReader;
import java.io.IOException;
import java.util.Date;

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
		
		AmazonDynamoDBClient client;
		DynamoDBMapper dbMapper;
		client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(Regions.US_WEST_2));
		dbMapper = new DynamoDBMapper(client);
		
		PlayerGamesCheckpoint checkpoint = dbMapper.load(PlayerGamesCheckpoint.class, counts.getGamertag());
		
		PlayerGamesHaloApiReader gameReader = new PlayerGamesHaloApiReader();
		
		if (checkpoint == null) {
			results.get(0).setGamertag("No Checkpoint");
			gameReader.setTotal(counts.getTotalGames());
			results = gameReader.getPlayerGamesByGamertag(counts.getGamertag(), 0, 25);
			dbMapper.batchSave(results);
			checkpoint = new PlayerGamesCheckpoint(counts.getGamertag(), results.size(), results.get(results.size() - 1).getMatchId());
			dbMapper.save(checkpoint);
		} else {
			gameReader.setTotal(counts.getTotalGames());
			gameReader.setLastMatch(checkpoint.getLastMatch());
			results = gameReader.getPlayerGamesByGamertag(counts.getGamertag(), checkpoint.getTotalGamesLoaded(), 25);
			if (results.size() > 0) {
				dbMapper.batchSave(results);
				checkpoint.setLastMatch(results.get(results.size() - 1).getMatchId());
				checkpoint.setTotalGamesLoaded(checkpoint.getTotalGamesLoaded() + results.size());
				checkpoint.setLastUpdated(new Date());
				dbMapper.save(checkpoint);

			}
		}

		if (results.size() == 24) {
			String topicArn = "arn:aws:sns:us-west-2:789201490085:capi-playergamescontinue";
			AmazonSNSClient snsClient = new AmazonSNSClient();		                           
			snsClient.setRegion(Region.getRegion(Regions.US_WEST_2));

			try {
				PublishRequest publishRequest;
				publishRequest = new PublishRequest(topicArn, mapper.writeValueAsString(counts));
				snsClient.publish(publishRequest);
			} catch (IOException exception) {
				results.add(new PlayerGame("fake", 50, ""));
			}
		}
        
        return results;
    }

}
