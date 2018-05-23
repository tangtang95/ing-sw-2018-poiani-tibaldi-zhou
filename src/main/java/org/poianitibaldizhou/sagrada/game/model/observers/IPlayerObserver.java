package org.poianitibaldizhou.sagrada.game.model.observers;

public interface IPlayerObserver {

    /**
     * Notify that the tokens of a certain player has changed
     *
     * @param value new value of the player tokens
     */
    void onFavorTokenChange(int value);
}
