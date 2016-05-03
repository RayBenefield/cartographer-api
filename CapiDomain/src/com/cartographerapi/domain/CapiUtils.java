package com.cartographerapi.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.amazonaws.services.lambda.runtime.Context;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;

public class CapiUtils {

	private static ObjectMapper mapper = new ObjectMapper();

	public static void logObject(Context context, Object object) {
		try {
			context.getLogger().log(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object));
		} catch (IOException exception) {
			context.getLogger().log("Object [" + object.toString() + "] can't be parsed.");
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getObjectFromSnsEvent(SNSEvent event) {
		try {
			return mapper.readValue(event.getRecords().get(0).getSNS().getMessage(), HashMap.class);
		} catch (IOException exception) {
			return null;
		}
	}

}
