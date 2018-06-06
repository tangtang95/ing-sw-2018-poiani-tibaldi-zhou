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

public class CLIToolCardView extends UnicastRemoteObject implements IToolCardObserver {

    private final transient ClientGetMessage clientGetMessage;
    private final transient String toolCardName;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIToolCardView)) return false;
        if (!super.equals(o)) return false;
        CLIToolCardView that = (CLIToolCardView) o;
        return Objects.equals(clientGetMessage, that.clientGetMessage) &&
                Objects.equals(toolCardName, that.toolCardName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), clientGetMessage, toolCardName);

    }

}
