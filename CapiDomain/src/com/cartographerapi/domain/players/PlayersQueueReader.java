package com.cartographerapi.domain.players;

import java.util.List;

/**
 * Queue reader repository interface for Players. This provides access to a
 * queue data source that contains the Players object.
 * 
 * @author GodlyPerfection
 *
 */
public interface PlayersQueueReader {

	/**
	 * Get a number of Player objects.
	 * 
	 * @param count
	 * @return
	 */
	public List<Player> getNumberOfPlayers(Integer count);

	/**
	 * With the Player processed, we can delete the message in the queue.
	 * 
	 * @param player
	 * @return
	 */
	public Player processedPlayer(Player game);
}
