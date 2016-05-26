package com.cartographerapi.domain.playergames;

import java.util.List;

/**
 * Reader repository interface for BarePlayerGames. This provides access to a
 * data source that contains the BarePlayerGame object.
 * 
 * @author GodlyPerfection
 *
 */
public interface BarePlayerGamesReader {

	/**
	 * Get BarePlayerGame objects for a given Gamertag.
	 * 
	 * @param gamertag
	 * @return
	 */
	public List<BarePlayerGame> getBarePlayerGamesByGamertag(String gamertag);

	/**
	 * Get BarePlayerGame objects for a given Gamertag.
	 * 
	 * @param gamertag
	 * @return
	 */
	public List<BarePlayerGame> getBarePlayerGamesByGamertag(String gamertag, Integer start, Integer count);

}
