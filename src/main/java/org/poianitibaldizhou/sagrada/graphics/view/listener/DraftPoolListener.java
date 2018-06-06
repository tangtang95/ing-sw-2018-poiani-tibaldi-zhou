package org.poianitibaldizhou.sagrada.graphics.view.listener;

import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IDraftPoolObserver;
import org.poianitibaldizhou.sagrada.graphics.view.DraftPoolView;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DraftPoolListener extends UnicastRemoteObject implements IDraftPoolObserver{

    private final transient DraftPoolView draftPoolView;

    public DraftPoolListener(DraftPoolView draftPoolView) throws RemoteException {
        this.draftPoolView = draftPoolView;
    }

    public DraftPoolView getDraftPoolView() {
        return draftPoolView;
    }

    @Override
    public void onDiceAdd(String dice) throws IOException {

    }

    @Override
    public void onDiceRemove(String dice) throws IOException {

    }

    @Override
    public void onDicesAdd(String dices) throws IOException {

    }

    @Override
    public void onDraftPoolReroll(String dices) throws IOException {

    }

    @Override
    public void onDraftPoolClear() throws IOException {

    }
}
