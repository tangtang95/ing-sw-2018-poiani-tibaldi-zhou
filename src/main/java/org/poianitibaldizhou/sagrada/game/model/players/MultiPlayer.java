package org.poianitibaldizhou.sagrada.game.model.players;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.coin.FavorToken;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MultiPlayer extends Player {


    /**
     * {@inheritDoc}
     */
    public MultiPlayer(User user, FavorToken favorToken, SchemaCard schemaCard, List<PrivateObjectiveCard> privateObjectiveCards) {
        super(user, favorToken, schemaCard, privateObjectiveCards);
    }

    public MultiPlayer(Player player) {
        super(player.user, new FavorToken(player.getCoins()), SchemaCard.newInstance(player.schemaCard),
                new ArrayList<>(player.privateObjectiveCards));
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
                .getScore(schemaCard) + getCoins() - schemaCard.getNumberOfEmptySpaces();
    }

    public static MultiPlayer newInstance(@NotNull Player player) {
        if(!(player instanceof MultiPlayer))
            throw new IllegalArgumentException("SEVERE ERROR: player is not a MultiPlayer, do not call this method from MultiPlayer");
        return new MultiPlayer(player);
    }

    @Override
    public JSONObject toJSON() {
        return null;
    }
}
