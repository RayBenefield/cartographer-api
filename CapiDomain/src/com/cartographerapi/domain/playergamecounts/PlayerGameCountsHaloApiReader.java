package com.cartographerapi.domain.playergamecounts;

import java.util.List;
import java.util.ArrayList;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.cartographerapi.domain.Halo5ApiWrapper;
import com.cartographerapi.domain.players.Player;


/**
 * Reader repository for PlayerGameCounts from the Halo API.
 *
 * @see PlayerGameCountsReader
 *
 * @author GodlyPerfection
 *
 */
public class PlayerGameCountsHaloApiReader implements PlayerGameCountsReader {

    private Halo5ApiWrapper api;
    private ObjectMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public PlayerGameCounts getPlayerGameCountsByGamertag(String gamertag) {
        Integer completedGames = 0;
        Integer totalGames = 0;

        try {
            JsonNode root;
            String totalResult = api.serviceRecord(gamertag);
            root = mapper.readTree(totalResult);

            if (root.path("Results").isMissingNode()) {
                return null;
            }

            completedGames = root.path("Results").path(0).path("Result").path("CustomStats").path("TotalGamesCompleted").asInt();
            totalGames = completedGames;

            Integer lastGames = 25;
            while (lastGames == 25) {
                String lastResult = api.customGames(gamertag, totalGames);
                root = mapper.readTree(lastResult);
                lastGames = root.path("ResultCount").asInt();
                totalGames += lastGames;
            }
        } catch (IOException exception) {
            return null;
        }

        return new PlayerGameCounts(gamertag, completedGames, totalGames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlayerGameCounts getPlayerGameCountsByPlayerGameCounts(PlayerGameCounts counts) {
        Integer completedGames = counts.getGamesCompleted();
        Integer totalGames = counts.getTotalGames();

        try {
            JsonNode root;
            String totalResult = api.serviceRecord(counts.getGamertag());
            root = mapper.readTree(totalResult);
            completedGames = root.path("Results").path(0).path("Result").path("CustomStats").path("TotalGamesCompleted").asInt();
            totalGames = counts.getTotalGames();

            Integer lastGames = 25;
            while (lastGames == 25) {
                String lastResult = api.customGames(counts.getGamertag(), totalGames);
                root = mapper.readTree(lastResult);
                lastGames = root.path("ResultCount").asInt();
                totalGames += lastGames;
            }
        } catch (IOException exception) {
            return counts;
        }

        return new PlayerGameCounts(counts.getGamertag(), completedGames, totalGames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PlayerGameCounts> getPlayerGameCountsByPlayers(List<Player> players) {
        List<PlayerGameCounts> counts = new ArrayList<PlayerGameCounts>();

        for (Player player : players) {
            counts.add(
                getPlayerGameCountsByGamertag(player.getGamertag())
            );
        }

        return counts;
    }

    /**
     * The lazy IOC constructor.
     */
    public PlayerGameCountsHaloApiReader() {
        this.api = new Halo5ApiWrapper();
        this.mapper = new ObjectMapper();
    }

}
