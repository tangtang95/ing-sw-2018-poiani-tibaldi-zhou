package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.ISchemaCardObserver;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.PositionWrapper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;

public class CLISchemaCardView extends UnicastRemoteObject implements ISchemaCardObserver {

    private transient CLIGameView cliGameView;

    public CLISchemaCardView(CLIGameView cliGameView) throws RemoteException {
        super();
        this.cliGameView = cliGameView;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLISchemaCardView)) return false;
        if (!super.equals(o)) return false;
        CLISchemaCardView that = (CLISchemaCardView) o;
        return Objects.equals(cliGameView, that.cliGameView);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cliGameView);
    }

    @Override
    public void onPlaceDice(String message) throws IOException {
        PositionWrapper positionWrapper = cliGameView.getClientGetMessage().getPosition(message);
        DiceWrapper diceWrapper = cliGameView.getClientGetMessage().getDice(message);
        String printMessage = cliGameView.getCurrentUser().getUsername() + " has placed a dice in position " + positionWrapper.toString();
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(printMessage).buildGraphicDice(diceWrapper).toString(), Level.STANDARD);
    }

    @Override
    public void onDiceRemove(String message) throws IOException {
        PositionWrapper positionWrapper = cliGameView.getClientGetMessage().getPosition(message);
        DiceWrapper diceWrapper = cliGameView.getClientGetMessage().getDice(message);
        String printMessage = cliGameView.getCurrentUser().getUsername() + " has removed a dice in position " + positionWrapper.toString();
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(printMessage).buildGraphicDice(diceWrapper).toString(), Level.STANDARD);
    }
}
