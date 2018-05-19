package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

public interface IToolCardObserver {

    void onTokenChange(int tokens);
    void onCardDestroy();
}

