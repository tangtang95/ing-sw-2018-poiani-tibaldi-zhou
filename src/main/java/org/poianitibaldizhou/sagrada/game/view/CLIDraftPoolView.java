package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IDraftPoolObserver;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class CLIDraftPoolView extends UnicastRemoteObject implements IDraftPoolObserver {
    private final DraftPool draftPool;
   // private final User currentUser;

    CLIDraftPoolView(CLIGameView cliGameView) throws RemoteException {
        super();
        this.draftPool = new DraftPool();
        //this.currentUser = cliGameView.getCurrentUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceAdd(Dice dice) {
        synchronized (draftPool) {
            draftPool.addDice(dice);
        }
        //String message = currentUser.getName() + " has added a dice to the draft pool";
        BuildGraphic buildGraphic = new BuildGraphic();
       //PrinterManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDice(dice).toString(), Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceRemove(Dice dice) {
        /*
        synchronized (draftPool) {
            try {
                draftPool.useDice(dice);
            } catch (DiceNotFoundException e) {
                PrinterManager.consolePrint("An error has occured when " + currentUser.getName() +
                        " tried to remove " +
                        dice.toString() + " from the draft pool. Dice is not present in the draft pool.\n", Level.INFORMATION);
                return;
            } catch (EmptyCollectionException e) {
                PrinterManager.consolePrint("An error has occured when " + currentUser.getName() +
                        " tried to remove " +
                        dice.toString() + " from the draft pool. Draft pool is empty.\n", Level.INFORMATION);
                return;
            }
        }
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = currentUser.getName() + " has removed a dice from the draft pool.";
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDice(dice).toString(), Level.STANDARD);
    */
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onDicesAdd(List<Dice> dices) {
        /*
        synchronized (draftPool) {
            draftPool.addDices(dices);
        }
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = currentUser.getName() + " has added a set of dices to the draft pool.";
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDices(dices).toString(), Level.STANDARD);

    */}

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDraftPoolReroll(List<Dice> dices) {/*
        synchronized (draftPool) {
            draftPool.clearPool();
            draftPool.addDices(dices);
        }
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = (currentUser.getName() + " has re-rolled the draft pool.");
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDices(dices).toString(), Level.STANDARD);
    */}

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDraftPoolClear() {/*
        synchronized (draftPool) {
            draftPool.clearPool();
        }
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = (currentUser.getName() + " has cleared the draft pool.");
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).toString(), Level.STANDARD);
    */}

    public DraftPool getDraftPool() {
        return draftPool;
    }
}
