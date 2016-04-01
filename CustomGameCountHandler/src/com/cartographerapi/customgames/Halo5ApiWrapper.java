package com.cartographerapi.customgames;

import java.net.URL;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class Halo5ApiWrapper {
	private String token;
	
	private final String URL_CUSTOM_SERVICE_RECORD = "https://www.haloapi.com/stats/h5/servicerecords/custom?players=%s";
	
	public Halo5ApiWrapper(String token) {
		this.token = token;
	}
	
	public String serviceRecord(String gamertag) throws IOException {
		return call(
			String.format(URL_CUSTOM_SERVICE_RECORD, gamertag)
		);
	}

	private String call(String url) throws IOException {
		URL apiUrl = new URL(url);
		HttpURLConnection urlConn = (HttpURLConnection)apiUrl.openConnection();
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
}
