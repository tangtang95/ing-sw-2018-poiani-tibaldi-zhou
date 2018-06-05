package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IDraftPoolObserver;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class CLIDraftPoolView extends UnicastRemoteObject implements IDraftPoolObserver {

    private transient CLIGameView cliGameView;

    public CLIDraftPoolView(CLIGameView cliGameView) throws RemoteException {
        super();
        this.cliGameView = cliGameView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceAdd(String dice) throws IOException {
        String message = cliGameView.getCurrentUser().getUsername() + " has added a dice to the draft pool";
        BuildGraphic buildGraphic = new BuildGraphic();
        DiceWrapper diceWrapper = cliGameView.getClientGetMessage().getDice(message);
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDice(diceWrapper).toString(), Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceRemove(String dice) throws IOException {
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = cliGameView.getCurrentUser().getUsername() + " has removed a dice from the draft pool.";
        DiceWrapper diceWrapper = cliGameView.getClientGetMessage().getDice(message);
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDice(diceWrapper).toString(), Level.STANDARD);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onDicesAdd(String dices) throws IOException {
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = cliGameView.getCurrentUser().getUsername() + " has added a set of dices to the draft pool.";
        List<DiceWrapper> diceWrapperList = cliGameView.getClientGetMessage().getDiceList(dices);
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDices(diceWrapperList).toString(), Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDraftPoolReroll(String dices) throws IOException {
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = (cliGameView.getCurrentUser().getUsername() + " has re-rolled the draft pool.");
        List<DiceWrapper> diceWrapperList = cliGameView.getClientGetMessage().getDiceList(dices);
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDices(diceWrapperList).toString(), Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDraftPoolClear() {
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = (cliGameView.getCurrentUser().getUsername() + " has cleared the draft pool.");
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).toString(), Level.STANDARD);
    }
}
