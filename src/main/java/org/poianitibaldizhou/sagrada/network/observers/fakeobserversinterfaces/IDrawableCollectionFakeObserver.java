package org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces;

import java.util.List;

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
