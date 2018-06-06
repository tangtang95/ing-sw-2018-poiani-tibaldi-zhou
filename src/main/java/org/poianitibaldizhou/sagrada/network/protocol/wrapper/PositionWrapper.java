package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

@Immutable
public final class PositionWrapper implements JSONable {

    private final int row;
    private final int column;

    /**
     * Position param for network protocol.
     */
    private static final String JSON_ROW = "row";
    private static final String JSON_COLUMN = "column";

    public PositionWrapper(int row, int column) {
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
     * Convert a PositionWrapper in a JSONObject.
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

    @Override
    public String toString() {
        return "(" + (getRow() + 1) + ", " + (getColumn() + 1) + ")";
    }

}
