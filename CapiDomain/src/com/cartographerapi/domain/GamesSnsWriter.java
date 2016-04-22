package com.cartographerapi.domain;

import java.util.List;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.model.PublishRequest;

public class GamesSnsWriter implements GamesWriter {
	
	private String topicArn;
	private AmazonSNSClient snsClient;
	private ObjectMapper mapper;

	@Override
	public Game saveGame(Game game) {
		// TODO clean up try catch
		try {
			PublishRequest publishRequest;
			publishRequest = new PublishRequest(topicArn, mapper.writeValueAsString(game));
			snsClient.publish(publishRequest);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return game;
	}

	@Override
	public List<Game> saveGames(List<Game> games) {
		for (Game game : games) {
			saveGame(game);
		}
		return games;
	}
	
	public GamesSnsWriter(String topicArn) {
		this.topicArn = topicArn;
        mapper = new ObjectMapper();
        snsClient = new AmazonSNSClient();		                           
        snsClient.setRegion(Region.getRegion(Regions.US_WEST_2));
	}

}
