package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.RoundTrack;

import java.rmi.RemoteException;
import java.util.List;

public interface IToolCardObserver {

    void onTokenChange(int tokens);
    void onCardDestroy();
}

