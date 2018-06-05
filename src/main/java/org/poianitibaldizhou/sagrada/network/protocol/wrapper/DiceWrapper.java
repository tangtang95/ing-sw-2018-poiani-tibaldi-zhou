package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

import java.util.Objects;

@Immutable
public class DiceWrapper implements JSONable{

    /**
     * DiceWrapper param for network protocol.
     */
    private static final String JSON_VALUE = "value";
    private static final String JSON_COLOR = "color";

    public static final int MAX_VALUE = 6;
    public static final int MIN_VALUE = 1;

    private final int number;
    private final ColorWrapper color;

    public DiceWrapper(ColorWrapper color, int number) {
        this.color = color;
        this.number = number;
    }

    /**
     * Fake constructor.
     */
    @SuppressWarnings("unused")
    public DiceWrapper() {
        this.number = 0;
        this.color = null;
    }

    public int getNumber() {
        return number;
    }

    public ColorWrapper getColor() {
        return color;
    }

    /**
     * Convert a diceWrapper in a JSONObject.
     *
     * @return a JSONObject.
     */
    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject main = new JSONObject();
        JSONObject diceJSON = new JSONObject();
        diceJSON.put(JSON_VALUE, this.getNumber());
        diceJSON.put(JSON_COLOR, this.getColor().name());
        main.put(SharedConstants.TYPE, SharedConstants.DICE);
        main.put(SharedConstants.BODY,diceJSON);
        return main;
    }

    /**
     * Convert a json string in a diceWrapper object.
     *
     * @param jsonObject a JSONObject that contains a dice.
     * @return a DiceWrapper object.
     */
    @Override
    public Object toObject(JSONObject jsonObject) {
        return new DiceWrapper(ColorWrapper.valueOf((String) jsonObject.get(JSON_COLOR)),
                Integer.parseInt(jsonObject.get(JSON_VALUE).toString()));
    }

    @Override
    public String toString() {
        return "" + number + "/" + Objects.requireNonNull(color).name();
    }
}
