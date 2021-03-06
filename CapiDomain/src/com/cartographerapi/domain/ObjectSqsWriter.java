package com.cartographerapi.domain;

import java.util.List;
import java.util.ArrayList;

import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

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
    private ConfigReader configReader;
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
        SendMessageBatchRequest request = new SendMessageBatchRequest();
        List<SendMessageBatchRequestEntry> batch = new ArrayList<SendMessageBatchRequestEntry>();
        Integer idIterator = 0;

        try {
            // For each object, start building a batch and send as needed
            for (Object object : objects) {
                batch.add(new SendMessageBatchRequestEntry()
                    .withId(idIterator.toString())
                    .withMessageBody(mapper.writeValueAsString(new SqsMessage(object)))
                );
                idIterator++;
                
                // When we hit 3 in the batch then package, send, and reset
                if (idIterator >= 3) {
                    request.withQueueUrl(queueUrl).withEntries(batch);
                    client.sendMessageBatch(request);
                    
                    // Reset and go again
                    idIterator = 0;
                    batch = new ArrayList<SendMessageBatchRequestEntry>();
                    request = new SendMessageBatchRequest();
                }
            }
            
            // We are out of objects, let's send any strays that exist
            if (batch.size() > 0) {
                request.withQueueUrl(queueUrl).withEntries(batch);
                client.sendMessageBatch(request);
            }
        } catch (IOException exception) {
        }

        return objects;
    }

    /**
     * Set the target queue for writing.
     * 
     * @param queueUrl
     */
    public void setQueueUrl(String queueUrlKey) {
        this.queueUrl = configReader.getValue(queueUrlKey);
    }

    /**
     * The lazy IOC constructor.
     */
    public ObjectSqsWriter(String queueUrlKey) {
        mapper = new ObjectMapper();
        this.configReader = new ConfigDynamoReader();
        this.queueUrl = configReader.getValue(queueUrlKey);
        client = new AmazonSQSClient();
        client.setRegion(Region.getRegion(Regions.US_WEST_2));
    }

    /**
     * The lazy IOC constructor.
     */
    public ObjectSqsWriter() {
        mapper = new ObjectMapper();
        this.configReader = new ConfigDynamoReader();
        client = new AmazonSQSClient();
        client.setRegion(Region.getRegion(Regions.US_WEST_2));
    }

}
