package org.poianitibaldizhou.sagrada.game.model.observers.realobservers;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.IOException;
import java.rmi.Remote;
import java.util.List;

public interface IDrawableCollectionObserver<T> extends Remote {
    /**
     * Notify that an element has been added to the DrawableCollection
     *
     * @param elem element added
     * @throws IOException network error
     */
    void onElementAdd(T elem) throws IOException;

    /**
     * Notify that a list of elements has been added to the DrawableCollection
     *
     * @param elemList list of elements added
     */
    void onElementsAdd(List<T> elemList) throws IOException;


    /**
     * Notify that an element has been removed from the DrawableCollection
     *
     * @param elem element removed
     * @throws IOException network error
     */
    void onElementDraw(T elem) throws IOException;
}