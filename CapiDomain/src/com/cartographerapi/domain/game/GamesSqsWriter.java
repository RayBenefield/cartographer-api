package com.cartographerapi.domain.game;

import java.util.List;

import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.AmazonSQSClient;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

import com.cartographerapi.domain.SqsMessage;
import com.cartographerapi.domain.game.Game;

/**
 * Writer repository for Games into an SQS queue.
 * 
 * @see GamesWriter
 * 
 * @author GodlyPerfection
 *
 */
public class GamesSqsWriter implements GamesWriter {

	private AmazonSQSClient client;
	private String queueUrl;
    private ObjectMapper mapper;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Game saveGame(Game game) {
		try {
			SendMessageRequest sendMessageRequest = new SendMessageRequest()
				.withQueueUrl(queueUrl)
				.withMessageBody(mapper.writeValueAsString(new SqsMessage(game)));
			client.sendMessage(sendMessageRequest);
		} catch (IOException exception) {
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Game> saveGames(List<Game> games) {
		for (Game game : games) {
			saveGame(game);
		}
		return games;
	}

    /**
     * The lazy IOC constructor.
     */
	public GamesSqsWriter(String queueUrlKey) {
		mapper = new ObjectMapper();
		JsonNode config = mapper.createObjectNode();
		try {
			config = mapper.readTree(getClass().getClassLoader().getResource("config.json"));
		} catch (IOException exception) {
		}

		this.queueUrl = config.path(queueUrlKey).asText();
		client = new AmazonSQSClient();
		client.setRegion(Region.getRegion(Regions.US_WEST_2));
	}

}
