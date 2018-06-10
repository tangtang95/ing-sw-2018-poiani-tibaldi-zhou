package org.poianitibaldizhou.sagrada.graphics.view.listener.executorListener;

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

}