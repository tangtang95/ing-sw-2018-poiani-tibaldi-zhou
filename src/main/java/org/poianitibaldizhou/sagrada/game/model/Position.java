package org.poianitibaldizhou.sagrada.game.model;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.PourOverDice;

import java.util.Objects;

@Immutable
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
    public Position(int row, int column) {
        if(SchemaCard.isOutOfBounds(row,column))
            throw new IllegalArgumentException("Row and column is out of bounds");
        this.row = row;
        this.column = column;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public Position add(int row, int column) {
        return new Position(this.row + row, this.column + column);
    }

    public Position subtract(int row, int column){
        return new Position(this.row - row, this.column - column);
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Position))
            return false;
        Position temp = (Position) o;
        return temp.getColumn() == this.column && temp.getRow() == this.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }



}
