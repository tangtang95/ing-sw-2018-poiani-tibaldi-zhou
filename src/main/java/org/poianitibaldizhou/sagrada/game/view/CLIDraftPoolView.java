package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IDraftPoolObserver;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Objects;

public class CLIDraftPoolView extends UnicastRemoteObject implements IDraftPoolObserver {

    private transient CLIStateView cliStateView;
    private final transient ClientGetMessage clientGetMessage;

    public CLIDraftPoolView(CLIStateView cliStateView) throws RemoteException {
        super();
        this.cliStateView = cliStateView;
        this.clientGetMessage = cliStateView.getClientGetMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceAdd(String dice) throws IOException {
        /* NOT IMPORTANT FOR THE CLI
        String message = cliStateView.getCurrentUser().getUsername() + " has added a dice to the draft pool";
        BuildGraphic buildGraphic = new BuildGraphic();
        DiceWrapper diceWrapper = clientGetMessage.getDice(dice);
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDice(diceWrapper).toString(), Level.STANDARD);
        */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceRemove(String dice) throws IOException {
        /* NOT IMPORTANT FOR THE CLI
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = cliStateView.getCurrentUser().getUsername() + " has removed a dice from the draft pool.";
        DiceWrapper diceWrapper = clientGetMessage.getDice(dice);
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDice(diceWrapper).toString(), Level.STANDARD);
        */
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onDicesAdd(String dices) throws IOException {
        /* NOT IMPORTANT FOR THE CLI
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = cliStateView.getCurrentUser().getUsername() + " has added a set of dices to the draft pool.";
        List<DiceWrapper> diceWrapperList = clientGetMessage.getDiceList(dices);
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDices(diceWrapperList).toString(), Level.STANDARD);
        */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDraftPoolReroll(String dices) throws IOException {
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = (cliStateView.getCurrentUser().getUsername() + " has re-rolled the draft pool.");
        List<DiceWrapper> diceWrapperList = clientGetMessage.getDiceList(dices);
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDices(diceWrapperList).toString(), Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDraftPoolClear() {
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = (cliStateView.getCurrentUser().getUsername() + " has cleared the draft pool.");
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).toString(), Level.STANDARD);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIDraftPoolView)) return false;
        if (!super.equals(o)) return false;
        CLIDraftPoolView that = (CLIDraftPoolView) o;
        return Objects.equals(cliStateView, that.cliStateView);
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }
}
