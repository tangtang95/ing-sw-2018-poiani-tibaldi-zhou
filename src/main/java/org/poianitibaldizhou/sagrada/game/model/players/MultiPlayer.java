package org.poianitibaldizhou.sagrada.game.model.players;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.coin.FavorToken;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.utilities.ServerMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * OVERVIEW: Represents a player who is playing the multi player version of Sagrada.
 */
public class MultiPlayer extends Player {

    /**
     * {@inheritDoc}
     */
    public MultiPlayer(User user, FavorToken favorToken, SchemaCard schemaCard, List<PrivateObjectiveCard> privateObjectiveCards) {
        super(user, favorToken, schemaCard, privateObjectiveCards);
    }

    /**
     * Private constructor used in the new instance.
     * Deep copy implemented.
     *
     * @param player a new player with the same state of player will be created
     */
    private MultiPlayer(Player player) {
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

    /**
     * A new instance of player will be created.
     * Deep copy implemented.
     *
     * @param player
     * @return
     */
    public static MultiPlayer newInstance(@NotNull Player player) {
        if(!(player instanceof MultiPlayer))
            throw new IllegalArgumentException(ServerMessage.MULTI_PLAYER_ILLEGAL_ARGUMENT);
        return new MultiPlayer(player);
    }

    /**
     * This method return null because player are never converted to json object, because
     * they are not transmitted over the network
     *
     * @return null
     */
    @Override
    public JSONObject toJSON() {
        return null;
    }
}
