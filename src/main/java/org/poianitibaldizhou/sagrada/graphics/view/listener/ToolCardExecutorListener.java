package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.graphics.controller.MultiPlayerController;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;
import org.poianitibaldizhou.sagrada.graphics.view.MessageType;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.ColorWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

public class ToolCardExecutorListener extends AbstractView implements IToolCardExecutorObserver {

    protected ToolCardExecutorListener(MultiPlayerController controller, Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
    }

    @Override
    public void notifyNeedDice(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        List<DiceWrapper> diceWrapperList = parser.getDiceList(message);
        Platform.runLater(() -> {
            if(getActivePane() != notifyPane){
                clearNotifyPane();
                activateNotifyPane();
            }
            showHelperText(notifyPane, "Scegli uno dei dadi nella lista: ");
            // TODO
        });
    }

    @Override
    public void notifyNeedNewValue() throws IOException {
        Platform.runLater(() -> {
            if(getActivePane() != notifyPane){
                clearNotifyPane();
                activateNotifyPane();
            }
            showHelperText(notifyPane, "Scegli il nuovo valore del dado: ");
            // TODO ASK FOR RICCARDO TO RETURN THE DICE
        });
    }

    @Override
    public void notifyNeedColor(String colors) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        List<ColorWrapper> colorWrapperList = parser.getColorList(colors);
        Platform.runLater(() -> {
            if(getActivePane() != notifyPane){
                clearNotifyPane();
                activateNotifyPane();
            }
            showHelperText(notifyPane, "Scegli uno dei colori indicati per la mossa successiva: ");

        });
    }

    @Override
    public void notifyNeedNewDeltaForDice(String message) throws IOException {
        Platform.runLater(() -> {
            if(getActivePane() != notifyPane){
                clearNotifyPane();
                activateNotifyPane();
            }
            showHelperText(notifyPane, "Scegli il nuovo valore del dado: ");
            // TODO ASK FOR RICCARDO TO RETURN THE DICE
        });
    }

    @Override
    public void notifyNeedDiceFromRoundTrack(String roundTrack) throws IOException {
        Platform.runLater(() -> {
            if(getActivePane() != notifyPane){
                clearNotifyPane();
                activateNotifyPane();
            }
            showHelperText(notifyPane, "Scegli un dado dalla Tracciato dei round: ");
            // TODO print round track
        });
    }

    @Override
    public void notifyNeedPosition(String message) throws IOException {
        Platform.runLater(() -> {
            if(getActivePane() != notifyPane){
                clearNotifyPane();
                activateNotifyPane();
            }
            showHelperText(notifyPane, "Scegli una posizione dalla tua Carta Schema: ");
            // TODO ask riccardo to add a method to the controller to get the matrix of tile placeable
            // TODO print schema card with tile selectable
        });
    }

    @Override
    public void notifyNeedDicePositionOfCertainColor(String message) throws IOException {
        Platform.runLater(() -> {
            if(getActivePane() != notifyPane){
                clearNotifyPane();
                activateNotifyPane();
            }
            showHelperText(notifyPane, "Scegli una posizione dalla tua Carta Schema di un certo colore: ");
            // TODO ask riccardo to add a method to the controller to get the matrix of tile placeable
            // TODO print schema card with tile selectable
        });
    }

    @Override
    public void notifyRepeatAction() throws IOException {
        Platform.runLater(() -> {
            if(getActivePane() != notifyPane){
                activateNotifyPane();
            }
            showMessage(notifyPane, "Hai sbagliato mossa, ripeti", MessageType.ERROR);
        });
    }

    @Override
    public void notifyCommandInterrupted(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        String error = parser.getCommandFlow(message);
        Platform.runLater(() -> {
            clearNotifyPane();
            deactivateNotifyPane();
            showMessage(corePane, "L'esecuzione della Carta Utensile è stata interrotta per errore: " + error,
                    MessageType.ERROR);
        });
    }

    @Override
    public void notifyNeedContinueAnswer() throws IOException {
        Platform.runLater(() -> {
            if(getActivePane() != notifyPane){
                clearNotifyPane();
                activateNotifyPane();
            }
            showHelperText(notifyPane, "Vuoi continuare l'esecuzione della Carta Utensile?");
            // TODO
        });
    }

    @Override
    public void notifyDiceReroll(String message) throws IOException {
        if(getActivePane() != notifyPane){
            clearNotifyPane();
            activateNotifyPane();
        }
        showHelperText(notifyPane, "Vuoi continuare l'esecuzione della Carta Utensile?");
    }
}
