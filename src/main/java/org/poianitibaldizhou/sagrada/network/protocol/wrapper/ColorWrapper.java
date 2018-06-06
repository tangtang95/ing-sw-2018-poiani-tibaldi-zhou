package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import org.jetbrains.annotations.Contract;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

public enum ColorWrapper implements JSONable{
    GREEN(0), RED(1), YELLOW(2), BLUE(3), PURPLE(4);

    private int index;

    ColorWrapper(int index) {
        this.index = index;
    }

    @Contract(pure = true)
    public int getIndex(){
        return index;
    }


    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject main = new JSONObject();
        main.put(SharedConstants.TYPE, SharedConstants.COLOR);
        main.put(SharedConstants.BODY,this.name());
        return main;
    }

    public static ColorWrapper toObject(JSONObject jsonObject) {
        return ColorWrapper.valueOf(jsonObject.get(SharedConstants.BODY).toString());
    }
}
