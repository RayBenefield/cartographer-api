package com.cartographerapi.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/**
 * Reader repository for PlayerGameCounts from CAPI.
 * 
 * @see PlayerGameCountsReader
 * 
 * @author GodlyPerfection
 *
 */
public class PlayerGameCountsCapiReader implements PlayerGameCountsReader {

	private CapiWrapper api;
	private ObjectMapper mapper;
	
	public PlayerGameCountsCapiReader() {
		this.api = new CapiWrapper();
		this.mapper = new ObjectMapper();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerGameCounts getPlayerGameCountsByGamertag(String gamertag) {
		PlayerGameCounts counts;

		try {
			String totalResult = api.playerGameCountsGetter(gamertag);
			counts = mapper.readValue(totalResult, PlayerGameCounts.class);
		} catch (IOException exception) {
			return new PlayerGameCounts(gamertag, 0, 0);
		}

		return counts;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerGameCounts getPlayerGameCountsByPlayerGameCounts(PlayerGameCounts counts) {
		PlayerGameCounts newCounts;

		try {
			String totalResult = api.playerGameCountsGetter(counts.getGamertag());
			newCounts = mapper.readValue(totalResult, PlayerGameCounts.class);
		} catch (IOException exception) {
			return counts;
		}

		return newCounts;
	}

}