package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IDrawableCollectionObserver;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class CLIDiceBagView extends UnicastRemoteObject implements IDrawableCollectionObserver {

    private final transient CLIGameView cliGameView;

    public CLIDiceBagView(CLIGameView cliGameView) throws RemoteException {
        super();
        this.cliGameView = cliGameView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onElementAdd(String elem) throws IOException {
        BuildGraphic buildGraphic = new BuildGraphic();

        String message = cliGameView.getCurrentUser().getUsername() + " has put a dice in the dice bag.";
        DiceWrapper diceWrapper = cliGameView.getClientGetMessage().getDice(elem);
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDice(diceWrapper).toString(), Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onElementsAdd(String elemList) throws IOException {
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = cliGameView.getCurrentUser().getUsername() + " a list of dice has been inserted in the dice bag";
        List<DiceWrapper> diceWrapperList = cliGameView.getClientGetMessage().getDiceList(elemList);
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDices(diceWrapperList).toString(), Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onElementDraw(String elem) throws IOException {
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = cliGameView.getCurrentUser().getUsername() + " a dice has been drawn from the dice bag";
        DiceWrapper diceWrapper = cliGameView.getClientGetMessage().getDice(elem);
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDice(diceWrapper).toString(), Level.STANDARD);
    }
}