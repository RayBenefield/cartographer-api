package com.cartographerapi.domain;

import java.util.List;

/**
 * Writer repository interface for PlayerGame. This provides access to a
 * data source that contains the PlayerGame object.
 * 
 * @author GodlyPerfection
 *
 */
public interface PlayerGamesWriter {
	
	/**
	 * Save PlayerGame objects.
	 * 
	 * @param games
	 * @return
	 */
	public List<PlayerGame> savePlayerGames(List<PlayerGame> games);

}
