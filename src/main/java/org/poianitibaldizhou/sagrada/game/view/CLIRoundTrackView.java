package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IRoundTrackObserver;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Objects;

public class CLIRoundTrackView extends UnicastRemoteObject implements IRoundTrackObserver {

    private final transient CLIStateView cliStateView;
    private final transient ClientGetMessage clientGetMessage;

    public CLIRoundTrackView(CLIStateView cliStateView) throws RemoteException {
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
        String printMessage = cliStateView.getCurrentUser().getUsername() +
                " added a list of dices to the round track at round " + round + ".";
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(printMessage).buildGraphicDices(diceWrapperList).toString(),
                Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceAddToRound(String message) throws IOException {
        Integer round = clientGetMessage.getValue(message);
        DiceWrapper diceWrapper = clientGetMessage.getDice(message);
        String printMessage = cliStateView.getCurrentUser().getUsername() + " added a dice to the round track at round "
                + round + ".";
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(printMessage).buildGraphicDice(diceWrapper).toString(),
                Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceRemoveFromRound(String message) throws IOException {
        Integer round  = clientGetMessage.getValue(message);
        DiceWrapper diceWrapper = clientGetMessage.getDice(message);
        String printMessage = cliStateView.getCurrentUser().getUsername() + " removed a dice from the round track at round " + round + ".";
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(printMessage).buildGraphicDice(diceWrapper).toString(),
                Level.STANDARD);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceSwap(String message) throws IOException {
        DiceWrapper oldDice = clientGetMessage.getOldDice(message);
        DiceWrapper newDice = clientGetMessage.getNewDice(message);
        Integer round = clientGetMessage.getValue(message);

        String printMessage = cliStateView.getCurrentUser().getUsername() + " swap a with the round track at round "
                + round + ".";
        String message2 = "Old dice (no more present in round track) : ";
        String message3 = "New dice (added to the round track) : ";
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(printMessage).buildMessage(message2).
                buildGraphicDice(oldDice).buildMessage(message3).buildGraphicDice(newDice).toString(), Level.STANDARD);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIRoundTrackView)) return false;
        if (!super.equals(o)) return false;
        CLIRoundTrackView that = (CLIRoundTrackView) o;
        return Objects.equals(cliStateView, that.cliStateView);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cliStateView);
    }
}
