package com.cartographerapi.domain.bareplayergames;

import java.util.List;

/**
 * Writer repository interface for BarePlayerGame. This provides access to a
 * data source that contains the BarePlayerGame object.
 *
 * @author GodlyPerfection
 *
 */
public interface BarePlayerGamesWriter {

    /**
     * Save a BarePlayerGame object.
     *
     * @param game
     * @return
     */
    public BarePlayerGame saveBarePlayerGame(BarePlayerGame game);

    /**
     * Save BarePlayerGame objects.
     *
     * @param games
     * @return
     */
    public List<BarePlayerGame> saveBarePlayerGames(List<BarePlayerGame> games);

}
