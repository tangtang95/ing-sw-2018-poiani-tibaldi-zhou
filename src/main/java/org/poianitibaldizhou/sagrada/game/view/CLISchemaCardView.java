package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.ISchemaCardObserver;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.PositionWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;

public class CLISchemaCardView extends UnicastRemoteObject implements ISchemaCardObserver {

    private final transient CLIStateView cliStateView;
    private final transient ClientGetMessage clientGetMessage;
    private final String username;

    public CLISchemaCardView(CLIStateView cliStateView, String username) throws RemoteException {
        super();
        this.cliStateView = cliStateView;
        this.username = username;
        this.clientGetMessage = cliStateView.getClientGetMessage();
    }

    @Override
    public void onPlaceDice(String message) throws IOException {
        PositionWrapper positionWrapper = clientGetMessage.getPosition(message);
        DiceWrapper diceWrapper = clientGetMessage.getDice(message);
        String printMessage = cliStateView.getCurrentUser().getUsername() + " has placed a dice in position " +
                positionWrapper.toString();
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(printMessage).buildGraphicDice(diceWrapper).toString(),
                Level.INFORMATION);
    }

    @Override
    public void onDiceRemove(String message) throws IOException {
        PositionWrapper positionWrapper = clientGetMessage.getPosition(message);
        DiceWrapper diceWrapper = clientGetMessage.getDice(message);
        String printMessage = cliStateView.getCurrentUser().getUsername() + " has removed a dice in position " +
                positionWrapper.toString();
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(printMessage).buildGraphicDice(diceWrapper).toString(),
                Level.INFORMATION);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLISchemaCardView)) return false;
        if (!super.equals(o)) return false;
        CLISchemaCardView that = (CLISchemaCardView) o;
        return Objects.equals(cliStateView, that.cliStateView) &&
                Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().concat(username).hashCode();
    }
}
