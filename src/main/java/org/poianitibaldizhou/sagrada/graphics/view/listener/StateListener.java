package org.poianitibaldizhou.sagrada.graphics.view.listener;

import com.jfoenix.controls.JFXButton;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IStateObserver;
import org.poianitibaldizhou.sagrada.graphics.controller.MultiPlayerController;
import org.poianitibaldizhou.sagrada.graphics.utils.TextureUtils;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StateListener extends AbstractView implements IStateObserver {

    private final SequentialTransition sequentialTransition;

    private HBox helperBox;

    private static final double DURATION_IN_MILLIS = 1500;

    public StateListener(MultiPlayerController controller, Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
        sequentialTransition = new SequentialTransition();
    }

    @Override
    public void onSetupGame() throws IOException {
        //TODO opacity pane with height a quarter and set thread sleep if last time create label was....
        System.out.println("onSetupGame");
        Platform.runLater(() -> {
            clearNotifyPane();
            deactivateNotifyPane();
            Label stateMessageLabel = createLabelMessage("Setup Game");
            corePane.getChildren().add(stateMessageLabel);
        });


    }

    @Override
    public void onSetupPlayer() throws IOException {
        System.out.println("onSetupPlayer");
        Platform.runLater(() -> {
            clearNotifyPane();
            activateNotifyPane();
            Label stateMessageLabel = createLabelMessage("Setup Player");
            notifyPane.getChildren().add(stateMessageLabel);
        });
    }

    @Override
    public void onRoundStart(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        int round = parser.getValue(message);
        UserWrapper roundUser = parser.getRoundUser(message);
        Platform.runLater(() -> {
            Label stateMessageLabel = createLabelMessage(String.format("Round %s del giocatore: %s",
                    String.valueOf(round + 1), roundUser.getUsername()));
            corePane.getChildren().add(stateMessageLabel);

        });
    }

    @Override
    public void onTurnState(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        int turn = parser.getTurnValue(message);
        UserWrapper turnUser = parser.getTurnUserWrapper(message);
        Platform.runLater(() -> {
            if(turnUser.getUsername().equals(controller.getUsername())) {
                //SHOW COMMANDS
                helperBox = showHelperText(corePane, "Tocca a te, scegli una delle seguenti azioni");

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.SOMETIMES);

                JFXButton placeDiceButton = TextureUtils.getButton("Piazza un dado", "positive-button");
                JFXButton useCardButton = TextureUtils.getButton("Usa una Carta Utensile", "positive-button");
                JFXButton endTurnButton = TextureUtils.getButton("Termina il tuo turno", "negative-button");

                placeDiceButton.setOnAction(this::onPlaceDiceButtonPressed);
                useCardButton.setOnAction(this::onUseCardButtonPressed);
                endTurnButton.setOnAction(this::onEndTurnButtonPressed);

                helperBox.getChildren().addAll(spacer, placeDiceButton, useCardButton, endTurnButton);

                //TODO add notify to disable some button (???);
            }
            else{
                showHelperText(corePane, String.format("%s turno del giocatore: %s",
                        getOrdinalNumberStrings().get(turn - 1), turnUser.getUsername()));
            }
        });
    }

    private void onPlaceDiceButtonPressed(ActionEvent actionEvent) {
        clearNotifyPane();
        activateNotifyPane();

        HBox helperPane = showHelperText(notifyPane, "Piazza un dado della Riserva sulla tua Carta Schema " +
                "rispettando le regole");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.SOMETIMES);

        JFXButton cancelButton = TextureUtils.getButton("Annulla", "negative-button");

        cancelButton.setOnAction(this::onCancelButtonPressed);

        helperPane.getChildren().addAll(spacer, cancelButton);

        // TODO show draft pool dices and own schemaCard
    }

    private void onUseCardButtonPressed(ActionEvent actionEvent){
        clearNotifyPane();
        activateNotifyPane();

        HBox helperPane = showHelperText(notifyPane, "Scegli una delle Carte Utensili");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.SOMETIMES);

        JFXButton continueButton = TextureUtils.getButton("Continua", "positive-button");
        JFXButton cancelButton = TextureUtils.getButton("Annulla", "negative-button");

        continueButton.setOnAction(this::onUseCardContinueButtonPressed);
        cancelButton.setOnAction(this::onCancelButtonPressed);

        helperPane.getChildren().addAll(spacer, continueButton, cancelButton);

        // TODO show toolCard
    }

    private void onCancelButtonPressed(ActionEvent actionEvent) {
        clearNotifyPane();
        deactivateNotifyPane();
    }

    private void onUseCardContinueButtonPressed(ActionEvent actionEvent){

    }

    private void onEndTurnButtonPressed(ActionEvent actionEvent){
        controller.endTurn();
    }


    @Override
    public void onRoundEnd(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        int round = parser.getValue(message);
        UserWrapper roundUser = parser.getRoundUser(message);
        Platform.runLater(() -> {
            Label stateMessageLabel = createLabelMessage(String.format("Fine del round %s del giocatore: %s",
                    String.valueOf(round + 1), roundUser.getUsername()));
            corePane.getChildren().add(stateMessageLabel);
        });
    }

    @Override
    public void onEndGame(String roundUser) throws IOException {
        Platform.runLater(() -> {
            Label stateMessageLabel = createLabelMessage("Fine del gioco");
            corePane.getChildren().add(stateMessageLabel);
        });
    }

    @Override
    public void onSkipTurnState(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        UserWrapper turnUser = parser.getTurnUserWrapper(message);
        Platform.runLater(() -> {
            Label stateMessageLabel = createLabelMessage(String.format("E\' stato saltato il turno del giocatore: %s",
                    turnUser.getUsername()));
            corePane.getChildren().add(stateMessageLabel);
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
            Label stateMessageLabel = createLabelMessage(String.format("Fine del %s turno del giocatore: %s",
                    getOrdinalNumberStrings().get(turn - 1), turnUser.getUsername()));
            corePane.getChildren().add(stateMessageLabel);
            // DEACTIVATE COMMANDS
            corePane.getChildren().remove(helperBox);
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

    private Label createLabelMessage(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("state-message");
        label.setTextFill(Color.TOMATO);
        label.translateXProperty().bind(getPivotX(getCenterX(), label.widthProperty(), 0.5));
        label.translateYProperty().bind(getCenterY().subtract(getCenterY().divide(1.3)));
        label.setOpacity(1);

        FadeTransition transition = new FadeTransition(Duration.millis(DURATION_IN_MILLIS), label);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.setInterpolator(Interpolator.LINEAR);
        transition.setCycleCount(6);
        transition.setAutoReverse(true);
        sequentialTransition.getChildren().add(transition);
        sequentialTransition.play();

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
