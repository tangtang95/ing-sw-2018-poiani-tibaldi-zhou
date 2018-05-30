package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IRoundTrackObserver;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Objects;

public class CLIRoundTrackView extends UnicastRemoteObject implements IRoundTrackObserver {

    private final transient CLIGameView cliGameView;
    private final transient RoundTrack roundTrack;

    CLIRoundTrackView(CLIGameView cliGameView) throws RemoteException {
        super();
        this.cliGameView = cliGameView;
        roundTrack = new RoundTrack();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDicesAddToRound(String diceList, String round){
        //User user = cliGameView.getCurrentUser();
        //String message = user.getName() + " added a list of dices to the round track at round " + round + ".";
        BuildGraphic buildGraphic = new BuildGraphic();
        //PrinterManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDices(diceList).toString(), Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceAddToRound(String dice, String round){
       // User user = cliGameView.getCurrentUser();
        synchronized (roundTrack) {

        }
        //String message = user.getName() + " added a dice to the round track at round " + round + ".";
        BuildGraphic buildGraphic = new BuildGraphic();
        //PrinterManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDice(dice).toString(), Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceRemoveFromRound(String dice, String round){
        //User user = cliGameView.getCurrentUser();
        synchronized (roundTrack) {

        }
        //String message = user.getName() + " removed a dice from the round track at round " + round + ".";
        BuildGraphic buildGraphic = new BuildGraphic();
        //PrinterManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDice(dice).toString(), Level.STANDARD);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceSwap(String oldDice, String newDice, String round){
        //User user = cliGameView.getCurrentUser();
       // String message = user.getName() + " swap a with the round track at round " + round + ".";
        String message2 = "Old dice (no more present in round track) : ";
        String message3 = "New dice (added to the round track) : ";
        BuildGraphic buildGraphic = new BuildGraphic();
        //PrinterManager.consolePrint(buildGraphic.buildMessage(message).buildMessage(message2).
            //    buildGraphicDice(oldDice).buildMessage(message3).buildGraphicDice(newDice).toString(), Level.STANDARD);
    }

    public RoundTrack getRoundTrack() {
        return roundTrack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIRoundTrackView)) return false;
        if (!super.equals(o)) return false;
        CLIRoundTrackView that = (CLIRoundTrackView) o;
        return Objects.equals(cliGameView, that.cliGameView) &&
                Objects.equals(getRoundTrack(), that.getRoundTrack());
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), cliGameView, getRoundTrack());
    }
}
