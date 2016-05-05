package com.cartographerapi.domain.game;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.AmazonSQSClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

import com.cartographerapi.domain.ConfigDynamoReader;
import com.cartographerapi.domain.ConfigReader;
import com.cartographerapi.domain.game.Game;

/**
 * Queue Reader repository for Games from an SQS Queue.
 * 
 * @see GamesQueueReader
 * 
 * @author GodlyPerfection
 *
 */
public class GamesSqsReader implements GamesQueueReader {

	private AmazonSQSClient client;
	private ConfigReader configReader;
	private String queueUrl;
    private ObjectMapper mapper;
    private Map<Game, Message> messageMap;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Game> getNumberOfGames(Integer count) {
		List<Game> results = new ArrayList<Game>();
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
        receiveMessageRequest.setMaxNumberOfMessages(count);
        List<Message> messages = client.receiveMessage(receiveMessageRequest).getMessages();
        
        // For each message map it to a Game for the message to be deleted after processing.
        for (Message message : messages) {
			try {
				JsonNode msgNode = mapper.readTree(mapper.readTree(message.getBody()).path("Message").textValue());
				Game game = new Game(
					msgNode.path("matchId").asText(),
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
	public Game processedGame(Game game) {
		Message message = messageMap.get(game);

		client.deleteMessage(new DeleteMessageRequest()
			.withQueueUrl(queueUrl)
			.withReceiptHandle(message.getReceiptHandle()));
		
		return game;
	}

    /**
     * The lazy IOC constructor.
     */
	public GamesSqsReader(String queueUrlKey) {
		this.mapper = new ObjectMapper();
		this.configReader = new ConfigDynamoReader();
		this.queueUrl = configReader.getValue(queueUrlKey);
		client = new AmazonSQSClient();
		this.messageMap = new HashMap<Game, Message>();
	}

}
