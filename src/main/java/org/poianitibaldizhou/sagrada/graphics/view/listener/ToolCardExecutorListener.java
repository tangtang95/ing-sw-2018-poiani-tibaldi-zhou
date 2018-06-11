package org.poianitibaldizhou.sagrada.graphics.view.listener;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.graphics.controller.MultiPlayerController;
import org.poianitibaldizhou.sagrada.graphics.utils.GraphicsUtils;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;
import org.poianitibaldizhou.sagrada.graphics.view.MessageType;
import org.poianitibaldizhou.sagrada.graphics.view.component.ColorView;
import org.poianitibaldizhou.sagrada.graphics.view.component.DiceView;
import org.poianitibaldizhou.sagrada.graphics.view.component.SchemaCardView;
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

public class ToolCardExecutorListener extends AbstractView implements IToolCardExecutorObserver {

    private final transient List<HistoryObject> historyMessages;

    private static final double DICE_SHOW_SCALE = 0.5;
    private static final double TILE_SHOW_SCALE = 0.5;
    private static final double SCHEMA_CARD_SHOW_SCALE = 0.45;
    private static final double DICE_SCHEMA_SHOW_SCALE = 0.3;

    protected ToolCardExecutorListener(MultiPlayerController controller, Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
        historyMessages = new ArrayList<>();
    }

