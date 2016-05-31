package com.cartographerapi.domain.mapgames;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A MapId that references a Map in Halo.
 *
 * @author GodlyPerfection
 *
 */
public class MapId {

    @JsonProperty("MapId")
	private String mapId;

	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId.toLowerCase();
	}

	public MapId(String mapId) {
		this.mapId = mapId.toLowerCase();
	}

	public MapId() {
	}
}
