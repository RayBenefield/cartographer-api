package com.cartographerapi.domain;

import java.net.URL;
import java.net.URLEncoder;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Simple wrapper to allow for querying the Halo API.
 * 
 * @author GodlyPerfection
 *
 */
public class Halo5ApiWrapper {

    private String token;
    private ConfigReader configReader;

    private final String URL_CUSTOM_SERVICE_RECORD = "https://www.haloapi.com/stats/h5/servicerecords/custom?players=%s";
    private final String URL_CUSTOM_GAMES = "https://www.haloapi.com/stats/h5/players/%s/matches?modes=custom&start=%s&count=%s";
    private final String URL_CUSTOM_MATCH = "https://www.haloapi.com/stats/h5/custom/matches/%s";
    private final String URL_CUSTOM_GAME_EVENTS = "https://www.haloapi.com/stats/h5/matches/%s/events";

    /**
     * Get the details on a Match.
     * 
     * @param matchId
     * @return
     * @throws IOException
     */
    public String match(String matchId) throws IOException {
        return call(String.format(URL_CUSTOM_MATCH, URLEncoder.encode(matchId, "UTF-8")));
    }

    /**
     * Get the events of a Match.
     * 
     * @param matchId
     * @return
     * @throws IOException
     */
    public String matchEvents(String matchId) throws IOException {
        return call(String.format(URL_CUSTOM_GAME_EVENTS, URLEncoder.encode(matchId, "UTF-8")));
    }

    /**
     * Get the service record for a player.
     * 
     * @param gamertag
     * @return
     * @throws IOException
     */
    public String serviceRecord(String gamertag) throws IOException {
        return call(String.format(URL_CUSTOM_SERVICE_RECORD, URLEncoder.encode(gamertag, "UTF-8")));
    }

    /**
     * Get the custom games for a player.
     * 
     * @param gamertag
     * @return
     * @throws IOException
     */
    public String customGames(String gamertag) throws IOException {
        return customGames(gamertag, 0, 25);
    }

    /**
     * Get the custom games for a player, starting at a given index.
     * 
     * @param gamertag
     * @param start
     * @return
     * @throws IOException
     */
    public String customGames(String gamertag, Integer start) throws IOException {
        return customGames(gamertag, start, 25);
    }

    /**
     * Get a given number of custom games for a player, starting at a given
     * index.
     * 
     * @param gamertag
     * @param start
     * @param count
     * @return
     * @throws IOException
     */
    public String customGames(String gamertag, Integer start, Integer count) throws IOException {
        return call(String.format(URL_CUSTOM_GAMES, URLEncoder.encode(gamertag, "UTF-8"), start, count));
    }

    /**
     * Call the Halo API with the given URL.
     * 
     * @param url
     * @return
     * @throws IOException
     */
    private String call(String url) throws IOException {
        URL apiUrl = new URL(url);
        HttpURLConnection urlConn = (HttpURLConnection) apiUrl.openConnection();
        urlConn.setRequestMethod("GET");
        urlConn.setRequestProperty("Ocp-Apim-Subscription-Key", token);

        StringBuilder output = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            output.append(inputLine);
        }
        in.close();
        return output.toString();
    }

    /**
     * The lazy IOC constructor.
     */
    public Halo5ApiWrapper() {
        this.configReader = new ConfigDynamoReader();
        this.token = configReader.getValue("HaloApiKey");
    }

}
