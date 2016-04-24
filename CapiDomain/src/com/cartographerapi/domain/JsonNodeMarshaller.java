package com.cartographerapi.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshaller;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/**
 * Converts a JsonNode to a String and vice versa for DynamoDB storage.
 * 
 * @author GodlyPerfection
 *
 */
public class JsonNodeMarshaller implements DynamoDBMarshaller<JsonNode> {

	private ObjectMapper mapper;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String marshall(JsonNode getterReturnResult) {
		return getterReturnResult.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JsonNode unmarshall(Class<JsonNode> clazz, String obj) {
		try {
			return mapper.readTree(obj);
		} catch (IOException exception) {
			return mapper.createObjectNode();
		}
	}
	
    /**
     * The lazy IOC constructor.
     */
	public JsonNodeMarshaller() {
		this.mapper = new ObjectMapper();
	}
}
