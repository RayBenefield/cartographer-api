package com.cartographerapi.customgames;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

public class PlayerGameCountsHaloApiReader implements PlayerGameCountsReader {

	Halo5ApiWrapper api = new Halo5ApiWrapper("ae4df7c91357455ea30be2d7bdf15522");
	ObjectMapper mapper = new ObjectMapper();
	
	public PlayerGameCountsHaloApiReader() {
	}

	@Override
	public PlayerGameCounts getPlayerGameCountsByGamertag(String gamertag) {
		Integer completedGames = 0;
		Integer totalGames = 0;

		try {
			JsonNode root;
			String totalResult = api.serviceRecord(gamertag);
			root = mapper.readTree(totalResult);
			completedGames = root.path("Results").path(0).path("Result").path("CustomStats").path("TotalGamesCompleted").asInt();
			totalGames = completedGames;

			Integer lastGames = 25;
			while (lastGames == 25) {
				String lastResult = api.customGames(gamertag, totalGames);
				root = mapper.readTree(lastResult);
				lastGames = root.path("ResultCount").asInt();
				totalGames += lastGames;
			}
		} catch (IOException exception) {
			return new PlayerGameCounts(gamertag, 0, 0);
		}

		return new PlayerGameCounts(gamertag, completedGames, totalGames);
	}

	@Override
	public PlayerGameCounts getPlayerGameCountsByPlayerGameCounts(PlayerGameCounts counts) {
		Integer completedGames = counts.getGamesCompleted();
		Integer totalGames = counts.getTotalGames();

		try {
			JsonNode root;
			String totalResult = api.serviceRecord(counts.getGamertag());
			root = mapper.readTree(totalResult);
			completedGames = root.path("Results").path(0).path("Result").path("CustomStats").path("TotalGamesCompleted").asInt();
			totalGames = counts.getTotalGames();

			Integer lastGames = 25;
			while (lastGames == 25) {
				String lastResult = api.customGames(counts.getGamertag(), totalGames);
				root = mapper.readTree(lastResult);
				lastGames = root.path("ResultCount").asInt();
				totalGames += lastGames;
			}
		} catch (IOException exception) {
			return counts;
		}

		return new PlayerGameCounts(counts.getGamertag(), completedGames, totalGames);
	}

}
