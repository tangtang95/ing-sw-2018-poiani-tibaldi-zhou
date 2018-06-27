package org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces;

import java.util.List;

/**
 * OVERVIEW: Fake observer for the drawable collection .
 * Fake observers are observers present in the server that listen to changes and modification.
 * In this way, the network part is decoupled from the model.
 *
 * @param <T> type of the elements present in the drawable collection
 */
public interface IDrawableCollectionFakeObserver<T> {
    /**
     * Notify that an element has been added to the DrawableCollection
     *
     * @param elem element added
     */
    void onElementAdd(T elem);

    /**
     * Notify that a list of elements has been added to the DrawableCollection
     *
     * @param elemList list of elements added
     */
    void onElementsAdd(List<T> elemList);


    /**
     * Notify that an element has been removed from the DrawableCollection
     *
     * @param elem element removed
     */
    void onElementDraw(T elem);
}
