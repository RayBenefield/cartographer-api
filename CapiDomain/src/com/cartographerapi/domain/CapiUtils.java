package com.cartographerapi.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import java.io.IOException;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;
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
     * Pretty log an object with System.out.
     *
     * @param object The object to be logged.
     */
    public static void logObject(Object object) {
        CapiUtils.logObject(object, object.getClass().toGenericString());
    }

    /**
     * Pretty log an object with System.out, including an entry with a contextual string.
     *
     * @param object The object to be logged.
     * @param contextualString A descriptive string to be printed before the object.
     */
    public static void logObject(Object object, String contextualString) {
        try {
            System.out.println("<<<<< " + contextualString + " >>>>>");
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object));
        } catch (IOException exception) {
            System.out.println("Object [" + object.toString() + "] can't be parsed.");
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

    /**
     * Grab sorted out item records from a DynamoDB event.
     *
     * @param event The event that has the item change records.
     * @return
     */
    public static Map<String, List<Map<String, Item>>> sortRecordsFromDynamoEvent(DynamodbEvent event) {
        Map<String, List<Map<String, Item>>> results = new HashMap<String, List<Map<String, Item>>>();
        List<Map<String, Item>> insertedRecords = new ArrayList<Map<String, Item>>();
        List<Map<String, Item>> updatedRecords = new ArrayList<Map<String, Item>>();
        List<Map<String, Item>> deletedRecords = new ArrayList<Map<String, Item>>();

        // For each Record, let's make a item record and add to the respective list.
        for (DynamodbStreamRecord record : event.getRecords()) {
            Map<String, AttributeValue> newData = record.getDynamodb().getNewImage();
            Map<String, AttributeValue> oldData = record.getDynamodb().getOldImage();

            Item newItem = null;
            Item oldItem = null;

            // If the data isn't null then make it into an Item.
            if (newData != null) {
                newItem = Item.fromMap(InternalUtils.toSimpleMapValue(newData));
            }
            if (oldData != null) {
                oldItem = Item.fromMap(InternalUtils.toSimpleMapValue(oldData));
            }

            // Create the item record.
            Map<String, Item> itemRecord = new HashMap<String, Item>();
            itemRecord.put("new", newItem);
            itemRecord.put("old", oldItem);

            // If there is no new data then this is a delete.
            if (newData == null) {
                deletedRecords.add(itemRecord);
                continue;
            }

            // If there is new data, but no old data, then this is an insert.
            if (oldData == null) {
                insertedRecords.add(itemRecord);
                continue;
            }

            // If there is new and old data then this is an update.
            updatedRecords.add(itemRecord);
        }

        // Consolidate the lists.
        results.put("inserted", insertedRecords);
        results.put("updated", updatedRecords);
        results.put("deleted", deletedRecords);

        return results;
    }

}
