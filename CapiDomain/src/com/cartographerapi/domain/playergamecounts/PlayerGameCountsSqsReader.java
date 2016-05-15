package com.cartographerapi.domain.playergamecounts;

import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cartographerapi.domain.ConfigDynamoReader;
import com.cartographerapi.domain.ConfigReader;
import com.cartographerapi.domain.playergamecounts.PlayerGameCounts;

/**
 * Queue Reader repository for PlayerGameCounts from an SQS Queue.
 * 
 * @see PlayerGameCountsQueueReader
 * 
 * @author GodlyPerfection
 *
 */
public class PlayerGameCountsSqsReader implements PlayerGameCountsQueueReader {

	private AmazonSQSClient client;
	private ConfigReader configReader;
	private String queueUrl;
    private ObjectMapper mapper;
    private Map<PlayerGameCounts, Message> messageMap;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PlayerGameCounts> getNumberOfPlayerGameCounts(Integer count) {
		List<PlayerGameCounts> results = new ArrayList<PlayerGameCounts>();
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
        receiveMessageRequest.setMaxNumberOfMessages(count);
        List<Message> messages = client.receiveMessage(receiveMessageRequest).getMessages();
        
        // For each message map it to a PlayerGameCounts for the message to be deleted after processing.
        for (Message message : messages) {
			try {
				JsonNode msgNode = mapper.readTree(mapper.readTree(message.getBody()).path("Message").textValue());
				PlayerGameCounts counts = new PlayerGameCounts(
					msgNode.path("gamertag").asText(),
					msgNode.path("gamesCompleted").asInt(),
					msgNode.path("totalGames").asInt()
				);
				messageMap.put(counts, message);
				results.add(counts);
			} catch (IOException exception) {
			}
        }
		return results;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerGameCounts processedPlayerGameCounts(PlayerGameCounts counts) {
		Message message = messageMap.get(counts);

		client.deleteMessage(new DeleteMessageRequest()
			.withQueueUrl(queueUrl)
			.withReceiptHandle(message.getReceiptHandle()));
		
		return counts;
	}

    /**
     * The lazy IOC constructor.
     */
	public PlayerGameCountsSqsReader(String queueUrlKey) {
		this.mapper = new ObjectMapper();
		this.configReader = new ConfigDynamoReader();
		this.queueUrl = configReader.getValue(queueUrlKey);
		client = new AmazonSQSClient();
		this.messageMap = new HashMap<PlayerGameCounts, Message>();
	}

}
