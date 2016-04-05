package com.cartographerapi.customgames;

public interface PlayerGameCountsReader {
	
	public PlayerGameCounts getPlayerGameCountsByGamertag(String gamertag);

	public PlayerGameCounts getPlayerGameCountsByPlayerGameCounts(PlayerGameCounts counts);

}
