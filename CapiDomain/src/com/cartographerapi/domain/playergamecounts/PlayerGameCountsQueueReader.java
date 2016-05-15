package com.cartographerapi.domain.playergamecounts;

import java.util.List;

/**
 * Queue reader repository interface for PlayerGameCounts. This provides access to a
 * queue data source that contains the PlayerGameCounts object.
 * 
 * @author GodlyPerfection
 *
 */
public interface PlayerGameCountsQueueReader {

	/**
	 * Get a number of PlayerGameCounts objects.
	 * 
	 * @param count
	 * @return
	 */
	public List<PlayerGameCounts> getNumberOfPlayerGameCounts(Integer count);

	/**
	 * With the PlayerGameCounts processed, we can delete the message in the queue
	 * 
	 * @param counts
	 * @return
	 */
	public PlayerGameCounts processedPlayerGameCounts(PlayerGameCounts counts);
}
