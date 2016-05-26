package com.cartographerapi.domain.game;

/**
 * A MatchId that references a Game in Halo.
 *
 * @author GodlyPerfection
 *
 */
public class MatchId {

	private String matchId;

	public String getMatchId() {
		return matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId.toLowerCase();
	}

	public MatchId(String matchId) {
		this.matchId = matchId.toLowerCase();
	}

	public MatchId() {
	}
}
