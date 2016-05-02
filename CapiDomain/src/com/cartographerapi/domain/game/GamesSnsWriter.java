package com.cartographerapi.domain.game;

import java.util.List;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

/**
 * Writer repository for Games to an SNS topic.
 * 
 * @see GamesWriter
 * 
 * @author GodlyPerfection
 *
 */
public class GamesSnsWriter implements GamesWriter {
	
	private String topicArn;
	private AmazonSNSClient snsClient;
	private ObjectMapper mapper;

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Game> saveGames(List<Game> games) {
		for (Game game : games) {
			saveGame(game);
		}
		return games;
	}
	
    /**
     * The lazy IOC constructor.
     */
	public GamesSnsWriter(String topicArnKey) {
		mapper = new ObjectMapper();
		JsonNode config = mapper.createObjectNode();
		try {
			config = mapper.readTree(getClass().getClassLoader().getResource("config.json"));
		} catch (IOException exception) {
		}

		this.topicArn = config.path(topicArnKey).asText();
        snsClient = new AmazonSNSClient();		                           
        snsClient.setRegion(Region.getRegion(Regions.US_WEST_2));
	}

}