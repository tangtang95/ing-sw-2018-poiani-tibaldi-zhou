package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IDraftPoolObserver;
import org.poianitibaldizhou.sagrada.graphics.controller.MultiPlayerController;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;
import org.poianitibaldizhou.sagrada.graphics.view.component.DraftPoolView;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DraftPoolWrapper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

public class DraftPoolListener extends AbstractView implements IDraftPoolObserver{

    private transient DraftPoolView draftPoolView;

    private static final double DRAFT_POOL_SCALE = 0.2;

    public DraftPoolListener(MultiPlayerController controller, Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
        DropShadow dropShadow = new DropShadow(4, 4, 4, Color.GRAY);
        draftPoolView = new DraftPoolView(DRAFT_POOL_SCALE);

        DoubleBinding x = getWidth().subtract(draftPoolView.widthProperty().divide(1.5));
        DoubleBinding y = getCenterY().add(getHeight().divide(3.5));

        draftPoolView.translateXProperty().bind(getPivotX(x, draftPoolView.widthProperty(), 0.5));
        draftPoolView.translateYProperty().bind(getPivotX(y, draftPoolView.heightProperty(), 0.5));
        draftPoolView.setEffect(dropShadow);
    }

    @Override
    public void updateView() {
        try {
            DraftPoolWrapper draftPoolWrapper = controller.getDraftPool();
            draftPoolView.drawDraftPool(draftPoolWrapper);
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public DraftPoolView getDraftPoolView() {
        return draftPoolView;
    }

    public void drawDraftPool() {
        corePane.getChildren().add(draftPoolView);
    }

    @Override
    public void onDiceAdd(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        DiceWrapper dice = parser.getDice(message);
        Platform.runLater(() -> {
            draftPoolView.addDiceToDraftPool(dice);
        });
    }

    @Override
    public void onDiceRemove(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        DiceWrapper dice = parser.getDice(message);
        Platform.runLater(() -> {
            draftPoolView.removeDiceFromDraftPool(dice);
        });
    }

    @Override
    public void onDicesAdd(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        List<DiceWrapper> dices = parser.getDiceList(message);
        Platform.runLater(() -> {
            dices.forEach(dice -> draftPoolView.removeDiceFromDraftPool(dice));
        });
    }

    @Override
    public void onDraftPoolReroll(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        List<DiceWrapper> dices = parser.getDiceList(message);
        Platform.runLater(() -> {
            draftPoolView.reRollDraftPool(dices);
        });
    }

    @Override
    public void onDraftPoolClear() throws IOException {
        Platform.runLater(() -> {
            draftPoolView.clearDraftPool();
        });
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DraftPoolListener;
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }

}
