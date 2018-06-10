package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IToolCardObserver;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;

/**
 * This class implement the IToolCardObserver and it takes care
 * of printing the notify of the toolCard on-screen.
 */
public class CLIToolCardView extends UnicastRemoteObject implements IToolCardObserver {

    /**
     * Reference to ClientGetMessage for getting message from the server.
     */
    private final transient ClientGetMessage clientGetMessage;

    /**
     * Reference of name of tooCard associated with this class.
     */
    private final String toolCardName;

    /**
     * @param cliStateView the CLI that contains all parameter.
     * @param toolCardName the name of toolCard associated at this class.
     * @throws RemoteException thrown when calling methods in a wrong sequence or passing invalid parameter values.
     */
    public CLIToolCardView(CLIStateView cliStateView, String toolCardName) throws RemoteException {
        super();
        this.clientGetMessage = cliStateView.getClientGetMessage();
        this.toolCardName = toolCardName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTokenChange(String tokens) throws IOException {
        Integer value = clientGetMessage.getValue(tokens);
        String message = "Token on " + toolCardName + "has been changed to: " + value;
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).toString(), Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCardDestroy() {
        String message = "Tool card " + toolCardName + "has been utilized and destroyed";
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).toString(), Level.STANDARD);
    }

    /**
     * @param o the other object to compare.
     * @return true if the CLIToolCardView is the same or it has the same toolCardName.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIToolCardView)) return false;
        if (!super.equals(o)) return false;
        CLIToolCardView that = (CLIToolCardView) o;
        return Objects.equals(toolCardName, that.toolCardName);
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().concat(toolCardName).hashCode();

    }

}
