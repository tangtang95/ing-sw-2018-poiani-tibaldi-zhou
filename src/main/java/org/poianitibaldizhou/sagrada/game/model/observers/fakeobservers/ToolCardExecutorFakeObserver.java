package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.IToolCardExecutorFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.network.protocol.ServerCreateMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ToolCardExecutorFakeObserver implements IToolCardExecutorFakeObserver {

    private String token;
    private GameObserverManager observerManager;
    private IToolCardExecutorObserver realObserver;

    private ServerCreateMessage serverCreateMessage;

    /**
     * Creates a fake observer of the draft pool used to manage the asynchronous call made to various client
     * and network communication errors
     *
     * @param token player's token of the real observer
     * @param realObserver real draft pool observer
     * @param observerManager observer manager of the specified game
     */
    public ToolCardExecutorFakeObserver(String token, GameObserverManager observerManager, IToolCardExecutorObserver realObserver) {
        this.token = token;
        this.observerManager = observerManager;
        this.realObserver = realObserver;

        serverCreateMessage = new ServerCreateMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedDice(List<Dice> diceList) {
        Runnable runnable = () -> {
            try {
                realObserver.notifyNeedDice(serverCreateMessage.createDiceList(diceList).buildMessage());
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
    public void notifyNeedNewValue() {
        Runnable runnable = () -> {
            try {
                realObserver.notifyNeedNewValue();
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
    public void notifyNeedColor(Set<Color> colors) {
        Runnable runnable = () -> {
            try {
                ArrayList<Color> colorArrayList = new ArrayList<>(colors);
                realObserver.notifyNeedColor(serverCreateMessage.createColorListMessage(colorArrayList).buildMessage());
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
    public void notifyNeedNewDeltaForDice(int diceValue, int value) {
        Runnable runnable = () -> {
            try {
                realObserver.notifyNeedNewDeltaForDice(serverCreateMessage.createMessageValue(value).createDiceValueMessage(diceValue).buildMessage());
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
    public void notifyNeedDiceFromRoundTrack(RoundTrack roundTrack) {
        Runnable runnable = () -> {
            try {
                realObserver.notifyNeedDiceFromRoundTrack(serverCreateMessage.createRoundTrackMessage(roundTrack).buildMessage());
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
    public void notifyNeedPosition(SchemaCard schemaCard) {
        Runnable runnable = () -> {
            try {
                System.out.println("IN NOTIFY THREAD: " + schemaCard.toString());
                realObserver.notifyNeedPosition(serverCreateMessage.createSchemaCardMessage(schemaCard).buildMessage());
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
    public void notifyNeedDicePositionOfCertainColor(Color color, SchemaCard schemaCard) {
        Runnable runnable = () -> {
            try {
                realObserver.notifyNeedDicePositionOfCertainColor(serverCreateMessage.createSchemaCardMessage(schemaCard).createColorMessage(color).buildMessage());
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
    public void notifyRepeatAction() {
        Runnable runnable = () -> {
            try {
                realObserver.notifyRepeatAction();
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
    public void notifyCommandInterrupted(CommandFlow error) {
        Runnable runnable = () -> {
            try {
                realObserver.notifyCommandInterrupted(serverCreateMessage.createCommandFlowMessage(error).buildMessage());
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
    public void notifyNeedContinueAnswer() {
        Runnable runnable = () -> {
            try {
                realObserver.notifyNeedContinueAnswer();
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
    public void notifyDiceReroll(Dice dice) {
        Runnable runnable = () -> {
            try {
                realObserver.notifyDiceReroll(serverCreateMessage.createDiceMessage(dice).buildMessage());
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);

    }

    @Override
    public void notifyExecutionEnded() throws IOException {
        Runnable runnable = () -> {
            try {
                realObserver.notifyExecutionEnded();
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }
}
