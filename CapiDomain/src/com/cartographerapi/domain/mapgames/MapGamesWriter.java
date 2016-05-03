package com.cartographerapi.domain.mapgames;

import java.util.List;

/**
 * Writer repository interface for MapGame. This provides access to a
 * data source that contains the MapGame object.
 * 
 * @author GodlyPerfection
 *
 */
public interface MapGamesWriter {
	
	/**
	 * Save MapGame object.
	 * 
	 * @param mapGame
	 * @return
	 */
	public MapGame saveMapGame(MapGame mapGame);
	
	/**
	 * Save multiple Game objects.
	 * 
	 * @param mapGames
	 * @return
	 */
	public List<MapGame> saveMapGames(List<MapGame> mapGames);

}
