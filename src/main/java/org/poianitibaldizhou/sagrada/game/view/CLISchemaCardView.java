package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BufferManager;
import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.ISchemaCardObserver;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CLISchemaCardView extends UnicastRemoteObject implements ISchemaCardObserver {

    private transient CLIGameView cliGameView;
    private transient Map<String, SchemaCard> schemaCards;
    private transient BufferManager bufferManager;

    CLISchemaCardView(CLIGameView cliGameView) throws RemoteException {
        super();
        this.cliGameView = cliGameView;
        schemaCards = new HashMap<>();
        this.bufferManager = cliGameView.bufferManager;
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
                        "place a dice in his schema card.", Level.ACK);
                return;
            }
        }
        String message = user.getName() + " placed a dice on his schema card.";
        BuildGraphic buildGraphic = new BuildGraphic();
        bufferManager.consolePrint(buildGraphic.buildMessage(message).buildMessage(position.toString()).
                buildGraphicDice(dice).toString(), Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceRemove(Dice dice, Position position){
        User user = cliGameView.getCurrentUser();
        synchronized (schemaCards.get(user.getName())) {
            schemaCards.get(user.getName()).removeDice(position);
        }
        String message = user.getName() + " removed a dice from his schema card.";
        BuildGraphic buildGraphic = new BuildGraphic();
        bufferManager.consolePrint(buildGraphic.buildMessage(message).buildMessage(position.toString()).
                buildGraphicDice(dice).toString(), Level.STANDARD);
    }

    public Map<String, SchemaCard> getSchemaCards() {
        return schemaCards;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLISchemaCardView)) return false;
        if (!super.equals(o)) return false;
        CLISchemaCardView that = (CLISchemaCardView) o;
        return Objects.equals(cliGameView, that.cliGameView) &&
                Objects.equals(getSchemaCards(), that.getSchemaCards()) &&
                Objects.equals(bufferManager, that.bufferManager);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), cliGameView, getSchemaCards(), bufferManager);
    }
}
