package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.ISchemaCardObserver;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CLISchemaCardView extends UnicastRemoteObject implements ISchemaCardObserver {

    private transient CLIGameView cliGameView;
    private transient Map<String, SchemaCard> schemaCards;

    CLISchemaCardView(CLIGameView cliGameView) throws RemoteException {
        super();
        this.cliGameView = cliGameView;
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
                Objects.equals(getSchemaCards(), that.getSchemaCards());
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), cliGameView, getSchemaCards());
    }

    @Override
    public void onPlaceDice(String message) throws IOException {

    }

    @Override
    public void onDiceRemove(String message) throws IOException {

    }
}
