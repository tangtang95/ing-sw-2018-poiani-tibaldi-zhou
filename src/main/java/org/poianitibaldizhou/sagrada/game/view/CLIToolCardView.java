package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IToolCardObserver;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;

public class CLIToolCardView extends UnicastRemoteObject implements IToolCardObserver {

    private transient final CLIGameView cliGameView;
    private transient final String toolCardName;

    public CLIToolCardView(CLIGameView cliGameView, String toolCardName) throws RemoteException {
        super();
        this.cliGameView = cliGameView;
        this.toolCardName = toolCardName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTokenChange(String tokens) throws IOException {
        Integer value = cliGameView.getClientGetMessage().getValue(tokens);
        String message = "Token on " + toolCardName + "has been changed to: " + value;
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).toString(), Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCardDestroy() throws IOException {
        String message = "Tool card " + toolCardName + "has been utilizied and destroyed";
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).toString(), Level.STANDARD);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cliGameView);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIDraftPoolView)) return false;
        if (!super.equals(o)) return false;
        CLIToolCardView that = (CLIToolCardView) o;
        return Objects.equals(cliGameView, that.cliGameView);
    }
}
