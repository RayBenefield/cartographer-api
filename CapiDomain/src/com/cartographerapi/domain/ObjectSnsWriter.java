package com.cartographerapi.domain;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

import java.io.IOException;

public class ObjectSnsWriter implements ObjectWriter {
	
	private String topicArn;
	private AmazonSNSClient snsClient;
	private ConfigReader configReader;
	private ObjectMapper mapper;

	@Override
	public Object saveObject(Object object) {
		try {
			PublishRequest publishRequest;
			publishRequest = new PublishRequest(topicArn, mapper.writeValueAsString(object));
			snsClient.publish(publishRequest);
		} catch (IOException e) {
		}
		return object;
	}

	@Override
	public List<Object> saveObjects(List<Object> objects) {
		for (Object object : objects) {
			saveObject(object);
		}

		return objects;
	}

    /**
     * The lazy IOC constructor.
     */
	public ObjectSnsWriter(String topicArnKey) {
		this.mapper = new ObjectMapper();
		this.configReader = new ConfigDynamoReader();
		this.topicArn = configReader.getValue(topicArnKey);
        snsClient = new AmazonSNSClient();		                           
        snsClient.setRegion(Region.getRegion(Regions.US_WEST_2));
	}

}
