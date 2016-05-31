package com.cartographerapi.domain.game;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A MatchId that references a Game in Halo.
 *
 * @author GodlyPerfection
 *
 */
public class MatchId {

    @JsonProperty("MatchId")
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
