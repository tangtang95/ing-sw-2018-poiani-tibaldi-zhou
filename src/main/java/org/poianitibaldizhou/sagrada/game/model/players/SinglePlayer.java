package org.poianitibaldizhou.sagrada.game.model.players;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.coin.ExpendableDice;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.utilities.ServerMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SinglePlayer extends Player {


    /**
     * {@inheritDoc}
     */
    public SinglePlayer(User user, ExpendableDice expendableDice, SchemaCard schemaCard,
                        List<PrivateObjectiveCard> privateObjectiveCards) {
        super(user, expendableDice, schemaCard, privateObjectiveCards);
    }

    private SinglePlayer(Player player) {
        super(player.user, player.coin, SchemaCard.newInstance(player.schemaCard), new ArrayList<>(player.privateObjectiveCards));
        this.outcome = player.outcome;
        this.indexOfPrivateObjectiveCard = player.indexOfPrivateObjectiveCard;
        this.observerMap = new HashMap<>(player.observerMap);
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
            throw new IllegalArgumentException(ServerMessage.SINGLE_PLAYER_ILLEGAL_ARGUMENT);
        return new SinglePlayer(player);
    }

    /**
     *
     * @return null
     */
    @Override
    public JSONObject toJSON() {
        return null;
    }
}
