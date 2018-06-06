package org.poianitibaldizhou.sagrada.game.model.players;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.coin.ExpendableDice;
import org.poianitibaldizhou.sagrada.game.model.coin.ICoin;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.util.ArrayList;
import java.util.List;

public class SinglePlayer extends Player {


    /**
     * {@inheritDoc}
     */
    public SinglePlayer(User user, ExpendableDice expendableDice, SchemaCard schemaCard,
                        List<PrivateObjectiveCard> privateObjectiveCards) {
        super(user, expendableDice, schemaCard, privateObjectiveCards);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getVictoryPoints() {
        return privateObjectiveCards.get(indexOfPrivateObjectiveCard)
                .getScore(schemaCard) - schemaCard.getNumberOfEmptySpaces() * 3;
    }

    public static SinglePlayer newInstance(@NotNull Player player) {
        if(!(player instanceof SinglePlayer))
            throw new IllegalArgumentException("SEVERE ERROR: player is not a single player, do not call this method directly");
        SinglePlayer newPlayer = new SinglePlayer(player.getUser(), (ExpendableDice) player.coin, SchemaCard.newInstance(player.schemaCard),
                new ArrayList<>(player.privateObjectiveCards));
        player.getObserverMap().forEach(newPlayer::attachObserver);
        return newPlayer;
    }

    @Override
    public JSONObject toJSON() {
        return null;
    }
}
