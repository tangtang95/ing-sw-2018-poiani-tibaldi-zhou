package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BufferManager;
import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.observers.IDraftPoolObserver;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.rmi.RemoteException;
import java.util.List;

public class CLIDraftPoolView implements IDraftPoolObserver {
    private final transient DraftPool draftPool;
    private final CLIGameView cliGameView;
    private final BufferManager bufferManager;

    public CLIDraftPoolView(CLIGameView cliGameView, BufferManager bufferManager) {
        this.cliGameView = cliGameView;
        this.bufferManager = bufferManager;
        draftPool = new DraftPool();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceAdd(Dice dice) throws RemoteException {
        User user = cliGameView.getCurrentUser();
        synchronized (draftPool) {
            draftPool.addDice(dice);
        }
        String message = user.getName() + " has added a dice to the draft pool";
        BuildGraphic buildGraphic = new BuildGraphic();
        bufferManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDice(dice).toString(), Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceRemove(Dice dice) throws RemoteException {
        User user = cliGameView.getCurrentUser();
        synchronized (draftPool) {
            try {
                draftPool.useDice(dice);
            } catch (DiceNotFoundException e) {
                cliGameView.bufferManager.consolePrint("An error has occured when " + user.getName() + " tried to remove " +
                        dice.toString() + " from the draft pool. Dice is not present in the draft pool.\n", Level.HIGH);
                return;
            } catch (EmptyCollectionException e) {
                cliGameView.bufferManager.consolePrint("An error has occured when " + user.getName() + " tried to remove " +
                        dice.toString() + " from the draft pool. Draft pool is empty.\n", Level.HIGH);
                return;
            }
        }
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = user.getName() + " has removed a dice from the draft pool.";
        bufferManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDice(dice).toString(), Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDicesAdd(List<Dice> dices) throws RemoteException {
        User user = cliGameView.getCurrentUser();
        synchronized (draftPool) {
            draftPool.addDices(dices);
        }
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = user.getName() + " has added a set of dices to the draft pool.";
        bufferManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDices(dices).toString(), Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDraftPoolReroll(List<Dice> dices) throws RemoteException {
        User user = cliGameView.getCurrentUser();
        synchronized (draftPool) {
            draftPool.clearPool();
            draftPool.addDices(dices);
        }
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = (user.getName() + " has re-rolled the draft pool.");
        bufferManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDices(dices).toString(), Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDraftPoolClear() throws RemoteException {
        User user = cliGameView.getCurrentUser();
        synchronized (draftPool) {
            draftPool.clearPool();
        }
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = (user.getName() + " has cleared the draft pool.");
        bufferManager.consolePrint(buildGraphic.buildMessage(message).toString(), Level.LOW);
    }
}
