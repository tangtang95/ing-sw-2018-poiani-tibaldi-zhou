package org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces;

public interface IToolCardFakeObserver {

    /**
     * Notify the change of the tokens
     *
     * @param tokens the new number of tokens
     */
    void onTokenChange(int tokens);

    /**
     * Notify the destruction of the toolCard
     */
    void onCardDestroy();
}