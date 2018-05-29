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
        try {
            realObserver.onPlayersCreate(players);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPublicObjectiveCardsDraw(List<PublicObjectiveCard> publicObjectiveCards)  {
        try {
            realObserver.onPublicObjectiveCardsDraw(publicObjectiveCards);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onToolCardsDraw(List<ToolCard> toolCards)  {
        try {
            realObserver.onToolCardsDraw(toolCards);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChoosePrivateObjectiveCards(List<PrivateObjectiveCard> privateObjectiveCards)  {
        try {
            realObserver.onChoosePrivateObjectiveCards(privateObjectiveCards);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPrivateObjectiveCardDraw(List<PrivateObjectiveCard> privateObjectiveCards)  {
        try {
            realObserver.onPrivateObjectiveCardDraw(privateObjectiveCards);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSchemaCardsDraw(List<List<SchemaCard>> schemaCards)  {
        try {
            realObserver.onSchemaCardsDraw(schemaCards);
        } catch (IOException e) {
            observerManager.signalDisconnection(token);
        }
    }
}
