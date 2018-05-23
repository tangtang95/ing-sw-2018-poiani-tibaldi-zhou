package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BufferManager;
import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.observers.ISchemaCardObserver;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.rmi.RemoteException;
import java.util.HashMap;

public class CLISchemaCardView implements ISchemaCardObserver {

    private final CLIGameView cliGameView;
    private final transient HashMap<String, SchemaCard> schemaCards;
    private final BufferManager bufferManager;

    public CLISchemaCardView(CLIGameView cliGameView, BufferManager bufferManager) {
        this.cliGameView = cliGameView;
        schemaCards = new HashMap<>();
        this.bufferManager = bufferManager;
    }

    /**
     * Add a schema card of a certain player playing in the game.
     *
     * @param userName user's name
     * @param schemaCard user's schemacard
     */
    public void addSchemaCard(String userName, SchemaCard schemaCard) {
        schemaCards.putIfAbsent(userName, schemaCard);
    }

    public SchemaCard getSchemaCard(String userName) {
        synchronized (schemaCards.get(userName)) {
            return schemaCards.get(userName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPlaceDice(Dice dice, Position position) throws RemoteException {
        User user = cliGameView.getCurrentUser();
        synchronized (schemaCards.get(user.getName())) {
            try {
                schemaCards.get(user.getName()).setDice(dice, position);
            } catch (RuleViolationException e) {
                bufferManager.consolePrint("An error has occured when " + user.getName() + " tried to" +
                        "place a dice in his schema card.", Level.HIGH);
                return;
            }
        }
        String message = user.getName() + " placed a dice on his schema card.";
        BuildGraphic buildGraphic = new BuildGraphic();
        bufferManager.consolePrint(buildGraphic.buildMessage(message).buildMessage(position.toString()).buildGraphicDice(dice).toString(), Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceRemove(Dice dice, Position position) throws RemoteException {
        User user = cliGameView.getCurrentUser();
        synchronized (schemaCards.get(user.getName())) {
            schemaCards.get(user.getName()).removeDice(position);
        }
        String message = user.getName() + " removed a dice from his schema card.";
        BuildGraphic buildGraphic = new BuildGraphic();
        bufferManager.consolePrint(buildGraphic.buildMessage(message).buildMessage(position.toString()).buildGraphicDice(dice).toString(), Level.LOW);
    }
}
