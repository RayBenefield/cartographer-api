package com.cartographerapi.domain;

import java.util.List;

/**
 * Reader repository interface for PlayerGames. This provides access to a
 * data source that contains the PlayerGame object.
 * 
 * @author GodlyPerfection
 *
 */
public interface PlayerGamesReader {

	/**
	 * Get PlayerGame objects for a given Gamertag.
	 * 
	 * @param gamertag
	 * @return
	 */
	public List<PlayerGame> getPlayerGamesByGamertag(String gamertag);

}
