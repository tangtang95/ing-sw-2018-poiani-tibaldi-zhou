package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.IView;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IGameObserver;
import org.poianitibaldizhou.sagrada.game.model.players.Player;

import java.util.List;

public interface IGameView extends IView, IGameObserver{
    void notifyModelSynch(DraftPool draftPool, List<Player> players, RoundTrack roundTrack,
                          List<ToolCard> toolCards);
}
