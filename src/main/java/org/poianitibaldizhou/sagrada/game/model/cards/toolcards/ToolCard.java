package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.exception.IllegalNumberOfTokensOnToolCardException;
import org.poianitibaldizhou.sagrada.game.model.cards.Card;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ToolCard extends Card {
    private Color color;
    private int tokens;
    private List<ICommand> commands;
    private boolean isSinglePlayer;
    private List<IToolCardObserver> observers;
    private Game game;

    // properties that need communication with client
    private Dice neededDice;
    private Color neededColor;
    private Integer neededValue;
    private Position position;
    private boolean turnEnd;

    public ToolCard(Color color, String name, String description, String action, boolean isSinglePlayer) {
        super(name, description);
        this.tokens = 0;
        this.color = color;
        this.isSinglePlayer = isSinglePlayer;
        ToolCardLanguageParser toolCardLanguageParser = new ToolCardLanguageParser();
        commands = toolCardLanguageParser.parseToolCard(action);
        observers = new ArrayList<>();
        neededDice = null;
        neededValue = null;
    }

    public List<IToolCardObserver> getObservers() {
        return new ArrayList<>(observers);
    }

    public void invokeCommands(Player player) throws RemoteException, InterruptedException {
        boolean flag = true;
        for (int i = 0; i < commands.size(); i++) {
            if(flag == false)
                break;
            flag = commands.get(i).executeCommand(player, this, game);
        }

        if(flag) {
            if (isSinglePlayer)
                for (IToolCardObserver obs : observers)
                    obs.onCardDestroy();
            else
                for (IToolCardObserver obs : observers)
                    obs.onTokenChange(tokens);
        }

        clearParameter();
    }

    public int getTokens() {
        return tokens;
    }

    public Color getColor() {
        return color;
    }

    public int getCost() throws IllegalNumberOfTokensOnToolCardException {
        if(tokens == 0)
            return 1;
        else if(tokens > 1)
            return 2;
        else
            throw new IllegalNumberOfTokensOnToolCardException();

    }

    public void attachToolCardObserver(IToolCardObserver observer) {
        observers.add(observer);
    }

    public void detachToolCardObserver(IToolCardObserver observer) {
        observers.remove(observer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToolCard toolCard = (ToolCard) o;
        return tokens == toolCard.tokens &&
                isSinglePlayer == toolCard.isSinglePlayer &&
                color == toolCard.color &&
                Objects.equals(commands, toolCard.commands)&&
                this.getName().equals(toolCard.getName()) &&
                this.getDescription().equals(toolCard.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, tokens, commands, isSinglePlayer);
    }

    public synchronized void setNeededValue(Integer neededValue) {
        this.neededValue = neededValue;
        notifyAll();
    }

    public synchronized int getNeededValue() throws InterruptedException {
        while(neededValue == null)
            wait();
        return neededValue;
    }

    public synchronized Dice getNeededDice() throws InterruptedException {
        while(neededDice == null)
            wait();
        return neededDice;
    }

    public synchronized void setNeededDice(Dice neededDice) {
        this.neededDice = neededDice;
        notifyAll();
    }

    public synchronized Color getNeededColor() throws InterruptedException {
        while(neededColor == null)
            wait();
        return neededColor;
    }

    public synchronized void setNeededColor(Color neededColor) {
        this.neededColor = neededColor;
        notifyAll();
    }

    public synchronized Position getPosition() throws InterruptedException {
        while(position == null)
            wait();
        return position;
    }

    public synchronized void setPosition(Position position) {
        this.position = position;
        notifyAll();
    }

    public synchronized boolean getTurnEnded() throws InterruptedException {
        if(turnEnd == false)
            wait();
        return true;
    }

    public synchronized void setTurnEnded(boolean isTurnEnded) {
        this.turnEnd = isTurnEnded;
        if(isTurnEnded)
            notifyAll();
    }

    /**
     * Once the commands is done, successful or not, all the parameter are set to null.
     */
    private void clearParameter() {
        this.position = null;
        this.neededColor = null;
        this.neededValue = null;
        this.neededDice = null;
        this.turnEnd = false;
    }
}
