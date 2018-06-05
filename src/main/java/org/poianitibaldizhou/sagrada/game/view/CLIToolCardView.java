package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IToolCardObserver;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CLIToolCardView extends UnicastRemoteObject implements IToolCardObserver {

    private transient final CLIGameView cliGameView;
    private transient final String toolCardName;

    public CLIToolCardView(CLIGameView cliGameView, String toolCardName) throws RemoteException {
        super();
        this.cliGameView = cliGameView;
        this.toolCardName = toolCardName;
    }


    @Override
    public void onTokenChange(String tokens) throws IOException {
        Integer value = cliGameView.getClientGetMessage().getValue(tokens);
        String message = "Token on " + toolCardName + "has been changed to: " + value;
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).toString(), Level.STANDARD);
    }

    @Override
    public void onCardDestroy() throws IOException {
        String message = "Tool card " + toolCardName + "has been utilizied and destroyed";
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).toString(), Level.STANDARD);
    }
}
