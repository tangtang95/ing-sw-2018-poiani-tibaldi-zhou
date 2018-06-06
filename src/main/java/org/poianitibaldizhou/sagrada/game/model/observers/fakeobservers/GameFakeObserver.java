package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.game.model.cards.FrontBackSchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.IGameFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IGameObserver;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.network.protocol.ServerCreateMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameFakeObserver implements IGameFakeObserver {
    private IGameObserver realObserver;
    private GameObserverManager observerManager;
    private String token;

    private ServerCreateMessage serverCreateMessage;

    /**
     * Creates a fake observer of the game used to manage the asynchronous call made to various client
     * and network communication errors
     *
     * @param token player's token of the real observer
     * @param realObserver real game observer
     * @param observerManager observer manager of the specified game
     */
    public GameFakeObserver(String token, IGameObserver realObserver, GameObserverManager observerManager) {
        this.token = token;
        this.observerManager = observerManager;
        this.realObserver = realObserver;

        serverCreateMessage = new ServerCreateMessage();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onPlayersCreate(List<Player> players) {
        Runnable runnable = () -> {
            try {
                ArrayList users = new ArrayList();
                players.forEach(player -> users.add(player.getUser()));
                realObserver.onPlayersCreate(serverCreateMessage.createUserList(users).buildMessage());
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
                realObserver.onPublicObjectiveCardsDraw(serverCreateMessage.createPublicObjectiveCardList(publicObjectiveCards).buildMessage());
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
                realObserver.onToolCardsDraw(serverCreateMessage.createToolCardList(toolCards).buildMessage());
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
                realObserver.onChoosePrivateObjectiveCards(serverCreateMessage.createPrivateObjectiveCardList(privateObjectiveCards).buildMessage());
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
                realObserver.onPrivateObjectiveCardDraw(serverCreateMessage.createPrivateObjectiveCardList(privateObjectiveCards).buildMessage());
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
                realObserver.onSchemaCardsDraw(serverCreateMessage.createFrontBackSchemaCardList(schemaCards).buildMessage());
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }
}
