package com.cartographerapi.domain;

import java.util.List;

/**
 * Reader repository interface for PlayerGameCounts. This provides access to a
 * data source that contains the PlayerGameCounts object with a LastUpdated
 * value.
 * 
 * @author GodlyPerfection
 *
 */
public interface PlayerGameCountsUpdatedReader extends PlayerGameCountsReader {

	/**
	 * Get all PlayerGameCounts object that have not been updated since given date.
	 * 
	 * @param date
	 * @return
	 */
	public List<PlayerGameCounts> getAllPlayerGameCountsNotUpdatedSince(String date);

}
