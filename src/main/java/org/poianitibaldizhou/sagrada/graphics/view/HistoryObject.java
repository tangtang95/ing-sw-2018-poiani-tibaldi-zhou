package org.poianitibaldizhou.sagrada.graphics.view;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.ColorWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.PositionWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.ToolCardWrapper;
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

public final class HistoryObject {

    private final Object object;
    private final ObjectMessageType objectMessageType;

    public HistoryObject(Object object, ObjectMessageType objectMessageType) {
        this.object = object;
        this.objectMessageType = objectMessageType;
    }

    @Contract(pure = true)
    public ObjectMessageType getObjectMessageType() {
        return objectMessageType;
    }

    @Contract(pure = true)
    public Object getObject() {
        return object;
    }

    /**
     * @return a message based on its ObjectMessageType
     */
    public String getMessage(){
        switch (objectMessageType){
            case COLOR:
                ColorWrapper colorWrapper = (ColorWrapper) object;
                return String.format(ClientMessage.COLOR_CHOSEN, colorWrapper.name().toLowerCase());
            case NONE:
                return "";
            case DICE:
                DiceWrapper diceWrapper = (DiceWrapper) object;
                return String.format(ClientMessage.DICE_CHOSEN, diceWrapper.toString());
            case POSITION:
                PositionWrapper positionWrapper = (PositionWrapper) object;
                return String.format(ClientMessage.POSITION_CHOSEN, positionWrapper.toString());
            case ANSWER:
                Boolean answer = (Boolean) object;
                return String.format(ClientMessage.ACTION_CHOSEN, (answer) ? "" : "non");
            case VALUE:
                Integer value = (Integer) object;
                return String.format(ClientMessage.VALUE_CHOSEN, value);
            case TOOL_CARD:
                ToolCardWrapper toolCardWrapper = (ToolCardWrapper) object;
                return String.format(ClientMessage.TOOL_CARD_CHOSEN,
                        toolCardWrapper.getName(), toolCardWrapper.getDescription());
        }
        return "";
    }

}