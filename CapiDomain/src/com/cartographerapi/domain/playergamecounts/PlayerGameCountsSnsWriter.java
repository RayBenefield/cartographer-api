package com.cartographerapi.domain.playergamecounts;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

import java.io.IOException;

/**
 * Writer repository interface for PlayerGameCounts. This provides access to a
 * data source that contains the PlayerGameCounts object.
 * 
 * @author GodlyPerfection
 *
 */
public class PlayerGameCountsSnsWriter implements PlayerGameCountsWriter {
	
	private String topicArn;
	private AmazonSNSClient snsClient;
	private ObjectMapper mapper;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerGameCounts savePlayerGameCounts(PlayerGameCounts counts) {
		try {
			PublishRequest publishRequest;
			publishRequest = new PublishRequest(topicArn, mapper.writeValueAsString(counts));
			snsClient.publish(publishRequest);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return counts;
	}
	
    /**
     * The lazy IOC constructor.
     */
	public PlayerGameCountsSnsWriter(String topicArnKey) {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode config = mapper.createObjectNode();
		try {
			config = mapper.readTree(getClass().getClassLoader().getResource("config.json"));
		} catch (IOException exception) {
		}

		this.topicArn = config.path(topicArnKey).asText();
        mapper = new ObjectMapper();
        snsClient = new AmazonSNSClient();		                           
        snsClient.setRegion(Region.getRegion(Regions.US_WEST_2));
	}

}
