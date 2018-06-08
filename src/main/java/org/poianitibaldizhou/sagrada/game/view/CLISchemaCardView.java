package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.ISchemaCardObserver;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.PositionWrapper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;

/**
 * This class implement the ISchemaCardObserver and it takes care
 * of printing the notify of the schema cards on-screen
 */
public class CLISchemaCardView extends UnicastRemoteObject implements ISchemaCardObserver {

    /**
     * Reference to CLIStateView for passing the parameter.
     */
    private final transient CLIStateView cliStateView;

    /**
     * Reference to ClientGetMessage for getting message from the server.
     */
    private final transient ClientGetMessage clientGetMessage;

    /**
     * Reference to my username.
     */
    private final String username;

    /**
     * Constructor.
     *
     * @param cliStateView the CLI that contains all parameter.
     * @param username my username in the current game.
     * @throws RemoteException thrown when calling methods in a wrong sequence or passing invalid parameter values.
     */
    public CLISchemaCardView(CLIStateView cliStateView, String username) throws RemoteException {
        super();
        this.cliStateView = cliStateView;
        this.username = username;
        this.clientGetMessage = cliStateView.getClientGetMessage();
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * @param o the other object to compare.
     * @return true if the CLISchemaCard is the same or the username is the same and the CLIStateView is the same.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLISchemaCardView)) return false;
        if (!super.equals(o)) return false;
        CLISchemaCardView that = (CLISchemaCardView) o;
        return Objects.equals(cliStateView, that.cliStateView) &&
                Objects.equals(username, that.username);
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().concat(username).hashCode();
    }
}