    @Override
    public void notifyNeedDice(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        List<DiceWrapper> diceWrapperList = parser.getDiceList(message);
        Platform.runLater(() -> {
            clearNotifyPane();
            HBox helperBox = showHelperText(notifyPane, "Scegli uno dei dadi nella lista: ");
            List<Pane> diceViewList = new ArrayList<>();
            diceWrapperList.forEach(diceWrapper -> {
                DiceView diceView = new DiceView(diceWrapper, DICE_SHOW_SCALE);
                diceViewList.add(diceView);
            });
            drawCenteredPanes(notifyPane, diceViewList, "on-notify-pane-card");
            ToggleGroup toggleGroup = new ToggleGroup();
            drawRadioButtons(toggleGroup, diceViewList);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.SOMETIMES);

            JFXButton continueButton = GraphicsUtils.getButton("Continua", "positive-button");
            continueButton.setOnAction(event -> fireDiceEvent(event, toggleGroup));

            helperBox.getChildren().addAll(spacer, continueButton);
        });
    }


    @Override
    public void notifyNeedNewValue() throws IOException {
        Platform.runLater(() -> {
            clearNotifyPane();
            HBox helperBox = showHelperText(notifyPane, "Scegli il nuovo valore del dado: ");
            try {
                HistoryObject historyObject = getMostRecentDiceMessage();
                DiceWrapper diceWrapper = (DiceWrapper) historyObject.getObject();
                List<Pane> diceViewList = new ArrayList<>();
                for (int i = DiceWrapper.MIN_VALUE; i <= DiceWrapper.MAX_VALUE; i++) {
                    DiceView diceView = new DiceView(new DiceWrapper(diceWrapper.getColor(), i), DICE_SHOW_SCALE);
                    diceViewList.add(diceView);
                }
                drawCenteredPanes(notifyPane, diceViewList, "on-notify-pane-card");
                ToggleGroup toggleGroup = new ToggleGroup();
                drawRadioButtons(toggleGroup, diceViewList);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.SOMETIMES);

                JFXButton continueButton = GraphicsUtils.getButton("Continua", "positive-button");
                continueButton.setOnAction(event -> fireDiceEvent(event, toggleGroup));

                helperBox.getChildren().addAll(spacer, continueButton);
            } catch (IOException e) {
                showCrashErrorMessage("Errore di sincronizzazione");
                e.printStackTrace();
            }
        });
    }

    @Override
    public void notifyNeedColor(String colors) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        List<ColorWrapper> colorWrapperList = parser.getColorList(colors);
        Platform.runLater(() -> {
            clearNotifyPane();
            HBox helperBox = showHelperText(notifyPane, "Scegli uno dei colori indicati per la mossa successiva: ");
            List<Pane> colorViewList = new ArrayList<>();
            colorWrapperList.forEach(colorWrapper -> {
                ColorView colorView = new ColorView(colorWrapper, TILE_SHOW_SCALE);
                colorViewList.add(colorView);
            });
            drawCenteredPanes(notifyPane, colorViewList, "on-notify-pane-card");
            ToggleGroup toggleGroup = new ToggleGroup();
            drawRadioButtons(toggleGroup, colorViewList);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.SOMETIMES);

            JFXButton continueButton = GraphicsUtils.getButton("Continua", "positive-button");
            continueButton.setOnAction(event -> fireColorEvent(event, toggleGroup));

            helperBox.getChildren().addAll(spacer, continueButton);
        });
    }


    @Override
    public void notifyNeedNewDeltaForDice(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        int deltaValue = parser.getValue(message);
        Platform.runLater(() -> {
            clearNotifyPane();
            HBox helperBox = showHelperText(notifyPane, "Scegli il nuovo valore del dado: ");
            try {
                HistoryObject historyObject = getMostRecentDiceMessage();
                DiceWrapper diceWrapper = (DiceWrapper) historyObject.getObject();
                List<Pane> diceViewList = new ArrayList<>();
                if (diceWrapper.getNumber() + deltaValue <= DiceWrapper.MAX_VALUE)
                    diceViewList.add(new DiceView(new DiceWrapper(diceWrapper.getColor(),
                            diceWrapper.getNumber() + deltaValue), DICE_SHOW_SCALE));
                if (diceWrapper.getNumber() - deltaValue >= DiceWrapper.MIN_VALUE)
                    diceViewList.add(new DiceView(new DiceWrapper(diceWrapper.getColor(),
                            diceWrapper.getNumber() - deltaValue), DICE_SHOW_SCALE));
                drawCenteredPanes(notifyPane, diceViewList, "on-notify-pane-card");
                ToggleGroup toggleGroup = new ToggleGroup();
                drawRadioButtons(toggleGroup, diceViewList);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.SOMETIMES);

                JFXButton continueButton = GraphicsUtils.getButton("Continua", "positive-button");
                continueButton.setOnAction(event -> fireDiceEvent(event, toggleGroup));

                helperBox.getChildren().addAll(spacer, continueButton);
            } catch (IOException e) {
                showCrashErrorMessage("Errore di sincronizzazione");
                e.printStackTrace();
            }
        });
    }

    @Override
    public void notifyNeedDiceFromRoundTrack(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        RoundTrackWrapper roundTrackWrapper = parser.getRoundTrack(message);

        Platform.runLater(() -> {
            clearNotifyPane();
            showHelperText(notifyPane, "Scegli un dado dal Tracciato dei round: ");
            // TODO print round track
        });
    }

    @Override
    public void notifyNeedPosition(String message) throws IOException {
        ClientGetMessage protocolParser = new ClientGetMessage();
        SchemaCardWrapper schemaCardWrapper = protocolParser.getSchemaCard(message);
        Platform.runLater(() -> {
            clearNotifyPane();
            DropShadow dropShadow = new DropShadow(4, 4, 4, Color.BLACK);
            SchemaCardView schemaCardView = new SchemaCardView(schemaCardWrapper, SCHEMA_CARD_SHOW_SCALE);
            schemaCardView.translateXProperty().bind(getPivotX(getCenterX(), schemaCardView.widthProperty(), 0.5));
            schemaCardView.translateYProperty().bind(getPivotY(getCenterY(), schemaCardView.widthProperty(), 0.33));
            schemaCardView.setEffect(dropShadow);
            notifyPane.getChildren().add(schemaCardView);
            HistoryObject historyObject = getMostRecentMessage();
            String helperMessage;
            if (historyObject.getObjectMessageType() == ObjectMessageType.DICE) {
                helperMessage = "Piazza il dado in una cella rispettando le restrizioni di piazzamento della Carta" +
                        "Utensile";
                DiceWrapper diceWrapper = (DiceWrapper) historyObject.getObject();
                DiceView diceView = new DiceView(diceWrapper, DICE_SCHEMA_SHOW_SCALE);

                schemaCardView.setOnDragOver(event -> {
                    double x = event.getX();
                    double y = event.getY();
                    schemaCardView.drawShadow(x, y);
                    event.acceptTransferModes(TransferMode.MOVE);
                    event.consume();
                });

                schemaCardView.setOnDragDropped(event -> {
                    schemaCardView.removeShadow();
                    final Dragboard dragboard = event.getDragboard();
                    double x = event.getX();
                    double y = event.getY();
                    PositionWrapper positionWrapper = schemaCardView.getTilePosition(x, y);
                    schemaCardView.setUserData(positionWrapper);
                    JSONParser parser = new JSONParser();
                    try {
                        JSONObject diceObject = (JSONObject) ((JSONObject) parser.parse(dragboard.getString())).get("body");
                        DiceWrapper dice = DiceWrapper.toObject(diceObject);
                        schemaCardView.drawDice(dice, positionWrapper);
                        event.setDropCompleted(true);
                        event.consume();
                    } catch (ParseException e) {
                        Logger.getAnonymousLogger().log(Level.SEVERE, "Parsing error");
                    }
                });

                diceView.translateXProperty().bind(getCenterX().subtract(diceView.widthProperty().divide(2)));
                diceView.translateYProperty().bind(schemaCardView.translateYProperty()
                        .add(schemaCardView.widthProperty()).add(PADDING * 3));
                diceView.setEffect(dropShadow);
                notifyPane.getChildren().add(diceView);

                diceView.setOnDragDetected(event -> {
                    Dragboard dragboard = diceView.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent clipboardContent = new ClipboardContent();
                    clipboardContent.putImage(diceView.getImage());
                    clipboardContent.putString(diceView.getDiceWrapper().toJSON().toJSONString());
                    dragboard.setContent(clipboardContent);
                    event.consume();
                });
            } else {
                helperMessage = "Rimuovi un dado dalla Carta Schema rispettando la Carta Utensile";

                schemaCardView.setOnMousePressed(event -> {
                    double x = event.getX();
                    double y = event.getY();
                    schemaCardView.removeShadow();
                    schemaCardView.drawShadow(x, y);
                    PositionWrapper positionWrapper = schemaCardView.getTilePosition(x, y);
                    schemaCardView.setUserData(positionWrapper);
                });

            }
            HBox helperBox = showHelperText(notifyPane, helperMessage);
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.SOMETIMES);

            JFXButton continueButton = GraphicsUtils.getButton("Continua", "positive-button");
            continueButton.setOnAction(event -> {
                firePositionEvent(event, schemaCardView);
            });

            helperBox.getChildren().addAll(spacer, continueButton);

        });
    }

    @Override
    public void notifyNeedDicePositionOfCertainColor(String message) throws IOException {
        ClientGetMessage protocolParser = new ClientGetMessage();
        ColorWrapper colorWrapper = protocolParser.getColor(message);
        SchemaCardWrapper schemaCardWrapper = protocolParser.getSchemaCard(message);
        Platform.runLater(() -> {
            clearNotifyPane();
            SchemaCardView schemaCardView = new SchemaCardView(schemaCardWrapper, SCHEMA_CARD_SHOW_SCALE);
            drawCenteredPane(notifyPane, schemaCardView, "");
            String helperMessage = String.format("Rimuovi un dado del color %s dalla Carta Schema rispettando la " +
                    "Carta Utensile", colorWrapper.name().toLowerCase());

            schemaCardView.setOnMousePressed(event -> {
                double x = event.getX();
                double y = event.getY();
                schemaCardView.removeShadow();
                schemaCardView.drawShadow(x, y);
                PositionWrapper positionWrapper = schemaCardView.getTilePosition(x, y);
                if(schemaCardView.getDiceByPosition(positionWrapper) == null)
                    showMessage(notifyPane, "Nessun dado selezionato", MessageType.ERROR);
                else if(schemaCardView.getDiceByPosition(positionWrapper).getColor() != colorWrapper)
                    showMessage(notifyPane, "Il dado selezionato è del colore sbagliato", MessageType.ERROR);
                else
                    schemaCardView.setUserData(positionWrapper);
            });

            HBox helperBox = showHelperText(notifyPane, helperMessage);
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.SOMETIMES);

            JFXButton continueButton = GraphicsUtils.getButton("Continua", "positive-button");
            continueButton.setOnAction(event -> firePositionEvent(event, schemaCardView));

            helperBox.getChildren().addAll(spacer, continueButton);
        });
    }

    @Override
    public void notifyRepeatAction() throws IOException {
        Platform.runLater(() -> {
            if (getActivePane() != notifyPane) {
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
            if (getActivePane() != notifyPane) {
                clearNotifyPane();
                activateNotifyPane();
            }
            HBox helperBox = showHelperText(notifyPane, "Vuoi continuare l'esecuzione della Carta Utensile?");
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.SOMETIMES);

            JFXButton continueButton = GraphicsUtils.getButton("Continua", "positive-button");
            JFXButton cancelButton = GraphicsUtils.getButton("Annulla", "negative-button");
            continueButton.setOnAction(event -> fireAnswerEvent(event, true, cancelButton));
            cancelButton.setOnAction(event -> fireAnswerEvent(event, false, continueButton));

            helperBox.getChildren().addAll(spacer, continueButton, cancelButton);
        });
    }


    @Override
    public void notifyDiceReroll(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        DiceWrapper diceWrapper = parser.getDice(message);
        Platform.runLater(() -> historyMessages.add(new HistoryObject(diceWrapper, ObjectMessageType.DICE)));
    }

    @Override
    public void notifyExecutionEnded() throws IOException {
        clearNotifyPane();
        deactivateNotifyPane();
    }

    @Override
    public void notifyDicePouredOver(String message) throws IOException {
        // TODO :)
    }

    public void addHistoryMessage(HistoryObject message) {
        historyMessages.add(message);
    }

    private void fireDiceEvent(ActionEvent actionEvent, ToggleGroup toggleGroup) {
        if (toggleGroup.getSelectedToggle() == null) {
            showMessage(notifyPane, "Devi scegliere un dado", MessageType.ERROR);
            return;
        }
        DiceView diceView = (DiceView) toggleGroup.getSelectedToggle().getUserData();
        DiceWrapper diceWrapper = diceView.getDiceWrapper();
        try {
            controller.sendDiceObject(diceWrapper);
            toggleGroup.getToggles().forEach((toggle -> ((RadioButton) toggle).setDisable(true)));
            ((Button) actionEvent.getSource()).setDisable(true);
            historyMessages.add(new HistoryObject(diceView.getDiceWrapper(), ObjectMessageType.DICE));
        } catch (IOException e) {
            showMessage(notifyPane, "Errore di connessione", MessageType.ERROR);
        }
    }

    private void fireColorEvent(ActionEvent actionEvent, ToggleGroup toggleGroup) {
        if (toggleGroup.getSelectedToggle() == null) {
            showMessage(notifyPane, "Devi scegliere un colore", MessageType.ERROR);
            return;
        }
        ColorView colorView = (ColorView) toggleGroup.getSelectedToggle().getUserData();
        ColorWrapper colorWrapper = colorView.getColorWrapper();
        try {
            controller.sendColorObject(colorWrapper);
            toggleGroup.getToggles().forEach((toggle -> ((RadioButton) toggle).setDisable(true)));
            ((Button) actionEvent.getSource()).setDisable(true);
            historyMessages.add(new HistoryObject(colorWrapper, ObjectMessageType.COLOR));
        } catch (IOException e) {
            showMessage(notifyPane, "Errore di connessione", MessageType.ERROR);
        }
    }

    private void firePositionEvent(ActionEvent actionEvent, SchemaCardView schemaCardView) {
        try {
            if(schemaCardView.getUserData() == null){
                showMessage(notifyPane, "Devi scegliere una posizione", MessageType.ERROR);
                return;
            }
            PositionWrapper positionWrapper = (PositionWrapper) schemaCardView.getUserData();
            controller.sendPositionObject(positionWrapper);
            ((Button) actionEvent.getSource()).setDisable(true);
            schemaCardView.removeEventHandler(DragEvent.DRAG_DROPPED, schemaCardView.getOnDragDone());
            historyMessages.add(new HistoryObject(positionWrapper, ObjectMessageType.POSITION));
        } catch (IOException e) {
            showMessage(notifyPane, "Errore di connessione", MessageType.ERROR);
        }
    }

    private void fireAnswerEvent(ActionEvent event, boolean answer, Button otherButton) {
        try {
            controller.sendAnswerObject(answer);
            ((Button) event.getSource()).setDisable(true);
            otherButton.setDisable(true);
            historyMessages.add(new HistoryObject(answer, ObjectMessageType.ANSWER));
        } catch (IOException e) {
            showCrashErrorMessage("Errore di connessione");
        }
    }

    public HistoryObject getMostRecentDiceMessage() throws IOException {
        for (int i = historyMessages.size() - 1; i >= 0; i--) {
            if (historyMessages.get(i).getObjectMessageType() == ObjectMessageType.DICE) {
                return historyMessages.get(i);
            }
        }
        throw new IOException();
    }

    public HistoryObject getMostRecentMessage() {
        if (historyMessages.isEmpty())
            return new HistoryObject(null, ObjectMessageType.NONE);
        return historyMessages.get(historyMessages.size() - 1);
    }
}
