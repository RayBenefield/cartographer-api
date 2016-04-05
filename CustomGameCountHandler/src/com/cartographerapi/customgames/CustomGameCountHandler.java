package com.cartographerapi.customgames;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class CustomGameCountHandler implements RequestHandler<Gamertag, PlayerGameCounts> {
	
    @Override
    public PlayerGameCounts handleRequest(Gamertag input, Context context) {
        context.getLogger().log("Input: " + input.getGamertag());
		ObjectMapper mapper = new ObjectMapper();
        
        try {
            AmazonDynamoDBClient client = new AmazonDynamoDBClient();
            client.setRegion(Region.getRegion(Regions.US_WEST_2));
            DynamoDBMapper dbMapper = new DynamoDBMapper(client);
			Halo5ApiWrapper api = new Halo5ApiWrapper("ae4df7c91357455ea30be2d7bdf15522");
			JsonNode root;
             
            PlayerGameCounts counts = dbMapper.load(PlayerGameCounts.class, input.getGamertag());

            Integer completedGames = 0;
			String totalResult = api.serviceRecord(input.getGamertag());
			root = mapper.readTree(totalResult);
			completedGames = root.path("Results").path(0).path("Result").path("CustomStats").path("TotalGamesCompleted").asInt();
			Integer totalGames = completedGames;
            if (counts != null) {
            	totalGames = counts.getTotalGames();
            }
			counts = new PlayerGameCounts(input.getGamertag(), completedGames, totalGames);

			Integer lastGames = 25;
			while (lastGames == 25) {
				String lastResult = api.customGames(input.getGamertag(), totalGames);
				root = mapper.readTree(lastResult);
				lastGames = root.path("ResultCount").asInt();
				totalGames += lastGames;
			}

			counts.setTotalGames(totalGames);
			dbMapper.save(counts);
			return counts;
        } catch (JsonGenerationException exception) {
        	return new PlayerGameCounts(input.getGamertag(), 0, 0);
        } catch (JsonMappingException exception) {
        	return new PlayerGameCounts(input.getGamertag(), 0, 0);
        } catch (IOException exception) {
        	return new PlayerGameCounts(input.getGamertag(), 0, 0);
        }

    }

}