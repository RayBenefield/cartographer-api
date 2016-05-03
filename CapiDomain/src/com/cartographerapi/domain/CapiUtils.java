package com.cartographerapi.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.amazonaws.services.lambda.runtime.Context;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;

/**
 * Utilities to clean up code for CAPI.
 * 
 * @author GodlyPerfection
 *
 */
public class CapiUtils {

	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * Pretty log an object with a Lambda context.
	 * 
	 * @param context The context the Lambda Function is run in.
	 * @param object The object to be logged.
	 */
	public static void logObject(Context context, Object object) {
		CapiUtils.logObject(context, object, object.getClass().toGenericString());
	}

	/**
	 * Pretty log an object with a Lambda context including an entry with a contextual string.
	 * 
	 * @param context The context the Lambda Function is run in.
	 * @param object The object to be logged.
	 * @param contextualString A descriptive string to be printed before the object.
	 */
	public static void logObject(Context context, Object object, String contextualString) {
		try {
			context.getLogger().log("<<<<< " + contextualString + " >>>>>");
			context.getLogger().log(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object));
		} catch (IOException exception) {
			context.getLogger().log("Object [" + object.toString() + "] can't be parsed.");
		}
	}
	
	/**
	 * Pull out a HashMap from an SNSEvent.
	 * 
	 * @param event The event to pull the HashMap out of.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getObjectFromSnsEvent(SNSEvent event) {
		try {
			return mapper.readValue(event.getRecords().get(0).getSNS().getMessage(), HashMap.class);
		} catch (IOException exception) {
			return null;
		}
	}

}
