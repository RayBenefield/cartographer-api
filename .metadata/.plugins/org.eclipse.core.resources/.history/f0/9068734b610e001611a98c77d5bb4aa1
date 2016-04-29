package com.cartographerapi.domain;

import java.util.List;

import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.AmazonSQSClient;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/**
 * Writer repository for Objects into an SQS queue.
 * 
 * @see ObjectWriter
 * 
 * @author GodlyPerfection
 *
 */
public class ObjectSqsWriter implements ObjectWriter {

	private AmazonSQSClient client;
	private String queueUrl;
    private ObjectMapper mapper;

	/**
	 * {@inheritDoc}
	 */
    @Override
	public Object saveObject(Object object) {
		try {
			SendMessageRequest sendMessageRequest = new SendMessageRequest()
				.withQueueUrl(queueUrl)
				.withMessageBody(mapper.writeValueAsString(new SqsMessage(object)));
			client.sendMessage(sendMessageRequest);
		} catch (IOException exception) {
		}
		return object;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	// TODO Setup better batching based on item size... limit is 256K per batch
	public List<Object> saveObjects(List<Object> objects) {
		for (Object object : objects) {
			saveObject(object);
		}
		return objects;
	}

    /**
     * The lazy IOC constructor.
     */
	public ObjectSqsWriter(String queueUrlKey) {
		mapper = new ObjectMapper();
		JsonNode config = mapper.createObjectNode();
		try {
			config = mapper.readTree(getClass().getClassLoader().getResource("config.json"));
		} catch (IOException exception) {
		}

		this.queueUrl = config.path(queueUrlKey).asText();
		client = new AmazonSQSClient();
		client.setRegion(Region.getRegion(Regions.US_WEST_2));
	}

}
