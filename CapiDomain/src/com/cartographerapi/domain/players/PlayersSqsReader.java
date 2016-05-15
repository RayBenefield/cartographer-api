package com.cartographerapi.domain.players;

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
 * Queue Reader repository for Players from an SQS Queue.
 * 
 * @see PlayersQueueReader
 * 
 * @author GodlyPerfection
 *
 */
public class PlayersSqsReader implements PlayersQueueReader {

	private AmazonSQSClient client;
	private ConfigReader configReader;
	private String queueUrl;
    private ObjectMapper mapper;
    private Map<Player, Message> messageMap;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Player> getNumberOfPlayers(Integer count) {
		List<Player> results = new ArrayList<Player>();
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
        receiveMessageRequest.setMaxNumberOfMessages(count);
        List<Message> messages = client.receiveMessage(receiveMessageRequest).getMessages();
        
        // For each message map it to a Player for the message to be deleted after processing.
        for (Message message : messages) {
			try {
				JsonNode msgNode = mapper.readTree(mapper.readTree(message.getBody()).path("Message").textValue());
				Player player = new Player(
					msgNode.path("gamertag").asText()
				);
				messageMap.put(player, message);
				results.add(player);
			} catch (IOException exception) {
			}
        }
		return results;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Player processedPlayer(Player player) {
		Message message = messageMap.get(player);

		client.deleteMessage(new DeleteMessageRequest()
			.withQueueUrl(queueUrl)
			.withReceiptHandle(message.getReceiptHandle()));
		
		return player;
	}

    /**
     * The lazy IOC constructor.
     */
	public PlayersSqsReader(String queueUrlKey) {
		this.mapper = new ObjectMapper();
		this.configReader = new ConfigDynamoReader();
		this.queueUrl = configReader.getValue(queueUrlKey);
		client = new AmazonSQSClient();
		this.messageMap = new HashMap<Player, Message>();
	}

}
