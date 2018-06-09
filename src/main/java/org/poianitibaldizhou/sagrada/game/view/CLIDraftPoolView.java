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

/**
 * This class implement the IDraftPoolObserver and it takes care
 * of printing the notify of draftPool on-screen.
 */
public class CLIDraftPoolView extends UnicastRemoteObject implements IDraftPoolObserver {

    /**
     * Reference to CLIStateView for passing the parameter.
     */
    private transient CLIStateView cliStateView;

    /**
     * Reference to ClientGetMessage for getting message from the server.
     */
    private final transient ClientGetMessage clientGetMessage;

    /**
     * Constructor.
     *
     * @param cliStateView the CLI that contains all parameter.
     * @throws RemoteException thrown when calling methods in a wrong sequence or passing invalid parameter values.
     */
    CLIDraftPoolView(CLIStateView cliStateView) throws RemoteException {
        super();
        this.cliStateView = cliStateView;
        this.clientGetMessage = cliStateView.getClientGetMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceAdd(String dice) {
        /* NOT IMPORTANT FOR THE CLI*/
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceRemove(String dice) {
        /* NOT IMPORTANT FOR THE CLI*/
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onDicesAdd(String dices) {
        /* NOT IMPORTANT FOR THE CLI*/
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDraftPoolReroll(String dices) throws IOException {
        BuildGraphic buildGraphic = new BuildGraphic();
        String message = (cliStateView.getCurrentUser().getUsername() + " has re-rolled the draft pool.");
        List<DiceWrapper> diceWrapperList = clientGetMessage.getDiceList(dices);
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDices(diceWrapperList).toString(),
                Level.INFORMATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDraftPoolClear() {
        /* NOT IMPORTANT FOR THE CLI*/
    }

    /**
     * @param o the other object to compare.
     * @return true if the CLIStateView is the same.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIDraftPoolView)) return false;
        if (!super.equals(o)) return false;
        CLIDraftPoolView that = (CLIDraftPoolView) o;
        return Objects.equals(cliStateView, that.cliStateView);
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }
}
