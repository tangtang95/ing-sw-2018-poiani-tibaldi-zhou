package org.poianitibaldizhou.sagrada.game.model.card.toolcards;

import org.poianitibaldizhou.sagrada.game.model.card.Card;

import java.util.List;

public class ToolCard extends Card {

    private int tokens;
    private List<ICommand> commands;

    protected ToolCard(String name, String description) {
        super(name, description);
        this.tokens = 0;
    }

    public void invokeCommands(){
        for (ICommand command : commands) {

        }
    }

    public int getTokens() {
        return tokens;
    }
}
