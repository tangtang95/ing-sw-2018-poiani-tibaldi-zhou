package org.poianitibaldizhou.sagrada.network.observers.realobservers;

import java.io.IOException;
import java.rmi.Remote;

public interface IDrawableCollectionObserver extends Remote {
    /**
     * Notify that an element has been added to the DrawableCollection
     *
     * @param elem element added
     * @throws IOException network error
     */
    void onElementAdd(String elem) throws IOException;

    /**
     * Notify that a list of elements has been added to the DrawableCollection
     *
     * @param elemList list of elements added
     */
    void onElementsAdd(String elemList) throws IOException;


    /**
     * Notify that an element has been removed from the DrawableCollection
     *
     * @param elem element removed
     * @throws IOException network error
     */
    void onElementDraw(String elem) throws IOException;
}
