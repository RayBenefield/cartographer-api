package com.cartographerapi.domain.game;

import java.util.List;

/**
 * Queue reader repository interface for Games. This provides access to a
 * queue data source that contains the Game object.
 * 
 * @author GodlyPerfection
 *
 */
public interface GamesQueueReader {

	/**
	 * Get a number of Game objects.
	 * 
	 * @param gamertag
	 * @return
	 */
	public List<Game> getNumberOfGames(Integer count);

	/**
	 * With the Game processed, we can delete the message in the queue
	 * 
	 * @param gamertag
	 * @return
	 */
	public Game processedGame(Game game);
}
