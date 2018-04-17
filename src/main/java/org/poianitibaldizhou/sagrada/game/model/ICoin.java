package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.game.model.card.ToolCard;

public interface ICoin {
    void upDate();
    void use(ToolCard toolCard);
    boolean checkCoin(ToolCard toolCard);
}
