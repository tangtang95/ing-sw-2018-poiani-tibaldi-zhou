package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.observers.IDrawableCollectionObserver;

import java.rmi.RemoteException;
import java.util.List;

public class CLIDiceBagView extends CLIMenuView implements IDrawableCollectionObserver<Dice> {

    private final CLIMenuView cliMenuView;

    public CLIDiceBagView(CLIMenuView cliMenuView) throws RemoteException {
        super(cliMenuView.networkManager, cliMenuView.screenManager);
        this.cliMenuView = cliMenuView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onElementAdd(Dice elem) {
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = cliMenuView.currentUser.getName() + " has put a dice in the dice bag.";
        bufferManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDice(elem).toString(), Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onElementsAdd(List<Dice> elemList) {
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = cliMenuView.currentUser.getName() + " a list of dice has been inserted in the dice bag";
        bufferManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDices(elemList).toString(), Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onElementDraw(Dice elem) {
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = cliMenuView.currentUser.getName() + " a dice has been drawn from the dice bag";
        bufferManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDice(elem).toString(), Level.LOW);
    }
}