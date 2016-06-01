package com.cartographerapi.domain.baregames;

import java.util.List;

/**
 * Writer repository interface for BareGame. This provides access to a
 * data source that contains the BareGame object.
 *
 * @author GodlyPerfection
 *
 */
public interface BareGamesWriter {

    /**
     * Save a BareGame object.
     *
     * @param game
     * @return
     */
    public BareGame saveBareGame(BareGame game);

    /**
     * Save BareGame objects.
     *
     * @param games
     * @return
     */
    public List<BareGame> saveBareGames(List<BareGame> games);

}
