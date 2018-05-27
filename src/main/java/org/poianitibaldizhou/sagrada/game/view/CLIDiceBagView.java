package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BufferManager;
import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.observers.IDrawableCollectionObserver;

import java.util.List;

public class CLIDiceBagView implements IDrawableCollectionObserver<Dice> {

    private final transient CLIGameView cliGameView;
    private final transient BufferManager bufferManager;

    public CLIDiceBagView(CLIGameView cliGameView) {
        this.cliGameView = cliGameView;
        this.bufferManager = cliGameView.bufferManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onElementAdd(Dice elem) {
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = cliGameView.getCurrentUser().getName() + " has put a dice in the dice bag.";
        bufferManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDice(elem).toString(), Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onElementsAdd(List<Dice> elemList) {
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = cliGameView.getCurrentUser().getName() + " a list of dice has been inserted in the dice bag";
        bufferManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDices(elemList).toString(), Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onElementDraw(Dice elem) {
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = cliGameView.getCurrentUser().getName() + " a dice has been drawn from the dice bag";
        bufferManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDice(elem).toString(), Level.LOW);
    }
}