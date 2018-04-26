package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.*;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;


public interface ICoin {

    /**
     * decrement the expendable coins when the player use that
     *
     * @param toolCard the card which the player would use
     * @throws NoCoinsExpendableException if there aren't any expandable favor tokens or dices
     * @throws DiceNotFoundException if dice is not present in the DraftPool
     * @throws EmptyCollectionException if draftPull is empty
     * @throws IllegalNumberOfTokensOnToolCardException if on the ToolCard there is a number of tokens < 0 or 1
     */
    void use(ToolCard toolCard) throws NoCoinsExpendableException, DiceNotFoundException,
            EmptyCollectionException, IllegalNumberOfTokensOnToolCardException;

    int getCoins();

}