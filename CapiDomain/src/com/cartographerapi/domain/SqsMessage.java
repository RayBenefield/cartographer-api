package com.cartographerapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/**
 * Wrapper object to properly format a domain object as if it was from an SNS
 * publish.
 * 
 * @author GodlyPerfection
 *
 */
public class SqsMessage {
	
	private String message;
	
	@JsonProperty("Message")
	public String getMessage() {
		return message;
	}
	
	public void setMessage(Object message) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			this.message = mapper.writeValueAsString(message);
		} catch (IOException execption) {
		}
	}
	
	public SqsMessage(Object message) {
		setMessage(message);
	}

}
