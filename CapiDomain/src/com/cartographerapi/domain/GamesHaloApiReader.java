package com.cartographerapi.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

/**
 * Reader repository for PlayerGameCounts from the Halo API.
 * 
 * @see PlayerGameCountsReader
 * 
 * @author GodlyPerfection
 *
 */
public class GamesHaloApiReader implements GamesReader {

	private Halo5ApiWrapper api;
	private ObjectMapper mapper;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Game getGameByMatchId(String matchId) {
		JsonNode root;

		try {
			String matchResult = api.match(matchId);
			root = mapper.readTree(matchResult);
		} catch (IOException exception) {
			return null;
		}

		return new Game(matchId, root);
	}

    /**
     * The lazy IOC constructor.
     */
	public GamesHaloApiReader() {
		this.api = new Halo5ApiWrapper();
		this.mapper = new ObjectMapper();
	}

}
