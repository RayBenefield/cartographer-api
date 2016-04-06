package com.cartographerapi.functions;

/**
 * Represents an Xbox Live (XBL) Gamertag, a single user's XBL identity.
 * Gamertag is not case-sensitive for uniqueness, but presentation of the
 * Gamertag is case sensitive.
 * 
 * For example `godlyperfection` is unique and others can't use
 * `GODLYPERFECTION`. However when a Gamertag is created, the casing is locked
 * in for presentation purposes so `GodlyPerfection` would always be displayed
 * for `godlyperfection` if that is how it was registered.
 * 
 * TODO Add 15 character validation.
 * TODO Add invalid character validation.
 * 
 * @author GodlyPerfection
 *
 */
public class Gamertag {

	private String gamertag;

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
