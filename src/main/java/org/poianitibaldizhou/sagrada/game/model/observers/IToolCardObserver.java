package org.poianitibaldizhou.sagrada.game.model.observers;

public interface IToolCardObserver {

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

