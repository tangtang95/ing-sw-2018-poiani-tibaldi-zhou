package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IDrawableCollectionObserver;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;

public class CLIDiceBagView extends UnicastRemoteObject implements IDrawableCollectionObserver {

    private final transient CLIStateView cliStateView;
    private final transient ClientGetMessage clientGetMessage;

    public CLIDiceBagView(CLIStateView cliStateView) throws RemoteException {
        super();
        this.cliStateView = cliStateView;
        this.clientGetMessage = cliStateView.getClientGetMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onElementAdd(String elem) throws IOException {
        /* NOT IMPORTANT FOR THE CLI
        BuildGraphic buildGraphic = new BuildGraphic();
        if (cliStateView.getCurrentUser() != null) {
            String message = cliStateView.getCurrentUser().getUsername() + " has put a dice in the dice bag.";
            DiceWrapper diceWrapper = clientGetMessage.getDiceElem(elem);
            PrinterManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDice(diceWrapper).toString(), Level.STANDARD);
        }
        */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onElementsAdd(String elemList) throws IOException {
        /* NOT IMPORTANT FOR THE CLI
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = cliStateView.getCurrentUser().getUsername() + " a list of dice has been inserted in the dice bag";
        List<DiceWrapper> diceWrapperList = clientGetMessage.getDiceElemList(elemList);
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDices(diceWrapperList).toString(), Level.STANDARD);
        */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onElementDraw(String elem) throws IOException {
        /* NOT IMPORTANT FOR THE CLI
        BuildGraphic buildGraphic = new BuildGraphic();
        String message;
        if (cliStateView.getCurrentUser() != null) {
            message = cliStateView.getCurrentUser().getUsername() + " a dice has been drawn from the dice bag";
        } else {
            message = "A dice has been drawn from the dice bag";
        }
        DiceWrapper diceWrapper = clientGetMessage.getDiceElem(elem);
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDice(diceWrapper).toString(), Level.STANDARD);
        */
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof CLIDiceBagView)) return false;
        if (!super.equals(o)) return false;
        CLIDiceBagView that = (CLIDiceBagView) o;
        return Objects.equals(cliStateView, that.cliStateView) &&
                Objects.equals(clientGetMessage, that.clientGetMessage);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), cliStateView, clientGetMessage);
    }
}