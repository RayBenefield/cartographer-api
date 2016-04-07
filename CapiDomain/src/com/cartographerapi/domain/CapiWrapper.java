package com.cartographerapi.domain;

import java.net.URL;
import java.net.URLEncoder;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Simple wrapper to allow for talking to CAPI.
 * 
 * @author GodlyPerfection
 *
 */
public class CapiWrapper {
	
	private final String URL_PLAYER_GAMES_COUNT = "https://q98m1h2tv6.execute-api.us-west-2.amazonaws.com/test/player/%s/game/count";
	
	public CapiWrapper() {
	}
	
	/**
	 * Update the count of games.
	 * 
	 * @param gamertag
	 * @return
	 * @throws IOException
	 */
	public String playerGameCountsUpdater(String gamertag) throws IOException {
		return call(
			String.format(URL_PLAYER_GAMES_COUNT, URLEncoder.encode(gamertag, "UTF-8")),
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
			String.format(URL_PLAYER_GAMES_COUNT, URLEncoder.encode(gamertag, "UTF-8"))
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
}
