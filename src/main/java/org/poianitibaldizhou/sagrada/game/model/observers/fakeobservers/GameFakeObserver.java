package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.observers.ObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IGameObserver;
import org.poianitibaldizhou.sagrada.game.model.players.Player;

import java.io.IOException;
import java.util.List;

public class GameFakeObserver implements IGameObserver {
    private IGameObserver realObserver;
    private ObserverManager observerManager;
    private String token;

    public GameFakeObserver(String token, IGameObserver realObserver, ObserverManager observerManager) {
        if(realObserver instanceof GameFakeObserver)
            throw new IllegalArgumentException();
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
                realObserver.onPlayersCreate(players);
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        Thread t = new Thread(runnable);
        t.start();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPublicObjectiveCardsDraw(List<PublicObjectiveCard> publicObjectiveCards)  {
        Runnable runnable = () -> {
            try {
                realObserver.onPublicObjectiveCardsDraw(publicObjectiveCards);
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        Thread t = new Thread(runnable);
        t.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onToolCardsDraw(List<ToolCard> toolCards)  {
        Runnable runnable = () -> {
            try {
                realObserver.onToolCardsDraw(toolCards);
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        Thread t = new Thread(runnable);
        t.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChoosePrivateObjectiveCards(List<PrivateObjectiveCard> privateObjectiveCards)  {
        Runnable runnable = () -> {
            try {
                realObserver.onChoosePrivateObjectiveCards(privateObjectiveCards);
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        Thread t = new Thread(runnable);
        t.start();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPrivateObjectiveCardDraw(List<PrivateObjectiveCard> privateObjectiveCards)  {
        Runnable runnable = () -> {
            try {
                realObserver.onPrivateObjectiveCardDraw(privateObjectiveCards);
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        Thread t = new Thread(runnable);
        t.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSchemaCardsDraw(List<List<SchemaCard>> schemaCards)  {
        Runnable runnable = () -> {
            try {
                realObserver.onSchemaCardsDraw(schemaCards);
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        Thread t = new Thread(runnable);
        t.start();
    }
}
