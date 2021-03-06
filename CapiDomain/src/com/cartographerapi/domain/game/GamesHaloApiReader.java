package com.cartographerapi.domain.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.cartographerapi.domain.Halo5ApiWrapper;
import com.cartographerapi.domain.playergamecounts.PlayerGameCountsReader;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

/**
 * Reader repository for PlayerGameCounts from the Halo API.
 *
 * @see PlayerGameCountsReader
 *
 * @author GodlyPerfection
 *
 */
public class GamesHaloApiReader implements GamesReader {

    private Halo5ApiWrapper api;
    private ObjectMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public Game getGameByMatchId(MatchId matchId) {
        JsonNode root;

        try {
            String matchResult = api.match(matchId.getMatchId());
            root = mapper.readTree(matchResult);
        } catch (IOException exception) {
            return null;
        }

        return new Game(matchId, root);
    }

    /**
     * The lazy IOC constructor.
     */
    public GamesHaloApiReader() {
        this.api = new Halo5ApiWrapper();
        this.mapper = new ObjectMapper();
    }

}
