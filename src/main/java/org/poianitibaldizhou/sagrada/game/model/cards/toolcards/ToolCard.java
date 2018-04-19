package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

import org.poianitibaldizhou.sagrada.exception.CommandNotFoundException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.exception.IllegalNumberOfTokensOnToolCardException;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.Card;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;

import java.util.ArrayList;
import java.util.List;

public class ToolCard extends Card {
    private Color color;
    private int tokens;
    private List<ICommand> commands;
    private Game game;
    private List<IToolCardObserver> observers;
    private ToolCardLanguageParser tclp;

    public ToolCard(String name, String description, Game game) throws CommandNotFoundException {
        super(name, description);
        this.tokens = 0;
        this.game = game;
        observers = new ArrayList<IToolCardObserver>();
        tclp = new ToolCardLanguageParser();
        commands = tclp.parseToolCard(this.getDescription());
    }

    public void invokeCommands(Player player){
        for (ICommand command : commands) {
            command.executeCommand(player);
        }

        if(game.isSinglePlayer())
            for(IToolCardObserver obs : observers)
                obs.onCardDestroy();
        else
            for(IToolCardObserver obs : observers)
                obs.onTokenChange(tokens);
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
}
