package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.Card;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ToolCard extends Card {
    private Color color;
    private int tokens;
    private List<ICommand> commands;
    private boolean isSinglePlayer;
    private List<IToolCardObserver> observers;

    public ToolCard(Color color, String name, String description, String action, boolean isSinglePlayer) {
        super(name, description);
        this.tokens = 0;
        this.color = color;
        this.isSinglePlayer = isSinglePlayer;
        ToolCardLanguageParser toolCardLanguageParser = new ToolCardLanguageParser();
        commands = toolCardLanguageParser.parseToolCard(action);
        observers = new ArrayList<>();
    }

    /**
     * copy-constructor
     *
     * @param toolCard the toolCard to copy
     */
    //TODO refactor
    private ToolCard(ToolCard toolCard){
        super(toolCard.getName(),toolCard.getDescription());
        this.color = toolCard.getColor();
        this.tokens = toolCard.getTokens();
        this.commands = toolCard.getCommands();
        this.isSinglePlayer = toolCard.isSinglePlayer;
        this.observers = toolCard.getObservers();

    }

    public List<IToolCardObserver> getObservers() {
        return new ArrayList<>(observers);
    }

    public ToolCardExecutor getToolCardExecutor(){
        ToolCardExecutor toolCardExecutor = new ToolCardExecutor(commands);

        if (isSinglePlayer) {
            for (IToolCardObserver obs : observers)
                obs.onCardDestroy();
        } else {
            for (IToolCardObserver obs : observers)
                obs.onTokenChange(tokens);
        }

        return toolCardExecutor;
    }

    public int getTokens() {
        return tokens;
    }

    public Color getColor() {
        return color;
    }

    public int getCost() {
        if (tokens <= 0)
            return 1;
        else
            return 2;
    }

    public void addTokens(int tokens) {
        this.tokens += tokens;
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
                equalsCommand(toolCard.commands) &&
                this.getName().equals(toolCard.getName()) &&
                this.getDescription().equals(toolCard.getDescription());
    }

    private boolean equalsCommand(List<ICommand> commands) {
        for (int i = 0; i < commands.size(); i++) {
            if (commands.get(i).getClass() != this.commands.get(i).getClass())
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, tokens, commands, isSinglePlayer);
    }

    public List<ICommand> getCommands() {
        return new ArrayList<>(commands);
    }

    //TODO refactor
    public static ToolCard newInstance(ToolCard toolCard) {
        if (toolCard == null)
            return null;
        return new ToolCard(toolCard);
    }
}
