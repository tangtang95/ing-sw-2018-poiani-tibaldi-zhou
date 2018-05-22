package org.poianitibaldizhou.sagrada.game.model;

import org.jetbrains.annotations.NotNull;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.coin.ICoin;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.util.ArrayList;
import java.util.List;

public class SinglePlayer extends Player {


    /**
     * Set to null the player parameters:
     * - coins are the player expendable coins for using the toolCard
     * - schemaCard
     * - privateObjectiveCard
     *
     * @param user
     * @param coin
     * @param schemaCard
     * @param privateObjectiveCards
     */
    public SinglePlayer(User user, ICoin coin, SchemaCard schemaCard, List<PrivateObjectiveCard> privateObjectiveCards) {
        super(user, coin, schemaCard, privateObjectiveCards);
    }

    /**
     * @return the score of the player for singlePlayerGame
     */
    @Override
    public int getVictoryPoints() {
        return privateObjectiveCards.get(indexOfPrivateObjectiveCard)
                .getScore(schemaCard) - schemaCard.getNumberOfEmptySpaces() * 3;
    }

    public static SinglePlayer newInstance(@NotNull Player player) {
        // TODO new instance of ICoin??
        SinglePlayer newPlayer = new SinglePlayer(player.getUser(), player.coin, SchemaCard.newInstance(player.schemaCard),
                new ArrayList<>(player.privateObjectiveCards));
        player.getObserverList().forEach(obs -> newPlayer.attachObserver(obs));
        return newPlayer;
    }
}
