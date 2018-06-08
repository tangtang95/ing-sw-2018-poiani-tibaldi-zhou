package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IStateObserver;
import org.poianitibaldizhou.sagrada.graphics.controller.MultiPlayerController;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StateListener extends AbstractView implements IStateObserver {

    public StateListener(MultiPlayerController controller, Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
    }

    @Override
    public void onSetupGame() throws IOException {

        //TODO opacity pane with height a quarter and set thread sleep if last time create label was....
        Platform.runLater(()->{
            createLabelMessage("Setup Game", corePane);
        });


    }

    @Override
    public void onSetupPlayer() throws IOException {
        Platform.runLater(()->{
            Pane statePane = getBackgroundPane();
            createLabelMessage("Setup Player", corePane);
        });
    }

    @Override
    public void onRoundStart(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        int round = parser.getValue(message);
        UserWrapper roundUser = parser.getRoundUser(message);
        Platform.runLater(()->{
            createLabelMessage(String.format("Round %s del giocatore: %s",
                    String.valueOf(round + 1), roundUser.getUsername()), corePane);
        });
    }

    @Override
    public void onTurnState(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        int turn = parser.getTurnValue(message);
        UserWrapper turnUser = parser.getTurnUserWrapper(message);
        Platform.runLater(() -> {
            createLabelMessage(String.format("%s turno del giocatore: %s",
                    getOrdinalNumberStrings().get(turn - 1), turnUser.getUsername()), corePane);
        });
    }

    @Override
    public void onRoundEnd(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        int round = parser.getValue(message);
        UserWrapper roundUser = parser.getRoundUser(message);
        Platform.runLater(() -> {
            createLabelMessage(String.format("Fine del round %s del giocatore: %s",
                    String.valueOf(round + 1), roundUser.getUsername()), corePane);
        });
    }

    @Override
    public void onEndGame(String roundUser) throws IOException {
        Platform.runLater(() -> {
            createLabelMessage("Fine del gioco", corePane);
        });
    }

    @Override
    public void onSkipTurnState(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        UserWrapper turnUser = parser.getTurnUserWrapper(message);
        Platform.runLater(() -> {
            createLabelMessage(String.format("E\' stato saltato il turno del giocatore: %s",
                    turnUser.getUsername()), corePane);
        });
    }

    @Override
    public void onPlaceDiceState(String message) throws IOException {
        /*NOT IMPORTANT FOR THE GUI*/
    }

    @Override
    public void onUseCardState(String message) throws IOException {
        /*NOT IMPORTANT FOR THE GUI*/
    }

    @Override
    public void onEndTurnState(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        int turn = parser.getTurnValue(message);
        UserWrapper turnUser = parser.getTurnUserWrapper(message);
        Platform.runLater(() -> {
            createLabelMessage(String.format("Fine del %s turno del giocatore: %s",
                    getOrdinalNumberStrings().get(turn - 1), turnUser.getUsername()), corePane);
        });
    }

    @Override
    public void onVictoryPointsCalculated(String victoryPoints) throws IOException {
        // TODO
    }

    @Override
    public void onResultGame(String winner) throws IOException {
        // TODO
    }

    private Label createLabelMessage(String text, Pane pane) {
        Label label = new Label(text);
        label.getStyleClass().add("state-message");
        label.setTextFill(Color.DEEPSKYBLUE);
        label.translateYProperty().bind(getCenterY().subtract(getCenterY().divide(4)));
        pane.getChildren().add(label);
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(3000), label);
        translateTransition.fromXProperty().bind(label.widthProperty().negate());
        translateTransition.toXProperty().bind(getWidth().add(label.widthProperty()));
        translateTransition.setInterpolator(Interpolator.LINEAR);
        translateTransition.setCycleCount(1);
        translateTransition.play();
        translateTransition.setOnFinished(event -> pane.getChildren().remove(label));

        return label;
    }

    private List<String> getOrdinalNumberStrings() {
        List<String> ordinalNumberStrings = new ArrayList<>();
        ordinalNumberStrings.add("Primo");
        ordinalNumberStrings.add("Secondo");
        ordinalNumberStrings.add("Terzo");
        ordinalNumberStrings.add("Quarto");
        ordinalNumberStrings.add("Quinto");
        ordinalNumberStrings.add("Sesto");
        ordinalNumberStrings.add("Settimo");
        ordinalNumberStrings.add("Ottavo");
        ordinalNumberStrings.add("Nono");
        ordinalNumberStrings.add("Decimo");
        return ordinalNumberStrings;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StateListener;
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }

}
