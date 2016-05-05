package com.cartographerapi.domain.playergames;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.cartographerapi.domain.ConfigDynamoReader;
import com.cartographerapi.domain.ConfigReader;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.AmazonSQSClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/**
 * Queue Reader repository for PlayerGames from an SQS Queue.
 * 
 * @see PlayerGamesQueueReader
 * 
 * @author GodlyPerfection
 *
 */
public class PlayerGamesSqsReader implements PlayerGamesQueueReader {

	private AmazonSQSClient client;
	private ConfigReader configReader;
	private String queueUrl;
    private ObjectMapper mapper;
    private Map<PlayerGame, Message> messageMap;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PlayerGame> getNumberOfPlayerGames(Integer count) {
		List<PlayerGame> results = new ArrayList<PlayerGame>();
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
        receiveMessageRequest.setMaxNumberOfMessages(count);
        List<Message> messages = client.receiveMessage(receiveMessageRequest).getMessages();
        
        // For each message map it to a PlayerGame for the message to be deleted after processing.
        for (Message message : messages) {
			try {
				JsonNode msgNode = mapper.readTree(mapper.readTree(message.getBody()).path("Message").textValue());
				PlayerGame game = new PlayerGame(
					msgNode.path("gamertag").asText(),
					msgNode.path("gameNumber").asInt(),
					msgNode.path("gameData")
				);
				messageMap.put(game, message);
				results.add(game);
			} catch (IOException exception) {
			}
        }
		return results;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerGame processedPlayerGame(PlayerGame game) {
		Message message = messageMap.get(game);

		client.deleteMessage(new DeleteMessageRequest()
			.withQueueUrl(queueUrl)
			.withReceiptHandle(message.getReceiptHandle()));
		
		return game;
	}

    /**
     * The lazy IOC constructor.
     */
	public PlayerGamesSqsReader(String queueUrlKey) {
		this.mapper = new ObjectMapper();
		this.configReader = new ConfigDynamoReader();
		this.queueUrl = configReader.getValue(queueUrlKey);
		client = new AmazonSQSClient();
		this.messageMap = new HashMap<PlayerGame, Message>();
	}

}
