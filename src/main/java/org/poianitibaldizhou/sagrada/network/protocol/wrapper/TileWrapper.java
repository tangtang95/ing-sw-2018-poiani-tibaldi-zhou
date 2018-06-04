package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.JSONClientProtocol;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

@Immutable
public class TileWrapper implements JSONable{

    private final String constraint;
    private DiceWrapper dice;

    private final JSONClientProtocol protocol = new JSONClientProtocol();

    /**
     * TileWrapper param for network protocol.
     */
    private static final String JSON_CONSTRAINT = "constraint";

    public TileWrapper(String constraint) {
        this.constraint = constraint;
    }

    public DiceWrapper getDice() {
        return dice;
    }

    /**
     * Convert a TileWrapper in a JSONObject.
     *
     * @return a JSONObject.
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject main = new JSONObject();
        JSONObject tileJSON =  new JSONObject();
        if(this.getDice() != null)
            tileJSON.put(SharedConstants.DICE, this.getDice().toJSON());
        tileJSON.put(JSON_CONSTRAINT, constraint);
        main.put(SharedConstants.TYPE, SharedConstants.TILE);
        main.put(SharedConstants.BODY,tileJSON);
        return main;
    }

    /**
     * Convert a json string in a TileWrapper object.
     *
     * @param jsonObject a JSONObject that contains a TileWrapper.
     * @return a tile object or null if the jsonObject is wrong.
     */
    @Override
    public Object toObject(JSONObject jsonObject) {
        TileWrapper tile;

        tile = new TileWrapper((String) jsonObject.get(JSON_CONSTRAINT));

        if (jsonObject.containsKey(SharedConstants.DICE)) {
            DiceWrapper readDice = (DiceWrapper) protocol.convertToObject(
                    (JSONObject) jsonObject.get(SharedConstants.DICE));
            tile.setDice(readDice);
        }
        return tile;
    }

    public String getConstraint() {
        return constraint;
    }

    public void setDice(DiceWrapper dice) {
        this.dice = dice;
    }
}
