package org.poianitibaldizhou.sagrada.game.model.cards;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

import java.io.Serializable;
import java.util.Objects;

@Immutable
public class Position implements JSONable {
    private int row;
    private int column;

    /**
     * Position param for network protocol.
     */
    private static final String JSON_ROW = "row";
    private static final String JSON_COLUMN = "column";

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


    /**
     * Convert a Position in a JSONObject.
     *
     * @return a JSONObject.
     */
    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject main = new JSONObject();
        JSONObject position = new JSONObject();
        position.putIfAbsent(JSON_ROW, this.getRow());
        position.putIfAbsent(JSON_COLUMN, this.getColumn());
        main.put(SharedConstants.TYPE, SharedConstants.POSITION);
        main.put(SharedConstants.BODY,position);
        return main;
    }

    /**
     * Convert a json string in a Position object.
     *
     * @param jsonObject a JSONObject that contains a Position.
     * @return a Position object.
     */
    public static Position toObject(JSONObject jsonObject) {
        return new Position(
                Integer.parseInt(jsonObject.get(JSON_ROW).toString()),
                Integer.parseInt(jsonObject.get(JSON_COLUMN).toString()));
    }

}
