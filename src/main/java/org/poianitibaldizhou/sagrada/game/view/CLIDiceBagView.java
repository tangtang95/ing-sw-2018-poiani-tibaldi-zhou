package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IDrawableCollectionObserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class CLIDiceBagView extends UnicastRemoteObject implements IDrawableCollectionObserver<Dice> {

    private final transient CLIGameView cliGameView;

    CLIDiceBagView(CLIGameView cliGameView) throws RemoteException {
        super();
        this.cliGameView = cliGameView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onElementAdd(Dice elem) {
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = cliGameView.getCurrentUser().getName() + " has put a dice in the dice bag.";
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDice(elem).toString(), Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onElementsAdd(List<Dice> elemList) {
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = cliGameView.getCurrentUser().getName() + " a list of dice has been inserted in the dice bag";
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDices(elemList).toString(), Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onElementDraw(Dice elem) {
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = cliGameView.getCurrentUser().getName() + " a dice has been drawn from the dice bag";
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDice(elem).toString(), Level.STANDARD);
    }
}