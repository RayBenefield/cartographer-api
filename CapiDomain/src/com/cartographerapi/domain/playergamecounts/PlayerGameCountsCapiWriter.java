package com.cartographerapi.domain.playergamecounts;

import com.cartographerapi.domain.CapiWrapper;
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
public class PlayerGameCountsCapiWriter implements PlayerGameCountsWriter {

	private CapiWrapper api;
	private ObjectMapper mapper;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerGameCounts savePlayerGameCounts(PlayerGameCounts counts) {
		try {
			String totalResult = api.playerGameCountsUpdater(counts.getGamertag());
			counts = mapper.readValue(totalResult, PlayerGameCounts.class);
		} catch (IOException exception) {
			return counts;
		}

		return counts;
	}
	
    /**
     * The lazy IOC constructor.
     */
	public PlayerGameCountsCapiWriter() {
		this.api = new CapiWrapper();
		this.mapper = new ObjectMapper();
	}

}
