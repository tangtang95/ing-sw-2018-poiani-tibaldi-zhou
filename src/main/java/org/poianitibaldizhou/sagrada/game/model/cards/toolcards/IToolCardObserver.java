package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

public interface IToolCardObserver {

    public void onTokenChange(int tokens);
    public void onCardDestroy();

    public void notifyNeedDice();
}
