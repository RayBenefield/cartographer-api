package com.cartographerapi.domain.mapgames;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import com.amazonaws.util.StringUtils;

import java.net.URLEncoder;

import java.io.UnsupportedEncodingException;

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
    private String owner;
    private String url;

	private final String URL_FILE_BROWSER = "https://www.halowaypoint.com/en-us/games/halo-5-guardians/xbox-one/map-variants#ugc_halo-5-guardians_xbox-one_mapvariant_%s_%s";

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

    @DynamoDBAttribute(attributeName="Owner")
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @DynamoDBAttribute(attributeName="Url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MapGame(Game game) {
        this.matchId = game.getMatchId();
        this.mapId = game.getGameData().path("MapVariantId").asText();
        this.owner = game.getGameData().path("MapVariantResourceId").path("Owner").asText();

        if (!StringUtils.isNullOrEmpty(this.owner)) {
            try {
                this.url = String.format(
                    URL_FILE_BROWSER,
                    URLEncoder.encode(this.owner, "UTF-8"),
                    URLEncoder.encode(this.mapId, "UTF-8")
                );
            } catch (UnsupportedEncodingException exception) {
                this.url = null;
            }
        }
    }

    public MapGame() {
    }

}
