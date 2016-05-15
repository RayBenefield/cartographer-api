package com.cartographerapi.domain.playergames;

import com.amazonaws.util.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cartographerapi.domain.Halo5ApiWrapper;

/**
 * Reader repository for PlayerGames from the Halo API.
 * 
 * @see PlayerGamesWriter
 * 
 * @author GodlyPerfection
 *
 */
public class PlayerGamesHaloApiReader implements PlayerGamesReader {

    private Halo5ApiWrapper api;
    private ObjectMapper mapper;
    private Integer total;
    private String lastMatch;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PlayerGame> getPlayerGamesByGamertag(String gamertag) {
        return getPlayerGamesByGamertag(gamertag, 0, 25);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PlayerGame> getPlayerGamesByGamertag(String gamertag, Integer start, Integer count) {
        List<PlayerGame> results = new ArrayList<PlayerGame>();
        try {
            // Go backwards from the total, back to where we want to start, then 24 items for storage.
            Integer apiStart = total - start - 24;
            
            // If the start is less than 0 then we only have a few matches left to grab.
            if (apiStart < 0) {
                apiStart = 0;
                count = total - start + 1;
            }

            String gamesResult = api.customGames(gamertag, apiStart, count);
            JsonNode root = mapper.readTree(gamesResult);

            if (root.path("ResultCount").isMissingNode()) {
                return results;
            }

            Integer resultCount = root.path("ResultCount").asInt();
            
            // If we have no lastMatch to check, then if we get more than 24
            // games then new games were added and we need to re-check the
            // total to avoid missing games.
            if (
                StringUtils.isNullOrEmpty(lastMatch)
                && resultCount.equals(25)
            ) {
                return null;
            }

            // If the very last match does not equal the expected lastMatch
            // then new games were added and we need to stop here to avoid
            // missing games.
            if (
                !StringUtils.isNullOrEmpty(lastMatch)
                && !root.path("Results").path(resultCount - 1).path("Id").path("MatchId").asText().equals(lastMatch)
            ) {
                return null;
            }

            Integer numberOfGame = start + (count - 1);
            for (JsonNode game : root.path("Results")) {
                PlayerGame newGame = new PlayerGame(gamertag, numberOfGame, game);
                results.add(newGame);
                numberOfGame--;
            }
            
            // Since the matches are returned by most recent first, not
            // chronologically we want to reverse the collection.
            Collections.reverse(results);
            
            // Since the last match was just used for verification, we can remove it.
            if (!StringUtils.isNullOrEmpty(lastMatch)) {
                results.remove(0);
            }
        } catch (IOException exception) {
            return null;
        }
        return results;
    }

    /**
     * Since the Halo API returns most recent first, this allows us to work
     * backwards from the first game played.
     * 
     * @param total
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     * If results were pulled before check against the last match we found.
     * 
     * @param lastMatch
     */
    public void setLastMatch(String lastMatch) {
        this.lastMatch = lastMatch;
    }
    
    /**
     * The lazy IOC constructor.
     */
    public PlayerGamesHaloApiReader() {
        this.api = new Halo5ApiWrapper();
        this.mapper = new ObjectMapper();
    }

}
