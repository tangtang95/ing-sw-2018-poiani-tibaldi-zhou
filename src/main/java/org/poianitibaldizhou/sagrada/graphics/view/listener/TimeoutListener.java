package org.poianitibaldizhou.sagrada.graphics.view.listener;

import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.ITimeOutObserver;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class TimeoutListener extends UnicastRemoteObject implements ITimeOutObserver {

    public TimeoutListener() throws RemoteException {

    }

    @Override
    public void onTimeOut(String message) throws IOException {

    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TimeoutListener;
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }
}
