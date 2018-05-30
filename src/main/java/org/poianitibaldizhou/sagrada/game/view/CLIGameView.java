package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;

import org.poianitibaldizhou.sagrada.exception.CommandNotFoundException;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class CLIGameView extends UnicastRemoteObject implements IGameView {

    private final transient ConnectionManager connectionManager;

    public CLIGameView(ConnectionManager connectionManager) throws RemoteException {
        this.connectionManager = connectionManager;
    }

    private int readNumber(int maxInt) {
        ConsoleListener consoleListener = ConsoleListener.getInstance();
        consoleListener.stopCommandConsole();
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        int key = 0;
        do {
            try {
                String read = r.readLine();
                key = Integer.parseInt(read);
                if (!(key > 0 && key <= maxInt))
                    throw new CommandNotFoundException();
            } catch (IOException e) {
                PrinterManager.consolePrint(this.getClass().getSimpleName() +
                        BuildGraphic.ERROR_READING, Level.ERROR);
                break;
            } catch (NumberFormatException e) {
                PrinterManager.consolePrint(BuildGraphic.NOT_A_NUMBER, Level.ERROR);
            } catch (CommandNotFoundException e) {
                PrinterManager.consolePrint(BuildGraphic.COMMAND_NOT_FOUND, Level.ERROR);
            }
        } while (key < 1);
        consoleListener.wakeUpCommandConsole();
        return key - 1;
    }

    @Override
    public void onPlayersCreate(String players) {
        //TODO
    }

    @Override
    public void onPublicObjectiveCardsDraw(String publicObjectiveCards) {
        //TODO
    }

    @Override
    public void onToolCardsDraw(String toolCards) {
        //TODO
    }

    @Override
    public void onChoosePrivateObjectiveCards(String privateObjectiveCards) {
        //TODO
    }

    @Override
    public void onPrivateObjectiveCardDraw(String privateObjectiveCards) {
        //TODO
    }

    @Override
    public void onSchemaCardsDraw(String schemaCards) {
        Runnable reader = () -> {

        };
        new Thread(reader).start();
    }

    /**
     * {{@inheritDoc}}
     */
    @Override
    public void ack(String ack) {
        PrinterManager.consolePrint("INFORMATION: " + ack + "\n", Level.INFORMATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void err(String err) {
        PrinterManager.consolePrint("ERROR: " + err + "\n", Level.INFORMATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyModelSynch(DraftPool draftPool, List<Player> players, RoundTrack roundTrack,
                                 List<ToolCard> toolCards) {
        // TODO
    }
}
