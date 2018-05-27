package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BufferManager;
import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.observers.IRoundTrackObserver;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.rmi.RemoteException;
import java.util.List;

public class CLIRoundTrackView implements IRoundTrackObserver {

    private final CLIGameView cliGameView;
    private final transient RoundTrack roundTrack;
    private final BufferManager bufferManager;

    CLIRoundTrackView(CLIGameView cliGameView)  {
        this.cliGameView = cliGameView;
        this.bufferManager = cliGameView.bufferManager;
        roundTrack = new RoundTrack();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDicesAddToRound(List<Dice> diceList, int round) throws RemoteException {
        User user = cliGameView.getCurrentUser();
        synchronized (roundTrack) {
            roundTrack.addDicesToRound(diceList, round);
        }
        String message = user.getName() + " added a list of dices to the round track at round " + round + ".";
        BuildGraphic buildGraphic = new BuildGraphic();
        bufferManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDices(diceList).toString(), Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceAddToRound(Dice dice, int round) throws RemoteException {
        User user = cliGameView.getCurrentUser();
        synchronized (roundTrack) {
            roundTrack.addDiceToRound(dice, round);
        }
        String message = user.getName() + " added a dice to the round track at round " + round + ".";
        BuildGraphic buildGraphic = new BuildGraphic();
        bufferManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDice(dice).toString(), Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceRemoveFromRound(Dice dice, int round) throws RemoteException {
        User user = cliGameView.getCurrentUser();
        synchronized (roundTrack) {
            roundTrack.removeDiceFromRoundTrack(round, dice);
        }
        String message = user.getName() + " removed a dice from the round track at round " + round + ".";
        BuildGraphic buildGraphic = new BuildGraphic();
        bufferManager.consolePrint(buildGraphic.buildMessage(message).buildGraphicDice(dice).toString(), Level.LOW);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceSwap(Dice oldDice, Dice newDice, int round) throws RemoteException {
        User user = cliGameView.getCurrentUser();
        synchronized (roundTrack) {
            try {
                roundTrack.swapDice(oldDice, newDice, round);
            } catch (DiceNotFoundException e) {
                bufferManager.consolePrint("An error has occured when " + user.getName() + "" +
                        "tried to swap a dice with the round track", Level.HIGH);
                return;
            }
        }
        String message = user.getName() + " swap a with the round track at round " + round + ".";
        String message2 = "Old dice (no more present in round track) : ";
        String message3 = "New dice (added to the round track) : ";
        BuildGraphic buildGraphic = new BuildGraphic();
        bufferManager.consolePrint(buildGraphic.buildMessage(message).buildMessage(message2).
                buildGraphicDice(oldDice).buildMessage(message3).buildGraphicDice(newDice).toString(), Level.LOW);
    }

    public RoundTrack getRoundTrack() {
        return roundTrack;
    }
}
