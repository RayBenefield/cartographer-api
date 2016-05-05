package com.cartographerapi.domain.game;

import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;

import com.cartographerapi.domain.ConfigDynamoReader;
import com.cartographerapi.domain.ConfigReader;

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
	private ConfigReader configReader;
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
		this.mapper = new ObjectMapper();
		this.configReader = new ConfigDynamoReader();
		this.topicArn = configReader.getValue(topicArnKey);
        snsClient = new AmazonSNSClient();		                           
        snsClient.setRegion(Region.getRegion(Regions.US_WEST_2));
	}

}
