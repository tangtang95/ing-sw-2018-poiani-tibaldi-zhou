package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Player;

public class CheckTurn implements ICommand {
    private final int turn;

    public CheckTurn(int turn) {
        this.turn = turn;
    }

    @Override
    public void executeCommand(Player player) {

    }

    public int getTurn() {
        return turn;
    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof CheckTurn))
            return false;
        CheckTurn obj = (CheckTurn) object;
        return this.turn == obj.getTurn();
    }
}