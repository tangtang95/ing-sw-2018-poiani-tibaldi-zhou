package org.poianitibaldizhou.sagrada.game.model.card.toolcards;

public interface IToolCardObserver {

    public void onTokenChange(int tokens);
    public void onCardDestroy();
}
