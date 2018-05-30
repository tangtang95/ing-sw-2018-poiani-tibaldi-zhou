package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;

import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class CLIGameView extends UnicastRemoteObject implements IGameView {

    public CLIGameView() throws RemoteException {
        //TODO
    }

    @Override
    public void onPlayersCreate(List<Player> players) {
        //TODO
    }

    @Override
    public void onPublicObjectiveCardsDraw(List<PublicObjectiveCard> publicObjectiveCards) {
        //TODO
    }

    @Override
    public void onToolCardsDraw(List<ToolCard> toolCards) {
        //TODO
    }

    @Override
    public void onChoosePrivateObjectiveCards(List<PrivateObjectiveCard> privateObjectiveCards) {
        //TODO
    }

    @Override
    public void onPrivateObjectiveCardDraw(List<PrivateObjectiveCard> privateObjectiveCards) {
        //TODO
    }

    @Override
    public void onSchemaCardsDraw(List<List<SchemaCard>> schemaCards) throws IOException {
        //TODO
    }


    @Override
    public void ack(String ack) {
        PrinterManager.consolePrint("INFORMATION: " + ack + "\n", Level.INFORMATION);
    }

    @Override
    public void err(String err) {
        PrinterManager.consolePrint("ERROR: " + err + "\n", Level.INFORMATION);
    }

    @Override
    public void notifyModelSynch(DraftPool draftPool, List<Player> players, RoundTrack roundTrack,
                                 List<ToolCard> toolCards) {
        // TODO
    }
}
