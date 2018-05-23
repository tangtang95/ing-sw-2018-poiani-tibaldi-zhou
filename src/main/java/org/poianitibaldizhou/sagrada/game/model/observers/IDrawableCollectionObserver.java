package org.poianitibaldizhou.sagrada.game.model.observers;

import java.util.List;

public interface IDrawableCollectionObserver<T> {
    /**
     * Notify that an element has been added to the DrawableCollection
     *
     * @param elem element added
     */
    void onElementAdd(T elem);

    /**
     * Notify that a list of elements has been added to the DrawableCollection
     *
     * @param elem list of elements added
     */
    void onElementsAdd(List<T> elem);

    /**
     * Notify that an element has been removed from the DrawableCollection
     *
     * @param elem element removed
     */
    void onElementDraw(T elem);
}
