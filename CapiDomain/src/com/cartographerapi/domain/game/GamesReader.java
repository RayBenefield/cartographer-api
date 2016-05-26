package com.cartographerapi.domain.game;

/**
 * Reader repository interface for Games. This provides access to a
 * data source that contains the Game object.
 * 
 * @author GodlyPerfection
 *
 */
public interface GamesReader {

	/**
	 * Get Game with the given MatchId.
	 * 
	 * @param matchId
	 * @return
	 */
	public Game getGameByMatchId(MatchId matchId);

}
