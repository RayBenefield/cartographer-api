package com.cartographerapi.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshaller;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class JsonNodeMarshaller implements DynamoDBMarshaller<JsonNode> {

	private ObjectMapper mapper;

	@Override
	public String marshall(JsonNode getterReturnResult) {
		return getterReturnResult.toString();
	}

	@Override
	public JsonNode unmarshall(Class<JsonNode> clazz, String obj) {
		try {
			return mapper.readTree(obj);
		} catch (IOException exception) {
			return mapper.createObjectNode();
		}
	}
	
	public JsonNodeMarshaller() {
		this.mapper = new ObjectMapper();
	}
}