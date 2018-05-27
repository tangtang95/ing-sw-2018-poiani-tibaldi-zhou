package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BufferManager;
import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.observers.IDraftPoolObserver;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.rmi.RemoteException;
import java.util.List;

public class CLIDraftPoolView implements IDraftPoolObserver {
    private final transient DraftPool draftPool;
    private final User currentUser;
    private final BufferManager bufferManager;

    CLIDraftPoolView(CLIGameView cliGameView) {
        this.bufferManager = cliGameView.bufferManager;
        this.draftPool = new DraftPool();
        this.currentUser = cliGameView.getCurrentUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceAdd(Dice dice) throws RemoteException {
        synchronized (draftPool) {
            draftPool.addDice(dice);
        }
        String message = currentUser.getName() + " has added a dice to the draft pool";
        BuildGraphic buildGraphic = new BuildGraphic();
        bufferManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDice(dice).toString(), Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceRemove(Dice dice) throws RemoteException {
        synchronized (draftPool) {
            try {
                draftPool.useDice(dice);
            } catch (DiceNotFoundException e) {
                bufferManager.consolePrint("An error has occured when " + currentUser.getName() +
                        " tried to remove " +
                        dice.toString() + " from the draft pool. Dice is not present in the draft pool.\n", Level.HIGH);
                return;
            } catch (EmptyCollectionException e) {
                bufferManager.consolePrint("An error has occured when " + currentUser.getName() +
                        " tried to remove " +
                        dice.toString() + " from the draft pool. Draft pool is empty.\n", Level.HIGH);
                return;
            }
        }
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = currentUser.getName() + " has removed a dice from the draft pool.";
        bufferManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDice(dice).toString(), Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDicesAdd(List<Dice> dices) throws RemoteException {
        synchronized (draftPool) {
            draftPool.addDices(dices);
        }
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = currentUser.getName() + " has added a set of dices to the draft pool.";
        bufferManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDices(dices).toString(), Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDraftPoolReroll(List<Dice> dices) throws RemoteException {
        synchronized (draftPool) {
            draftPool.clearPool();
            draftPool.addDices(dices);
        }
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = (currentUser.getName() + " has re-rolled the draft pool.");
        bufferManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDices(dices).toString(), Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDraftPoolClear() throws RemoteException {
        synchronized (draftPool) {
            draftPool.clearPool();
        }
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = (currentUser.getName() + " has cleared the draft pool.");
        bufferManager.consolePrint(buildGraphic.buildMessage(message).toString(), Level.LOW);
    }

    public DraftPool getDraftPool() {
        return draftPool;
    }
}
