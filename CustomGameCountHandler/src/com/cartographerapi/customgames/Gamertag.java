package com.cartographerapi.customgames;

public class Gamertag {
	String gamertag;

	public String getGamertag() {
		return gamertag;
	}

	public void setGamertag(String gamertag) {
		this.gamertag = gamertag.toLowerCase();
	}

	public Gamertag(String gamertag) {
		this.gamertag = gamertag.toLowerCase();
	}

	public Gamertag() {
	}
}
