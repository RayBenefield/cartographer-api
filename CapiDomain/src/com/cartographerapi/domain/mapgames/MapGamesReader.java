package com.cartographerapi.domain.mapgames;

import java.util.List;

/**
 * Reader repository interface for MapGames. This provides access to a
 * data source that contains the MapGame object.
 * 
 * @author GodlyPerfection
 *
 */
public interface MapGamesReader {

	/**
	 * Get MapGame objects for a given MapId.
	 * 
	 * @param mapId
	 * @return
	 */
	public List<MapGame> getMapGamesByMapId(MapId mapId);

}
