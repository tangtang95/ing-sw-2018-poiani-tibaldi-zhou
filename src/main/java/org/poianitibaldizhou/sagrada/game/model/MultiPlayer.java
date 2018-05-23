package org.poianitibaldizhou.sagrada.game.model;

import org.jetbrains.annotations.NotNull;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.coin.ICoin;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.util.ArrayList;
import java.util.List;

public class MultiPlayer extends Player {


    /**
     * {@inheritDoc}
     */
    public MultiPlayer(User user, ICoin coin, SchemaCard schemaCard, List<PrivateObjectiveCard> privateObjectiveCards) {
        super(user, coin, schemaCard, privateObjectiveCards);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getVictoryPoints() {
        return privateObjectiveCards.get(indexOfPrivateObjectiveCard)
                .getScore(schemaCard) + getCoins() - schemaCard.getNumberOfEmptySpaces();
    }

    public static MultiPlayer newInstance(@NotNull MultiPlayer player) {
        // TODO coin new instance
        MultiPlayer newPlayer = new MultiPlayer(player.getUser(), player.coin, SchemaCard.newInstance(player.schemaCard), new ArrayList<>(player.privateObjectiveCards));
        player.getObserverList().forEach(newPlayer::attachObserver);
        return newPlayer;
    }
}
