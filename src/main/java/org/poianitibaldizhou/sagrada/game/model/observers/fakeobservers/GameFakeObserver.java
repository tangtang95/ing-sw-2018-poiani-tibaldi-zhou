package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.game.model.cards.FrontBackSchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.observers.ObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.IGameFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IGameObserver;
import org.poianitibaldizhou.sagrada.game.model.players.Player;

import java.io.IOException;
import java.util.List;

public class GameFakeObserver implements IGameFakeObserver {
    private IGameObserver realObserver;
    private ObserverManager observerManager;
    private String token;

    /**
     * Creates a fake observer of the game used to manage the asynchronous call made to various client
     * and network communication errors
     *
     * @param token player's token of the real observer
     * @param realObserver real game observer
     * @param observerManager observer manager of the specified game
     */
    public GameFakeObserver(String token, IGameObserver realObserver, ObserverManager observerManager) {
        this.realObserver = realObserver;
        this.token = token;
        this.observerManager = observerManager;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onPlayersCreate(List<Player> players) {
        Runnable runnable = () -> {
            try {
                StringBuilder json = new StringBuilder();
                players.forEach(player -> {
                    json.append(player.toJSON().toJSONString());
                });
                realObserver.onPlayersCreate(json.toString());
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPublicObjectiveCardsDraw(List<PublicObjectiveCard> publicObjectiveCards)  {
        Runnable runnable = () -> {
            try {
                StringBuilder json = new StringBuilder();
                publicObjectiveCards.forEach(card -> json.append(card.toJSON().toJSONString()));
                realObserver.onPublicObjectiveCardsDraw(json.toString());
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onToolCardsDraw(List<ToolCard> toolCards)  {
        Runnable runnable = () -> {
            try {
                StringBuilder stringBuilder  = new StringBuilder();
                toolCards.forEach(card -> stringBuilder.append(card.toJSON().toJSONString()));
                realObserver.onToolCardsDraw(stringBuilder.toString());
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChoosePrivateObjectiveCards(List<PrivateObjectiveCard> privateObjectiveCards)  {
        Runnable runnable = () -> {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                privateObjectiveCards.forEach(card -> stringBuilder.append(card.toJSON().toJSONString()));
                realObserver.onChoosePrivateObjectiveCards(stringBuilder.toString());
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPrivateObjectiveCardDraw(List<PrivateObjectiveCard> privateObjectiveCards)  {
        Runnable runnable = () -> {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                privateObjectiveCards.forEach(card -> stringBuilder.append(card.toJSON().toJSONString()));
                realObserver.onPrivateObjectiveCardDraw(stringBuilder.toString());
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSchemaCardsDraw(List<FrontBackSchemaCard> schemaCards)  {
        Runnable runnable = () -> {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                schemaCards.forEach(doubleFace -> {
                    //TODO
                });
                realObserver.onSchemaCardsDraw(stringBuilder.toString());
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }
}
