package org.poianitibaldizhou.sagrada.graphics.view.listener.executorListener;

import org.poianitibaldizhou.sagrada.network.protocol.wrapper.ColorWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.PositionWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.ToolCardWrapper;

public final class HistoryObject {

    private final Object object;
    private final ObjectMessageType objectMessageType;

    public HistoryObject(Object object, ObjectMessageType objectMessageType) {
        this.object = object;
        this.objectMessageType = objectMessageType;
    }

    public ObjectMessageType getObjectMessageType() {
        return objectMessageType;
    }

    public Object getObject() {
        return object;
    }

    public String getMessage(){
        switch (objectMessageType){
            case COLOR:
                ColorWrapper colorWrapper = (ColorWrapper) object;
                return String.format("Hai scelto precedentemente il colore: %s", colorWrapper.name().toLowerCase());
            case NONE:
                return "";
            case DICE:
                DiceWrapper diceWrapper = (DiceWrapper) object;
                return String.format("Hai scelto precedentemente il dado: %s", diceWrapper.toString());
            case POSITION:
                PositionWrapper positionWrapper = (PositionWrapper) object;
                return String.format("Hai scelto precedentemente la posizione: %s", positionWrapper.toString());
            case ANSWER:
                Boolean answer = (Boolean) object;
                return String.format("Hai scelto precedentemente di %s continuare", (answer) ? "" : "non");
            case VALUE:
                Integer value = (Integer) object;
                return String.format("Hai scelto precedentemente il valore: %s", value);
            case TOOL_CARD:
                ToolCardWrapper toolCardWrapper = (ToolCardWrapper) object;
                return String.format("La Carta Utensile in uso è: %s, la cui descrizione: %s",
                        toolCardWrapper.getName(), toolCardWrapper.getDescription());
        }
        return "";
    }

}