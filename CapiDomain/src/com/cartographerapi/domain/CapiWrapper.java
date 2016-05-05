package com.cartographerapi.domain;

import java.net.URL;
import java.net.URLEncoder;
import java.net.HttpURLConnection;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Simple wrapper to allow for talking to CAPI.
 * 
 * @author GodlyPerfection
 *
 */
public class CapiWrapper {
	
	private final String URL_PLAYER_GAMES_COUNT = "%s/test/player/%s/game/count";
	
	private String host;
	private ConfigReader configReader;
	
	/**
	 * Update the count of games.
	 * 
	 * @param gamertag
	 * @return
	 * @throws IOException
	 */
	public String playerGameCountsUpdater(String gamertag) throws IOException {
		return call(
			String.format(URL_PLAYER_GAMES_COUNT, host, URLEncoder.encode(gamertag, "UTF-8")),
			"POST"
		);
	}
	
	/**
	 * Get the count of games for a given player.
	 * 
	 * @param gamertag
	 * @return
	 * @throws IOException
	 */
	public String playerGameCountsGetter(String gamertag) throws IOException {
		return call(
			String.format(URL_PLAYER_GAMES_COUNT, host, URLEncoder.encode(gamertag, "UTF-8"))
		);
	}

	/**
	 * Call CAPI with the given URL using GET.
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	private String call(String url) throws IOException {
		return call(url, "GET");
	}

	/**
	 * Call CAPI with the given URL using the given method.
	 * 
	 * @param url
	 * @param method
	 * @return
	 * @throws IOException
	 */
	private String call(String url, String method) throws IOException {
		URL apiUrl = new URL(url);
		HttpURLConnection urlConn = (HttpURLConnection)apiUrl.openConnection();
		urlConn.setRequestMethod(method);

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
	public CapiWrapper() {
		this.configReader = new ConfigDynamoReader();
		this.host = configReader.getValue("capiHost");
	}
}
