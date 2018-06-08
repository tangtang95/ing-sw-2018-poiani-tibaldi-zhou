package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.graphics.controller.MultiPlayerController;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;

import java.io.IOException;
import java.rmi.RemoteException;

public class ToolCardExecutionListener extends AbstractView implements IToolCardExecutorObserver {

    protected ToolCardExecutionListener(MultiPlayerController controller, Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
    }

    @Override
    public void notifyNeedDice(String diceList) throws IOException {

    }

    @Override
    public void notifyNeedNewValue() throws IOException {

    }

    @Override
    public void notifyNeedColor(String colors) throws IOException {

    }

    @Override
    public void notifyNeedNewDeltaForDice(String message) throws IOException {

    }

    @Override
    public void notifyNeedDiceFromRoundTrack(String roundTrack) throws IOException {

    }

    @Override
    public void notifyNeedPosition(String message) throws IOException {

    }

    @Override
    public void notifyNeedDicePositionOfCertainColor(String message) throws IOException {

    }

    @Override
    public void notifyRepeatAction() throws IOException {

    }

    @Override
    public void notifyCommandInterrupted(String error) throws IOException {

    }

    @Override
    public void notifyNeedContinueAnswer() throws IOException {

    }

    @Override
    public void notifyDiceReroll(String message) throws IOException {

    }
}
