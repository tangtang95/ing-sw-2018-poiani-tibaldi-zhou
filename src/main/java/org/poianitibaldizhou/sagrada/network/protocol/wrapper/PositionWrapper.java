package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.network.protocol.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

import java.util.Objects;

/**
 * Copy class of Position in the model.
 */
@Immutable
public final class PositionWrapper implements JSONable {

    /**
     * Row position.
     */
    private final int row;

    /**
     * Column position.
     */
    private final int column;

    /**
     * Position param for network protocol.
     */
    private static final String JSON_ROW = "row";
    private static final String JSON_COLUMN = "column";

    /**
     * Constructor.
     *
     * @param row position on column.
     * @param column position on row.
     */
    public PositionWrapper(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * @return the column position.
     */
    public int getColumn() {
        return column;
    }

    /**
     * @return the row position.
     */
    public int getRow() {
        return row;
    }

    /**
     * Convert a PositionWrapper in a JSONObject.
     *
     * @return a JSONObject.
     */
    @Override
    @SuppressWarnings({"unchecked", "Duplicates"})
    public JSONObject toJSON() {
        JSONObject main = new JSONObject();
        JSONObject position = new JSONObject();
        position.putIfAbsent(JSON_ROW, this.getRow());
        position.putIfAbsent(JSON_COLUMN, this.getColumn());
        main.put(SharedConstants.TYPE, SharedConstants.POSITION);
        main.put(SharedConstants.BODY, position);
        return main;
    }

    /**
     * Convert a json string in a PositionWrapper object.
     *
     * @param jsonObject a JSONObject that contains a PositionWrapper.
     * @return a PositionWrapper object.
     */
    public static PositionWrapper toObject(JSONObject jsonObject) {
        return new PositionWrapper(
                Integer.parseInt(jsonObject.get(JSON_ROW).toString()),
                Integer.parseInt(jsonObject.get(JSON_COLUMN).toString()));
    }

    /**
     * @return Position to string -> (row + 1,column + 1)
     */
    @Override
    public String toString() {
        return "(" + (getRow() + 1) + ", " + (getColumn() + 1) + ")";
    }

    /**
     * @param o the other object to compare.
     * @return true if the PositionWrapper is the same object or if it has the same numberRow and
     * the same numberColumn.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PositionWrapper)) return false;
        PositionWrapper that = (PositionWrapper) o;
        return getRow() == that.getRow() &&
                getColumn() == that.getColumn();
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getRow(), getColumn());
    }
}
