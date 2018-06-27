package org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces;

/**
 * OVERVIEW: Fake observer for a certain tool card.
 * Fake observers are observer present on the server that listen to changes and modifications.
 * In this way, the network part is decoupled from the model.
 */
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