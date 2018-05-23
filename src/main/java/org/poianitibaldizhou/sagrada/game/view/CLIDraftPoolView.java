package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.observers.IDraftPoolObserver;

import java.rmi.RemoteException;
import java.util.List;

public class CLIDraftPoolView extends CLIMenuView implements IDraftPoolObserver {
    private final transient DraftPool draftPool;
    private final CLIMenuView cliMenuView;

    public CLIDraftPoolView(CLIMenuView cliMenuView) throws RemoteException {
        super(cliMenuView.networkManager, cliMenuView.screenManager);
        this.cliMenuView = cliMenuView;
        draftPool = new DraftPool();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceAdd(Dice dice) {
        synchronized (draftPool) {
            draftPool.addDice(dice);
        }
        String message = cliMenuView.currentUser.getName() + " has added a dice to the draft pool";
        BuildGraphic buildGraphic = new BuildGraphic();
        bufferManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDice(dice).toString(), Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceRemove(Dice dice) {
        synchronized (draftPool) {
            try {
                draftPool.useDice(dice);
            } catch (DiceNotFoundException e) {
                cliMenuView.bufferManager.consolePrint("An error has occured when " + currentUser.getName() + " tried to remove " +
                        dice.toString() + " from the draft pool. Dice is not present in the draft pool.\n", Level.HIGH);
            } catch (EmptyCollectionException e) {
                cliMenuView.bufferManager.consolePrint("An error has occured when " + currentUser.getName() + " tried to remove " +
                        dice.toString() + " from the draft pool. Draft pool is empty.\n", Level.HIGH);
            }
        }BuildGraphic buildGraphic = new BuildGraphic();
        String message = cliMenuView.currentUser.getName() + " has removed a dice from the draft pool.";
        bufferManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDice(dice).toString(), Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDicesAdd(List<Dice> dices) {
        synchronized (draftPool) {
            draftPool.addDices(dices);
        }
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = cliMenuView.currentUser.getName() + " has added a set of dices to the draft pool.";
        bufferManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDices(dices).toString(), Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDraftPoolReroll(List<Dice> dices) {
        synchronized (draftPool) {
            draftPool.clearPool();
            draftPool.addDices(dices);
        }
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = (cliMenuView.currentUser.getName() + " has re-rolled the draft pool.");
        bufferManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDices(dices).toString(), Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDraftPoolClear() {
        synchronized (draftPool) {
            draftPool.clearPool();
        }
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = (cliMenuView.currentUser.getName() + " has cleared the draft pool.");
        bufferManager.consolePrint(buildGraphic.buildMessage(message).toString(), Level.LOW);
    }
}
