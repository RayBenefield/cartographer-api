package com.cartographerapi.domain.gameevents;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

import com.cartographerapi.domain.Halo5ApiWrapper;
import com.cartographerapi.domain.game.MatchId;

/**
 * Reader repository for Games from the Halo API.
 *
 * @see GamesReader
 *
 * @author GodlyPerfection
 *
 */
public class GameEventsHaloApiReader implements GameEventsReader {

    private Halo5ApiWrapper api;
    private ObjectMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public GameEvents getGameEventsByMatchId(MatchId matchId) {
        JsonNode root;

        try {
            String matchEventsResult = api.matchEvents(matchId.getMatchId());
            root = mapper.readTree(matchEventsResult);
        } catch (IOException exception) {
            return null;
        }

        System.out.println(root.path("GameEvents").size());
        return new GameEvents(matchId, root.path("GameEvents").size());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GameEvents> getGameEventsByMatchIds(List<MatchId> matchIds) {
        List<GameEvents> results = new ArrayList<GameEvents>();

        for (MatchId matchId : matchIds) {
            GameEvents events = this.getGameEventsByMatchId(matchId);

            if (events != null) {
                results.add(events);
            }
        }

        return results;
    }

    /**
     * The lazy IOC constructor.
     */
    public GameEventsHaloApiReader() {
        this.api = new Halo5ApiWrapper();
        this.mapper = new ObjectMapper();
    }

}
