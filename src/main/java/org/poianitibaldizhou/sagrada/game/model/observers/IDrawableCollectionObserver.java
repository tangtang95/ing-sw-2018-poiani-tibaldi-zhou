package org.poianitibaldizhou.sagrada.game.model.observers;

import java.util.List;

public interface IDrawableCollectionObserver<T> {
    void onElementAdd(T elem);
    void onElementsAdd(List<T> elem);
    void onElementDraw(T elem);
}
