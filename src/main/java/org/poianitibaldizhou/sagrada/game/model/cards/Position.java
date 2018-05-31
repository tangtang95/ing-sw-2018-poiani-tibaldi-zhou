package org.poianitibaldizhou.sagrada.game.model.cards;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;

import java.io.Serializable;
import java.util.Objects;

@Immutable
public class Position implements Serializable, JSONable {
    private int row;
    private int column;

    /**
     * Constructor.
     * It creates a position with a specified row and column.
     *
     * @param row    specified row
     * @param column specified column
     */
    public Position(int row, int column) {
        if (SchemaCard.isOutOfBounds(row, column))
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

    /**
     * @param row the delta row to add
     * @param column the delta column to add
     * @return a new Position with row = this.row + row given and column = this.column + column given
     */
    public Position add(int row, int column) {
        return new Position(this.row + row, this.column + column);
    }

    /**
     *
     * @param row the delta row to subtract
     * @param column the delta column to subtract
     * @return a new Position with row = this.row - row given and column = this.column - column given
     */
    public Position subtract(int row, int column) {
        return new Position(this.row - row, this.column - column);
    }

    @Override
    public String toString() {
        return "Position: (" + getRow() +", " + getColumn() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Position))
            return false;
        Position temp = (Position) o;
        return temp.getColumn() == this.column && temp.getRow() == this.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }


    @Override
    public JSONObject toJSON() {
        JSONObject position = new JSONObject();
        position.putIfAbsent("row", this.getRow());
        position.putIfAbsent("column", this.getColumn());
        return position;
    }

    @Override
    public Object toObject(JSONObject jsonObject) {
        return null;
    }
}
