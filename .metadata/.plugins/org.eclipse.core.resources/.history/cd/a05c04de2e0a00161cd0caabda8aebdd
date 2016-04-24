package com.cartographerapi.domain;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import com.fasterxml.jackson.databind.JsonNode;

public class PlayerGamesHaloApiReader implements PlayerGamesReader {

	private Halo5ApiWrapper api;
	private ObjectMapper mapper;
	private Integer total;
	private String lastMatch;
	
	public PlayerGamesHaloApiReader() {
		this.api = new Halo5ApiWrapper();
		this.mapper = new ObjectMapper();
	}

	@Override
	public List<PlayerGame> getPlayerGamesByGamertag(String gamertag) {
		return getPlayerGamesByGamertag(gamertag, 0, 25);
	}

	@Override
	public List<PlayerGame> getPlayerGamesByGamertag(String gamertag, Integer start, Integer count) {
		List<PlayerGame> results = new ArrayList<PlayerGame>();
		try {
			Integer apiStart = total - start - 24;
			if (apiStart < 0) {
				apiStart = 0;
				count = total - start + 1;
			}
			String gamesResult = api.customGames(gamertag, apiStart, count);
			JsonNode root = mapper.readTree(gamesResult);
			Integer lastResult = root.path("ResultCount").asInt();
			if (
				lastMatch != null
				&& !root.path("Results").path(lastResult - 1).path("Id").path("MatchId").asText().equals(lastMatch)
			) {
				return results;
			}
			Integer numberOfGame = start + (count - 1);
			for (JsonNode game : root.path("Results")) {
				PlayerGame newGame = new PlayerGame(gamertag, numberOfGame, game);
				results.add(newGame);
				numberOfGame--;
			}
			Collections.reverse(results);
			if (lastMatch != null) {
				results.remove(0);
			}
		} catch (IOException exception) {
			return null;
		}
		return results;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public void setLastMatch(String lastMatch) {
		this.lastMatch = lastMatch;
	}

}
