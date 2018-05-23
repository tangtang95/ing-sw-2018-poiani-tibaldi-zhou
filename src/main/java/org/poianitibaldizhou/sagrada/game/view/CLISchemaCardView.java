package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.observers.ISchemaCardObserver;

import java.rmi.RemoteException;
import java.util.HashMap;

public class CLISchemaCardView extends CLIMenuView implements ISchemaCardObserver {

    private CLIMenuView cliMenuView;
    private final HashMap<String, SchemaCard> schemaCards;

    public CLISchemaCardView(CLIMenuView cliMenuView) throws RemoteException {
        super(cliMenuView.networkManager, cliMenuView.screenManager);
        this.cliMenuView = cliMenuView;
        schemaCards = new HashMap<>();
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
    public void onPlaceDice(Dice dice, Position position) {
        synchronized (schemaCards.get(cliMenuView.currentUser.getName())) {
            try {
                schemaCards.get(cliMenuView.currentUser.getName()).setDice(dice, position);
            } catch (RuleViolationException e) {
                bufferManager.consolePrint("An error has occured when " + cliMenuView.currentUser.getName() + " tried to" +
                        "place a dice in his schema card.", Level.HIGH);
                return;
            }
        }
        String message = cliMenuView.currentUser.getName() + " placed a dice on his schema card.";
        BuildGraphic buildGraphic = new BuildGraphic();
        bufferManager.consolePrint(buildGraphic.buildMessage(message).buildMessage(position.toString()).buildGraphicDice(dice).toString(), Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceRemove(Dice dice, Position position) {
        synchronized (schemaCards.get(cliMenuView.currentUser.getName())) {
            schemaCards.get(cliMenuView.currentUser.getName()).removeDice(position);
        }
        String message = cliMenuView.currentUser.getName() + " removed a dice from his schema card.";
        BuildGraphic buildGraphic = new BuildGraphic();
        bufferManager.consolePrint(buildGraphic.buildMessage(message).buildMessage(position.toString()).buildGraphicDice(dice).toString(), Level.LOW);
    }
}
