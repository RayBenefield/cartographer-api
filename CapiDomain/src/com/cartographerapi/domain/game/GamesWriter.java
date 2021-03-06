package com.cartographerapi.domain.game;

import java.util.List;

/**
 * Writer repository interface for Game. This provides access to a
 * data source that contains the Game object.
 * 
 * @author GodlyPerfection
 *
 */
public interface GamesWriter {
	
	/**
	 * Save Game object.
	 * 
	 * @param game
	 * @return
	 */
	public Game saveGame(Game game);
	
	/**
	 * Save multiple Game objects.
	 * 
	 * @param games
	 * @return
	 */
	public List<Game> saveGames(List<Game> games);

}
