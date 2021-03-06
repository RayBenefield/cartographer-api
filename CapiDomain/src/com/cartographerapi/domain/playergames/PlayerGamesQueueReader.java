package com.cartographerapi.domain.playergames;

import java.util.List;

/**
 * Queue reader repository interface for PlayerGames. This provides access to a
 * queue data source that contains the PlayerGame object.
 * 
 * @author GodlyPerfection
 *
 */
public interface PlayerGamesQueueReader {

	/**
	 * Get a number of PlayerGame objects.
	 * 
	 * @param gamertag
	 * @return
	 */
	public List<PlayerGame> getNumberOfPlayerGames(Integer count);

	/**
	 * With the PlayerGame processed, we can delete the message in the queue
	 * 
	 * @param gamertag
	 * @return
	 */
	public PlayerGame processedPlayerGame(PlayerGame game);
}
