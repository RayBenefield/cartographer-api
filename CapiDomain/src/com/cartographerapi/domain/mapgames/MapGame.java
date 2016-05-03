package com.cartographerapi.domain.mapgames;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import com.cartographerapi.domain.game.Game;

/**
 * Domain object that handles the relationship between a map and a game.
 * <pre>
 *    MapId
 *    MatchId
 * </pre>
 * 
 * @author GodlyPerfection
 *
 */
@DynamoDBTable(tableName="MapGames")
public class MapGame {

	private String mapId;
	private String matchId;

	@DynamoDBHashKey(attributeName="MapId")
	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	@DynamoDBRangeKey(attributeName="MatchId")
	public String getMatchId() {
		return matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}

	public MapGame(Game game) {
		this.matchId = game.getMatchId();
		this.mapId = game.getGameData().path("MapVariantId").asText();
	}

	public MapGame() {
	}

}
