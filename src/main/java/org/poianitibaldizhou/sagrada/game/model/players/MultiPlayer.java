package org.poianitibaldizhou.sagrada.game.model.players;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.coin.FavorToken;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.util.ArrayList;
import java.util.List;

public class MultiPlayer extends Player {


    /**
     * {@inheritDoc}
     */
    public MultiPlayer(User user, SchemaCard schemaCard, List<PrivateObjectiveCard> privateObjectiveCards) {
        super(user, new FavorToken(schemaCard.getDifficulty()), schemaCard, privateObjectiveCards);
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
        // TODO coin new instance
        MultiPlayer newPlayer = new MultiPlayer(player.getUser(), SchemaCard.newInstance(player.schemaCard), new ArrayList<>(player.privateObjectiveCards));
        player.getObserverMap().forEach(newPlayer::attachObserver);
        return newPlayer;
    }

    @Override
    public JSONObject toJSON() {
        return null;
    }
}
