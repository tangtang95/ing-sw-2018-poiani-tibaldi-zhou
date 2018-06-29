package org.poianitibaldizhou.sagrada.cli.game;

import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IRoundTrackObserver;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Objects;

/**
 * This class implement the IRoundTrackObserver and it takes care
 * of printing the notify of roundTrack on-screen
 */
public class CLIRoundTrackView extends UnicastRemoteObject implements IRoundTrackObserver {

    /**
     * Reference to CLIStateView for passing the parameter.
     */
    private final transient CLIStateView cliStateView;

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
    CLIRoundTrackView(CLIStateView cliStateView) throws RemoteException {
        super();
        this.cliStateView = cliStateView;
        this.clientGetMessage = cliStateView.getClientGetMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDicesAddToRound(String message) throws IOException {
        Integer round = clientGetMessage.getValue(message);
        List<DiceWrapper> diceWrapperList = clientGetMessage.getDiceList(message);
        String printMessage = String.format(ClientMessage.ADD_DICE_LIST_TO_ROUND,cliStateView.getCurrentUser().getUsername(),round);
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(printMessage).buildGraphicDices(diceWrapperList).toString(),
                Level.INFORMATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceAddToRound(String message) throws IOException {
        Integer round = clientGetMessage.getValue(message);
        DiceWrapper diceWrapper = clientGetMessage.getDice(message);
        String printMessage = String.format(ClientMessage.ADD_DICE_TO_ROUND,
                cliStateView.getCurrentUser().getUsername(),round);
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(printMessage).buildGraphicDice(diceWrapper).toString(),
                Level.INFORMATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceRemoveFromRound(String message) throws IOException {
        Integer round  = clientGetMessage.getValue(message);
        DiceWrapper diceWrapper = clientGetMessage.getDice(message);
        String printMessage = String.format(ClientMessage.REMOVE_DICE_FROM_ROUND_TRACK,
                cliStateView.getCurrentUser().getUsername(),round);
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(printMessage).buildGraphicDice(diceWrapper).toString(),
                Level.INFORMATION);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceSwap(String message) throws IOException {
        DiceWrapper oldDice = clientGetMessage.getOldDice(message);
        DiceWrapper newDice = clientGetMessage.getNewDice(message);
        Integer round = clientGetMessage.getValue(message);

        String printMessage = String.format(ClientMessage.SWAP_DICE,cliStateView.getCurrentUser().getUsername(),round);
        String message2 = ClientMessage.OLD_DICE;
        String message3 = ClientMessage.NEW_DICE;
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(printMessage).buildMessage(message2).
                buildGraphicDice(oldDice).buildMessage(message3).buildGraphicDice(newDice).toString(), Level.INFORMATION);
    }

    /**
     * @param o the other object to compare.
     * @return true if the CLIRoundTrackView is the same or the CLIStateView is the same.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIRoundTrackView)) return false;
        if (!super.equals(o)) return false;
        CLIRoundTrackView that = (CLIRoundTrackView) o;
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
