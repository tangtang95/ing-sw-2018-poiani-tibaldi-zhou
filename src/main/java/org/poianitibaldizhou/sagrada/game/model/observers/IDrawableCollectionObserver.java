package org.poianitibaldizhou.sagrada.game.model.observers;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IDrawableCollectionObserver<T> extends Remote {
    /**
     * Notify that an element has been added to the DrawableCollection
     *
     * @param elem element added
     * @throws RemoteException network error
     */
    void onElementAdd(T elem) throws RemoteException;

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
     * @throws RemoteException network error
     */
    void onElementDraw(T elem) throws RemoteException;
}
