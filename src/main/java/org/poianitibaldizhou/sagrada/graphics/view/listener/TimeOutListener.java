package org.poianitibaldizhou.sagrada.graphics.view.listener;

import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.ITimeOutObserver;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class TimeOutListener extends UnicastRemoteObject implements ITimeOutObserver {

    public TimeOutListener() throws RemoteException {

    }

    @Override
    public void onTimeOut(String message) throws IOException {

    }
}
