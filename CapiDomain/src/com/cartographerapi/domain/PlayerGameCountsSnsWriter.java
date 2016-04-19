package com.cartographerapi.domain;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.model.PublishRequest;

public class PlayerGameCountsSnsWriter implements PlayerGameCountsWriter {
	
	private String topicArn;
	private AmazonSNSClient snsClient;
	private ObjectMapper mapper;

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
	
	public PlayerGameCountsSnsWriter(String topicArn) {
		this.topicArn = topicArn;
        mapper = new ObjectMapper();
        snsClient = new AmazonSNSClient();		                           
        snsClient.setRegion(Region.getRegion(Regions.US_WEST_2));
	}

}