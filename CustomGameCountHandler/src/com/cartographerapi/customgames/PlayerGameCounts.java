package com.cartographerapi.customgames;

public class PlayerGameCounts {
	String gamertag;
	Integer gamesCompleted;
	Integer totalGames;

	public String getGamertag() {
		return gamertag;
	}

	public void setGamertag(String gamertag) {
		this.gamertag = gamertag;
	}

	public Integer getGamesCompleted() {
		return gamesCompleted;
	}

	public void setGamesCompleted(Integer gamesCompleted) {
		this.gamesCompleted = gamesCompleted;
	}

	public Integer getTotalGames() {
		return totalGames;
	}

	public void setTotalGames(Integer totalGames) {
		this.totalGames = totalGames;
	}

	public PlayerGameCounts(String gamertag, Integer gamesCompleted, Integer totalGames) {
		this.gamertag = gamertag;
		this.gamesCompleted = gamesCompleted;
		this.totalGames = totalGames;
	}

	public PlayerGameCounts() {
	}
}
