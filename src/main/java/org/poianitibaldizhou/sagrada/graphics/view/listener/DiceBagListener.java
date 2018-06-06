package org.poianitibaldizhou.sagrada.graphics.view.listener;

import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IDrawableCollectionObserver;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DiceBagListener extends UnicastRemoteObject implements IDrawableCollectionObserver {

    public DiceBagListener() throws RemoteException {
    }

    @Override
    public void onElementAdd(String elem) throws IOException {

    }

    @Override
    public void onElementsAdd(String elemList) throws IOException {

    }

    @Override
    public void onElementDraw(String elem) throws IOException {

    }
}
