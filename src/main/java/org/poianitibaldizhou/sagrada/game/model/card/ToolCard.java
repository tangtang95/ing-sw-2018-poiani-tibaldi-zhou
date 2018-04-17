package org.poianitibaldizhou.sagrada.game.model.card;

import java.util.List;

public class ToolCard extends Card{

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
