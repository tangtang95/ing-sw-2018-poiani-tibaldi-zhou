package org.poianitibaldizhou.sagrada.graphics.view.listener;

import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IStateObserver;
import org.poianitibaldizhou.sagrada.graphics.controller.MultiPlayerController;
import org.poianitibaldizhou.sagrada.graphics.utils.GraphicsUtils;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;
import org.poianitibaldizhou.sagrada.graphics.view.MessageType;
import org.poianitibaldizhou.sagrada.graphics.view.component.DiceView;
import org.poianitibaldizhou.sagrada.graphics.view.component.SchemaCardView;
import org.poianitibaldizhou.sagrada.graphics.view.component.ToolCardView;
import org.poianitibaldizhou.sagrada.graphics.view.listener.executorListener.HistoryObject;
import org.poianitibaldizhou.sagrada.graphics.view.listener.executorListener.ObjectMessageType;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StateListener extends AbstractView implements IStateObserver {

    private transient final SequentialTransition sequentialTransition;

    private transient HBox helperBox;
    private transient HBox topBarBox;

    private static final double DURATION_IN_MILLIS = 1500;

    private static final double SCHEMA_CARD_SHOW_SIZE = 0.45;
    private static final double TOOL_CARD_SHOW_SIZE = 0.7;
    private static final double DICE_SHOW_SIZE = 0.3;

    public StateListener(MultiPlayerController controller, Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
        sequentialTransition = new SequentialTransition();
    }

    @Override
    public void onSetupGame() throws IOException {
        Platform.runLater(() -> {
            clearNotifyPane(false);
            deactivateNotifyPane();
        });

    }

    @Override
    public void onSetupPlayer() throws IOException {
        Platform.runLater(() -> {
            clearNotifyPane(false);
            activateNotifyPane();
            Label stateMessageLabel = createLabelMessage("Setup Player");
            notifyPane.getChildren().add(stateMessageLabel);
        });
    }

    @Override
    public void onRoundStart(String message) throws IOException {

    }

    @Override
    public void onTurnState(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        int turn = parser.getTurnValue(message);
        int round = parser.getValue(message);
        UserWrapper turnUser = parser.getTurnUserWrapper(message);
        Platform.runLater(() -> {
            corePane.getChildren().remove(helperBox);
            corePane.getChildren().remove(topBarBox);
            topBarBox = showTopBarText(corePane, String.format("%s Round - Turno %s di %s",
                    getOrdinalNumberStrings().get(round), turn,  turnUser.getUsername().toUpperCase()));
            topBarBox.setOpacity(0.7);
            topBarBox.setAlignment(Pos.CENTER);
            if(turnUser.getUsername().equals(controller.getUsername())) {
                //SHOW COMMANDS
                helperBox = showHelperText(corePane, "Tocca a te, scegli una delle seguenti azioni");

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.SOMETIMES);

                JFXButton placeDiceButton = GraphicsUtils.getButton("Piazza un dado", "positive-button");
                JFXButton useCardButton = GraphicsUtils.getButton("Usa una Carta Utensile", "positive-button");
                JFXButton endTurnButton = GraphicsUtils.getButton("Termina il tuo turno", "negative-button");

                placeDiceButton.setOnAction(this::onPlaceDiceButtonPressed);
                useCardButton.setOnAction(this::onUseCardButtonPressed);
                endTurnButton.setOnAction(event -> {
                    placeDiceButton.setDisable(true);
                    useCardButton.setDisable(true);
                    endTurnButton.setDisable(true);
                    onEndTurnButtonPressed(event);
                });

                helperBox.getChildren().addAll(spacer, placeDiceButton, useCardButton, endTurnButton);
            }
            else{
                helperBox = showHelperText(corePane, String.format("%s turno del giocatore: %s",
                        getOrdinalNumberStrings().get(turn - 1), turnUser.getUsername()));
            }
            helperBox.setOpacity(0.7);
        });
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
        UserWrapper turnUser = parser.getTurnUserWrapper(message);
        Platform.runLater(() -> {
            // TODO
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

    @Override
    public void onGameTerminationBeforeStarting() throws IOException {
        // TODO :)
    }

    private void onPlaceDiceButtonPressed(ActionEvent actionEvent) {
        clearNotifyPane(false);
        activateNotifyPane();

        HBox helperPane = showHelperText(notifyPane, "Piazza un dado della Riserva sulla tua Carta Schema " +
                "rispettando le regole");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.SOMETIMES);

        JFXButton cancelButton = GraphicsUtils.getButton("Annulla", "negative-button");

        cancelButton.setOnAction(this::onCancelButtonPressed);

        helperPane.getChildren().addAll(spacer, cancelButton);

        DropShadow dropShadow = new DropShadow(4, 4, 4, Color.BLACK);

        try {
            SchemaCardWrapper mySchemaCard = controller.getOwnSchemaCard();
            DraftPoolWrapper draftPool = controller.getDraftPool();

            SchemaCardView schemaCardView = new SchemaCardView(mySchemaCard, SCHEMA_CARD_SHOW_SIZE);
            schemaCardView.translateXProperty().bind(getPivotX(getCenterX(), schemaCardView.widthProperty(), 0.5));
            schemaCardView.translateYProperty().bind(getPivotY(getCenterY(), schemaCardView.widthProperty(), 0.33));
            schemaCardView.setEffect(dropShadow);

            schemaCardView.setOnDragOver(event -> {
                double x = event.getX();
                double y = event.getY();
                schemaCardView.drawShadow(x,y);
                event.acceptTransferModes(TransferMode.MOVE);
                event.consume();
            });

            schemaCardView.setOnDragDropped(event -> {
                onSchemaCardDragDropped(event, schemaCardView);
                event.consume();
            });

            notifyPane.getChildren().add(schemaCardView);

            DoubleBinding diceY = getCenterY().add(getHeight().divide(4));

            for (int i = 0; i < draftPool.size(); i++) {
                DiceView diceView = new DiceView(draftPool.getDice(i), DICE_SHOW_SIZE);
                double totalWidth = diceView.getImageWidth() * (draftPool.size())
                         + ((draftPool.size()-1)*PADDING*2);
                double startDiceX = getCenterX().get() - totalWidth/2;
                double x = startDiceX + (i*PADDING*2) + (diceView.getImageWidth()*i);

                diceView.setTranslateX(x);
                diceView.setTranslateY(diceY.get());
                diceView.setEffect(dropShadow);

                diceView.setOnDragDetected(event -> {
                    Dragboard dragboard = diceView.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent clipboardContent = new ClipboardContent();
                    clipboardContent.putImage(diceView.getImage());
                    clipboardContent.putString(diceView.getDiceWrapper().toJSON().toJSONString());
                    dragboard.setContent(clipboardContent);
                    event.consume();
                });

                notifyPane.getChildren().add(diceView);
            }

        } catch (IOException e) {
            e.printStackTrace();
            showCrashErrorMessage("Errore di connessione");
            return;
        }
    }

    private void onSchemaCardDragDropped(DragEvent event, SchemaCardView schemaCardView){
        schemaCardView.removeShadow();
        final Dragboard dragboard = event.getDragboard();
        double x = event.getX();
        double y = event.getY();
        PositionWrapper positionWrapper = schemaCardView.getTilePosition(x, y);
        JSONParser parser = new JSONParser();
        try {
            JSONObject diceObject = (JSONObject) ((JSONObject) parser.parse(dragboard.getString())).get("body");
            DiceWrapper dice = DiceWrapper.toObject(diceObject);
            controller.placeDice(dice, positionWrapper);
            event.setDropCompleted(true);
            event.consume();
        } catch (ParseException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Parsing error");
        } catch (IOException e) {
            showCrashErrorMessage("Errore di connessione");
        }
    }

    private void onUseCardButtonPressed(ActionEvent actionEvent){
        clearNotifyPane(false);
        activateNotifyPane();

        HBox helperPane = showHelperText(notifyPane, "Scegli una delle Carte Utensili");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.SOMETIMES);

        JFXButton continueButton = GraphicsUtils.getButton("Continua", "positive-button");
        JFXButton cancelButton = GraphicsUtils.getButton("Annulla", "negative-button");


        cancelButton.setOnAction(this::onCancelButtonPressed);

        helperPane.getChildren().addAll(spacer, continueButton, cancelButton);

        try {
            List<ToolCardWrapper> toolCardWrappers = controller.getToolCards();
            List<Pane> toolCardViews = new ArrayList<>();
            toolCardWrappers.forEach(toolCard -> {
                ToolCardView toolCardView = new ToolCardView(toolCard, TOOL_CARD_SHOW_SIZE);
                toolCardViews.add(toolCardView);
            });
            drawCenteredPanes(notifyPane, toolCardViews, "on-notify-pane-card");
            ToggleGroup toggleGroup = new ToggleGroup();
            drawRadioButtons(toggleGroup, toolCardViews);
            continueButton.setOnAction(event -> onUseCardContinueButtonPressed(event, toggleGroup));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onCancelButtonPressed(ActionEvent actionEvent) {
        clearNotifyPane(false);
        deactivateNotifyPane();
    }

    private void onUseCardContinueButtonPressed(ActionEvent actionEvent, ToggleGroup toggleGroup){
        if (toggleGroup.getSelectedToggle() == null) {
            showMessage(notifyPane, "Devi scegliere una Carta Utensile", MessageType.ERROR);
            return;
        }
        ToolCardView toolCardView = (ToolCardView) toggleGroup.getSelectedToggle().getUserData();
        ToolCardWrapper toolCardWrapper = toolCardView.getToolCardWrapper();
        try {
            ToolCardExecutorListener toolCardExecutorObserver = new ToolCardExecutorListener(controller,
                    corePane, notifyPane);
            toolCardExecutorObserver.addHistoryMessage(new HistoryObject(toolCardWrapper, ObjectMessageType.TOOL_CARD));
            controller.useToolCard(toolCardWrapper, toolCardExecutorObserver);
            toggleGroup.getToggles().forEach(toggle -> ((RadioButton)toggle).setDisable(true));
            ((Button) actionEvent.getSource()).setDisable(true);
        } catch (IOException e) {
            showMessage(notifyPane, "Errore di connessione", MessageType.ERROR);
        }
    }

    private void onEndTurnButtonPressed(ActionEvent actionEvent){
        try {
            controller.endTurn();
        } catch (IOException e) {
            e.printStackTrace();
            showCrashErrorMessage("Errore di connessione");
        }
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
