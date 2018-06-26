package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IDraftPoolObserver;
import org.poianitibaldizhou.sagrada.graphics.controller.GameGraphicsController;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;
import org.poianitibaldizhou.sagrada.graphics.view.component.DraftPoolView;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DraftPoolWrapper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

public class DraftPoolListener extends AbstractView implements IDraftPoolObserver{

    private final transient DraftPoolView draftPoolView;

    private static final double DRAFT_POOL_SCALE = 0.2;

    /**
     * Constructor.
     * Create a draft pool listener that update its draftPoolView every time a notify is called
     *
     * @param controller the game controller of the GUI
     * @param corePane the core view of the game
     * @param notifyPane the view of the game to show the image on a greater size
     * @throws RemoteException network error
     */
    public DraftPoolListener(GameGraphicsController controller, Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
        DropShadow dropShadow = new DropShadow(4, 4, 4, Color.GRAY);
        draftPoolView = new DraftPoolView(DRAFT_POOL_SCALE);

        DoubleBinding x = getWidth().subtract(draftPoolView.widthProperty().divide(1.5));
        DoubleBinding y = getCenterY().add(getHeight().divide(3.5));

        draftPoolView.translateXProperty().bind(getPivotX(x, draftPoolView.widthProperty(), 0.5));
        draftPoolView.translateYProperty().bind(getPivotX(y, draftPoolView.heightProperty(), 0.5));
        draftPoolView.setEffect(dropShadow);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateView() {
        try {
            DraftPoolWrapper draftPoolWrapper = controller.getDraftPool();
            draftPoolView.drawDraftPool(draftPoolWrapper);
            System.out.println("update draft pool");
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceAdd(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        DiceWrapper dice = parser.getDice(message);
        Platform.runLater(() -> {
            draftPoolView.addDiceToDraftPool(dice);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceRemove(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        DiceWrapper dice = parser.getDice(message);
        Platform.runLater(() -> {
            draftPoolView.removeDiceFromDraftPool(dice);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDicesAdd(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        List<DiceWrapper> dices = parser.getDiceList(message);
        Platform.runLater(() -> {
            dices.forEach(dice -> draftPoolView.removeDiceFromDraftPool(dice));
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDraftPoolReroll(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        List<DiceWrapper> dices = parser.getDiceList(message);
        Platform.runLater(() -> {
            draftPoolView.reRollDraftPool(dices);
        });
    }

    /**
     * {@inheritDoc}
     */
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
