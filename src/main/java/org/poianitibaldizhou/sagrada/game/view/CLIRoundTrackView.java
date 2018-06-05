package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IRoundTrackObserver;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Objects;

public class CLIRoundTrackView extends UnicastRemoteObject implements IRoundTrackObserver {

    private final transient CLIGameView cliGameView;
    public CLIRoundTrackView(CLIGameView cliGameView) throws RemoteException {
        super();
        this.cliGameView = cliGameView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDicesAddToRound(String message) throws IOException {
        Integer round = cliGameView.getClientGetMessage().getValue(message);
        List<DiceWrapper> diceWrapperList = cliGameView.getClientGetMessage().getDiceList(message);
        String printMessage = cliGameView.getCurrentUser().getUsername() + " added a list of dices to the round track at round " + round + ".";
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(printMessage).buildGraphicDices(diceWrapperList).toString(), Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceAddToRound(String message) throws IOException {
        Integer round = cliGameView.getClientGetMessage().getValue(message);
        DiceWrapper diceWrapper = cliGameView.getClientGetMessage().getDice(message);
        String printMessage = cliGameView.getCurrentUser().getUsername() + " added a dice to the round track at round " + round + ".";
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(printMessage).buildGraphicDice(diceWrapper).toString(), Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceRemoveFromRound(String message) throws IOException {
        Integer round  = cliGameView.getClientGetMessage().getValue(message);
        DiceWrapper diceWrapper = cliGameView.getClientGetMessage().getDice(message);
        String printMessage = cliGameView.getCurrentUser().getUsername() + " removed a dice from the round track at round " + round + ".";
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(printMessage).buildGraphicDice(diceWrapper).toString(), Level.STANDARD);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceSwap(String message) throws IOException {
        DiceWrapper oldDice = cliGameView.getClientGetMessage().getOldDice(message);
        DiceWrapper newDice = cliGameView.getClientGetMessage().getNewDice(message);
        Integer round = cliGameView.getClientGetMessage().getValue(message);

        String printMessage = cliGameView.getCurrentUser().getUsername() + " swap a with the round track at round " + round + ".";
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
        return Objects.equals(cliGameView, that.cliGameView);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cliGameView);
    }
}
