package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.PourOverDice;

public class Position {
    private int row;
    private int column;

    /**
     * Constructor.
     * It creates a position with a specified row and column.
     *
     * @param row specified row
     * @param column specified column
     */
    public Position(int row, int column) throws IllegalAccessException {
        if(SchemaCard.isOutOfBounds(row,column))
            throw new IllegalAccessException();
        this.row = row;
        this.column = column;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Position))
            return false;
        Position temp = (Position) o;
        return (temp.getColumn() == this.column && temp.getRow() == this.row)? true:false;
    }
}
