package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.observers.IDraftPoolObserver;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.rmi.RemoteException;
import java.util.List;

public class CLIDraftPoolView extends CLIMenuView implements IDraftPoolObserver {
    private final transient DraftPool draftPool;
    private final User currentUser;

    public CLIDraftPoolView(CLIGameView cliGameView) throws RemoteException {
        super(cliGameView.networkManager, cliGameView.screenManager, cliGameView.bufferManager);
        draftPool = new DraftPool();
        currentUser = cliGameView.getCurrentUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceAdd(Dice dice) {
        synchronized (draftPool) {
            draftPool.addDice(dice);
        }
        StringBuilder stringBuilder = new StringBuilder(currentUser.getName() + " has added a dice to the draft pool.\n");
        buildGraphicDice(dice, stringBuilder);
        bufferManager.consolePrint(stringBuilder.toString(), Level.LOW);
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
                        dice.toString() + " from the draft pool. Draft pool is empty.\n", Level.HIGH);;
            }
        }
        StringBuilder stringBuilder = new StringBuilder(currentUser.getName() + " has removed a dice from the draft pool.\n");
        buildGraphicDice(dice, stringBuilder);
        bufferManager.consolePrint(stringBuilder.toString(), Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDicesAdd(List<Dice> dices) {
        synchronized (draftPool) {
            draftPool.addDices(dices);
        }
        StringBuilder stringBuilder = new StringBuilder(currentUser.getName() + " has added a set of dices to the draft pool.\n");
        buildGraphicDices(stringBuilder, dices, 0, dices.size());
        bufferManager.consolePrint(stringBuilder.toString(), Level.LOW);
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
        StringBuilder stringBuilder = new StringBuilder(currentUser.getName() + " has re-rolled the draft pool.\n");
        buildGraphicDices(stringBuilder, dices, 0, dices.size());
        bufferManager.consolePrint(stringBuilder.toString(), Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDraftPoolClear() {
        synchronized (draftPool) {
            draftPool.clearPool();
        }
        StringBuilder stringBuilder = new StringBuilder(currentUser.getName() + " has cleared the draft pool.\n");
        bufferManager.consolePrint(stringBuilder.toString(), Level.LOW);
    }

    public static void buildGraphicsDraftPool(StringBuilder stringBuilder) {
        // TODO
    }
}
