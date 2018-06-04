package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.ISchemaCardFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.ISchemaCardObserver;
import org.poianitibaldizhou.sagrada.network.protocol.JSONServerProtocol;
import org.poianitibaldizhou.sagrada.network.protocol.ServerCreateMessage;

import java.io.IOException;

public class SchemaCardFakeObserver implements ISchemaCardFakeObserver {

    private String token;
    private GameObserverManager observerManager;
    private ISchemaCardObserver realObserver;

    private ServerCreateMessage serverCreateMessage;

    /**
     * Creates a fake observer of the schema card used to manage the asynchronous call made to various client
     * and network communication errors
     *
     * @param token player's token of the real observer
     * @param realObserver real schema card observer
     * @param observerManager observer manager of the specified game
     */
    public SchemaCardFakeObserver(String token, GameObserverManager observerManager, ISchemaCardObserver realObserver) {
        this.token = token;
        this.observerManager = observerManager;
        this.realObserver = realObserver;

        serverCreateMessage = new ServerCreateMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPlaceDice(Dice dice, Position position)  {
        Runnable runnable = () -> {
            try {
                realObserver.onPlaceDice(serverCreateMessage.createDiceMessage(dice).createPositionMessage(position).buildMessage());
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
    public void onDiceRemove(Dice dice, Position position)  {
        Runnable runnable = () -> {
            try {
                realObserver.onDiceRemove(serverCreateMessage.createDiceMessage(dice).createPositionMessage(position).buildMessage());
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }
}
