package org.poianitibaldizhou.sagrada.graphics.view.listener;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.poianitibaldizhou.sagrada.graphics.controller.GameGraphicsController;
import org.poianitibaldizhou.sagrada.graphics.utils.GraphicsUtils;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;
import org.poianitibaldizhou.sagrada.graphics.view.MessageType;
import org.poianitibaldizhou.sagrada.graphics.view.component.ColorView;
import org.poianitibaldizhou.sagrada.graphics.view.component.DiceView;
import org.poianitibaldizhou.sagrada.graphics.view.component.RoundTrackView;
import org.poianitibaldizhou.sagrada.graphics.view.component.SchemaCardView;
import org.poianitibaldizhou.sagrada.graphics.view.HistoryObject;
import org.poianitibaldizhou.sagrada.graphics.view.ObjectMessageType;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * OVERVIEW: Listen to the modification of the execution of a tool card
 */
public class ToolCardExecutorListener extends AbstractView implements IToolCardExecutorObserver {

    private final transient List<HistoryObject> historyMessages;

    private static final double ROUND_TRACK_SHOW_SCALE = 1;
    private static final double DICE_SHOW_SCALE = 1;
    private static final double TILE_SHOW_SCALE = 1;
    private static final double SCHEMA_CARD_SHOW_SCALE = 1;
    private static final double DICE_SCHEMA_SHOW_SCALE = 0.6;

    private static final String NEGATIVE_BUTTON = "negative-button";
    private static final String POSITIVE_BUTTON = "positive-button";
    private static final String CSS_CLASS = "on-notify-pane-card";
    private static final String BUTTON_TEXT = "Continua";

    /**
     * Constructor.
     * Create a tool card executor listener that handles every notify related to the execution of the toolCard
     *
     * @param controller the game controller of the GUI
     * @param corePane the core view of the game
     * @param notifyPane the view of the game to show the image on a greater size
     * @throws RemoteException network error
     */
    public ToolCardExecutorListener(GameGraphicsController controller, Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
        historyMessages = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateView() {
        /* NOTHING TO UPDATE */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedDice(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        List<DiceWrapper> diceWrapperList = parser.getDiceList(message);
        Platform.runLater(() -> {
            clearNotifyPane(false);
            activateNotifyPane();
            drawHistoryButton();
            HBox helperBox = showHelperText(notifyPane, ClientMessage.CHOOSE_DICE_FROM_LIST);
            List<Pane> diceViewList = new ArrayList<>();
            diceWrapperList.forEach(diceWrapper -> {
                DiceView diceView = new DiceView(diceWrapper, DICE_SHOW_SCALE);
                diceViewList.add(diceView);
            });
            drawCenteredPanes(notifyPane, diceViewList, CSS_CLASS);
            ToggleGroup toggleGroup = new ToggleGroup();
            drawRadioButtons(toggleGroup, diceViewList);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.SOMETIMES);

            JFXButton continueButton = GraphicsUtils.getButton(BUTTON_TEXT, POSITIVE_BUTTON);
            continueButton.setOnAction(event -> fireDiceEvent(event, toggleGroup));

            helperBox.getChildren().addAll(spacer, continueButton);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedNewValue(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        DiceWrapper diceWrapper = parser.getDice(message);
        Platform.runLater(() -> {
            clearNotifyPane(false);
            activateNotifyPane();
            drawHistoryButton();
            HBox helperBox = showHelperText(notifyPane, ClientMessage.CHOOSE_DICE_VALUE);
            List<Pane> diceViewList = new ArrayList<>();
            for (int i = DiceWrapper.MIN_VALUE; i <= DiceWrapper.MAX_VALUE; i++) {
                DiceView diceView = new DiceView(new DiceWrapper(diceWrapper.getColor(), i), DICE_SHOW_SCALE);
                diceViewList.add(diceView);
            }
            drawCenteredPanes(notifyPane, diceViewList, CSS_CLASS);
            ToggleGroup toggleGroup = new ToggleGroup();
            drawRadioButtons(toggleGroup, diceViewList);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.SOMETIMES);

            JFXButton continueButton = GraphicsUtils.getButton(BUTTON_TEXT, POSITIVE_BUTTON);
            continueButton.setOnAction(event -> fireValueEvent(event, toggleGroup));

            helperBox.getChildren().addAll(spacer, continueButton);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedColor(String colors) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        List<ColorWrapper> colorWrapperList = parser.getColorList(colors);
        Platform.runLater(() -> {
            clearNotifyPane(false);
            activateNotifyPane();
            drawHistoryButton();
            HBox helperBox = showHelperText(notifyPane, ClientMessage.CHOOSE_COLOR_FOR_CONTINUE);
            List<Pane> colorViewList = new ArrayList<>();
            colorWrapperList.forEach(colorWrapper -> {
                ColorView colorView = new ColorView(colorWrapper, TILE_SHOW_SCALE);
                colorViewList.add(colorView);
            });
            drawCenteredPanes(notifyPane, colorViewList, CSS_CLASS);
            ToggleGroup toggleGroup = new ToggleGroup();
            drawRadioButtons(toggleGroup, colorViewList);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.SOMETIMES);

            JFXButton continueButton = GraphicsUtils.getButton(BUTTON_TEXT, POSITIVE_BUTTON);
            continueButton.setOnAction(event -> fireColorEvent(event, toggleGroup));

            helperBox.getChildren().addAll(spacer, continueButton);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedNewDeltaForDice(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        DiceWrapper diceWrapper = parser.getDice(message);
        int deltaValue = parser.getValue(message);
        Platform.runLater(() -> {
            clearNotifyPane(false);
            activateNotifyPane();
            drawHistoryButton();
            HBox helperBox = showHelperText(notifyPane, ClientMessage.CHOOSE_DICE_VALUE);
            List<Pane> diceViewList = new ArrayList<>();
            if (diceWrapper.getNumber() + deltaValue <= DiceWrapper.MAX_VALUE)
                diceViewList.add(new DiceView(new DiceWrapper(diceWrapper.getColor(),
                        diceWrapper.getNumber() + deltaValue), DICE_SHOW_SCALE));
            if (diceWrapper.getNumber() - deltaValue >= DiceWrapper.MIN_VALUE)
                diceViewList.add(new DiceView(new DiceWrapper(diceWrapper.getColor(),
                        diceWrapper.getNumber() - deltaValue), DICE_SHOW_SCALE));
            drawCenteredPanes(notifyPane, diceViewList, CSS_CLASS);
            ToggleGroup toggleGroup = new ToggleGroup();
            drawRadioButtons(toggleGroup, diceViewList);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.SOMETIMES);

            JFXButton continueButton = GraphicsUtils.getButton(BUTTON_TEXT, POSITIVE_BUTTON);
            continueButton.setOnAction(event -> fireValueEvent(event, toggleGroup));

            helperBox.getChildren().addAll(spacer, continueButton);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedDiceFromRoundTrack(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        RoundTrackWrapper roundTrackWrapper = parser.getRoundTrack(message);

        Platform.runLater(() -> {
            clearNotifyPane(false);
            activateNotifyPane();
            drawHistoryButton();
            HBox helperBox = showHelperText(notifyPane, ClientMessage.CHOOSE_DICE_FROM_ROUND_TRACK);
            RoundTrackView copyRoundTrackView = new RoundTrackView(roundTrackWrapper, ROUND_TRACK_SHOW_SCALE);

            drawPane(notifyPane, copyRoundTrackView, CSS_CLASS,
                    getPivotX(getCenterX(), copyRoundTrackView.widthProperty(), 0.5),
                    getPivotY(getCenterY(), copyRoundTrackView.heightProperty(), 0));

            List<Pane> diceViews = new ArrayList<>();
            Label roundLabel = new Label();
            roundLabel.setFont(Font.font(ROUND_TRACK_SHOW_SCALE * 60));
            roundLabel.setTextFill(Color.WHITE);

            ToggleGroup toggleGroup = new ToggleGroup();

            for (int i = 0; i < RoundTrackWrapper.NUMBER_OF_TRACK; i++) {
                int round = i;
                copyRoundTrackView.setDicePressedEvent(round, (event1 -> {
                    toggleGroup.getToggles().clear();
                    toggleGroup.setUserData(round);
                    notifyPane.getChildren().removeAll(diceViews);
                    notifyPane.getChildren().removeAll(roundLabel);
                    diceViews.clear();

                    List<DiceWrapper> diceList = roundTrackWrapper.getDicesForRound(round);
                    diceList.forEach(diceWrapper -> diceViews.add(new DiceView(diceWrapper, DICE_SCHEMA_SHOW_SCALE)));
                    DoubleBinding y = copyRoundTrackView.translateYProperty()
                            .add(copyRoundTrackView.heightProperty()).add(PADDING * 3);
                    roundLabel.setText(String.format("Round %s", String.valueOf(round + 1)));
                    roundLabel.translateXProperty().bind(getPivotX(getCenterX(), roundLabel.widthProperty(), 0.5));
                    roundLabel.translateYProperty().bind(y);
                    notifyPane.getChildren().add(roundLabel);

                    GraphicsUtils.drawCenteredPanes(notifyPane, diceViews, CSS_CLASS,
                            getCenterX(), roundLabel.translateYProperty().add(roundLabel.heightProperty().add(PADDING * 3)));

                    drawRadioButtons(toggleGroup, diceViews);

                    event1.consume();
                }));
            }

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.SOMETIMES);

            JFXButton continueButton = GraphicsUtils.getButton(BUTTON_TEXT, POSITIVE_BUTTON);
            continueButton.setOnAction(event -> {
                if (toggleGroup.getSelectedToggle() != null)
                    fireValueEvent(event, (Integer) toggleGroup.getUserData());
                fireDiceEvent(event, toggleGroup);
            });

            helperBox.getChildren().addAll(spacer, continueButton);
        });

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedPositionForRemoving(String message) throws IOException {
        ClientGetMessage protocolParser = new ClientGetMessage();
        SchemaCardWrapper schemaCardWrapper = protocolParser.getSchemaCard(message);
        Platform.runLater(() -> {
            clearNotifyPane(false);
            activateNotifyPane();
            drawHistoryButton();
            DropShadow dropShadow = new DropShadow(4, 4, 4, Color.BLACK);
            SchemaCardView schemaCardView = new SchemaCardView(schemaCardWrapper, SCHEMA_CARD_SHOW_SCALE);
            schemaCardView.translateXProperty().bind(getPivotX(getCenterX(), schemaCardView.widthProperty(), 0.5));
            schemaCardView.translateYProperty().bind(getPivotY(getCenterY(), schemaCardView.widthProperty(), 0.33));
            schemaCardView.setEffect(dropShadow);
            notifyPane.getChildren().add(schemaCardView);
            String helperMessage = ClientMessage.REMOVE_DICE;

            schemaCardView.setOnMousePressed(event -> {
                double x = event.getX();
                double y = event.getY();
                schemaCardView.removeShadow();
                schemaCardView.drawShadow(x, y);
                PositionWrapper positionWrapper = schemaCardView.getTilePosition(x, y);
                schemaCardView.setUserData(positionWrapper);
                event.consume();
            });

            HBox helperBox = showHelperText(notifyPane, helperMessage);
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.SOMETIMES);

            JFXButton continueButton = GraphicsUtils.getButton(BUTTON_TEXT, POSITIVE_BUTTON);
            continueButton.setOnAction(event -> firePositionEvent(event, schemaCardView));

            helperBox.getChildren().addAll(spacer, continueButton);

        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedPositionForPlacement(String message) throws IOException {
        ClientGetMessage protocolParser = new ClientGetMessage();
        SchemaCardWrapper schemaCardWrapper = protocolParser.getSchemaCard(message);
        DiceWrapper diceWrapper = protocolParser.getDice(message);
        Platform.runLater(() -> {
            clearNotifyPane(false);
            activateNotifyPane();
            drawHistoryButton();
            DropShadow dropShadow = new DropShadow(4, 4, 4, Color.BLACK);
            SchemaCardView schemaCardView = new SchemaCardView(schemaCardWrapper, SCHEMA_CARD_SHOW_SCALE);
            schemaCardView.translateXProperty().bind(getPivotX(getCenterX(), schemaCardView.widthProperty(), 0.5));
            schemaCardView.translateYProperty().bind(getPivotY(getCenterY(), schemaCardView.widthProperty(), 0.33));
            schemaCardView.setEffect(dropShadow);
            notifyPane.getChildren().add(schemaCardView);
            String helperMessage = ClientMessage.TOOL_CARD_PLACE_DICE;
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
                if (schemaCardView.getUserData() != null) {
                    PositionWrapper oldPositionWrapper = (PositionWrapper) schemaCardView.getUserData();
                    try {
                        schemaCardView.removeDice(diceWrapper, oldPositionWrapper);
                    } catch (IOException e) {
                        Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
                        showCrashErrorMessage(ClientMessage.SYNCHRONIZED_ERROR);
                        return;
                    }
                }

                PositionWrapper positionWrapper = schemaCardView.getTilePosition(x, y);
                if (schemaCardView.getDiceByPosition(positionWrapper) != null) {
                    showMessage(notifyPane, ClientMessage.PLACE_DICE_ERROR, MessageType.ERROR);
                    return;
                }
                schemaCardView.setUserData(positionWrapper);
                JSONParser parser = new JSONParser();
                try {
                    JSONObject diceObject = (JSONObject) ((JSONObject) parser.parse(dragboard.getString())).get("body");
                    DiceWrapper dice = DiceWrapper.toObject(diceObject);
                    schemaCardView.drawDice(dice, positionWrapper);
                    event.setDropCompleted(true);
                    event.consume();
                } catch (ParseException e) {
                    Logger.getAnonymousLogger().log(Level.SEVERE, ClientMessage.PARSE_EXCEPTION);
                }
            });

            diceView.translateXProperty().bind(getPivotX(getCenterX(), diceView.widthProperty(), 0.5));
            diceView.translateYProperty().bind(schemaCardView.translateYProperty()
                    .add(schemaCardView.heightProperty()).add(PADDING * 2));
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

            HBox helperBox = showHelperText(notifyPane, helperMessage);
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.SOMETIMES);

            JFXButton continueButton = GraphicsUtils.getButton(BUTTON_TEXT, POSITIVE_BUTTON);
            continueButton.setOnAction(event -> firePositionEvent(event, schemaCardView));

            helperBox.getChildren().addAll(spacer, continueButton);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedDicePositionOfCertainColor(String message) throws IOException {
        ClientGetMessage protocolParser = new ClientGetMessage();
        ColorWrapper colorWrapper = protocolParser.getColor(message);
        SchemaCardWrapper schemaCardWrapper = protocolParser.getSchemaCard(message);
        Platform.runLater(() -> {
            clearNotifyPane(false);
            activateNotifyPane();
            drawHistoryButton();
            SchemaCardView schemaCardView = new SchemaCardView(schemaCardWrapper, SCHEMA_CARD_SHOW_SCALE);
            drawCenteredPane(notifyPane, schemaCardView, "");
            String helperMessage = String.format(ClientMessage.REMOVE_DICE_BY_COLOR, colorWrapper.name().toLowerCase());

            schemaCardView.setOnMousePressed(event -> {
                double x = event.getX();
                double y = event.getY();
                schemaCardView.removeShadow();
                schemaCardView.drawShadow(x, y);
                PositionWrapper positionWrapper = schemaCardView.getTilePosition(x, y);
                if (schemaCardView.getDiceByPosition(positionWrapper) == null)
                    showMessage(notifyPane, ClientMessage.NO_DICE_SELECTED, MessageType.ERROR);
                else if (Objects.requireNonNull(schemaCardView.getDiceByPosition(positionWrapper)).getColor() != colorWrapper)
                    showMessage(notifyPane, ClientMessage.COLOR_DICE_IS_WRONG, MessageType.ERROR);
                else
                    schemaCardView.setUserData(positionWrapper);
                event.consume();
            });

            HBox helperBox = showHelperText(notifyPane, helperMessage);
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.SOMETIMES);

            JFXButton continueButton = GraphicsUtils.getButton(BUTTON_TEXT, POSITIVE_BUTTON);
            continueButton.setOnAction(event -> firePositionEvent(event, schemaCardView));

            helperBox.getChildren().addAll(spacer, continueButton);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyRepeatAction() throws IOException {
        Platform.runLater(() -> {
            showMessage(notifyPane, ClientMessage.REPEAT_ACTION, MessageType.ERROR);
            historyMessages.remove(historyMessages.size() - 1);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyCommandInterrupted(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        String error = parser.getCommandFlow(message);
        Platform.runLater(() -> {
            clearNotifyPane(false);
            deactivateNotifyPane();
            showMessage(corePane,  ClientMessage.TOOL_CARD_INTERRUPT_EXCEPTION + error,
                    MessageType.ERROR);
            try {
                controller.updateAllViews();
            } catch (IOException e) {
                showCrashErrorMessage(ClientMessage.CONNECTION_ERROR);
                Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedContinueAnswer() throws IOException {
        Platform.runLater(() -> {
            clearNotifyPane(false);
            activateNotifyPane();
            drawHistoryButton();
            HBox helperBox = showHelperText(notifyPane, ClientMessage.WOULD_YOU_CONTINUE);
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.SOMETIMES);

            JFXButton continueButton = GraphicsUtils.getButton(BUTTON_TEXT, POSITIVE_BUTTON);
            JFXButton cancelButton = GraphicsUtils.getButton("Annulla", NEGATIVE_BUTTON);
            continueButton.setOnAction(event -> fireAnswerEvent(event, true, cancelButton));
            cancelButton.setOnAction(event -> fireAnswerEvent(event, false, continueButton));

            helperBox.getChildren().addAll(spacer, continueButton, cancelButton);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyDiceReroll(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        DiceWrapper diceWrapper = parser.getDice(message);
        Platform.runLater(() -> historyMessages.add(new HistoryObject(diceWrapper, ObjectMessageType.DICE)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyExecutionEnded() throws IOException {
        Platform.runLater(() -> {
            clearNotifyPane(false);
            deactivateNotifyPane();
            showMessage(corePane, ClientMessage.TOOL_CARD_EXECUTION_SUCCESSFUL, MessageType.INFO);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyWaitTurnEnd() throws IOException {
        Platform.runLater(() -> {
            clearNotifyPane(false);
            deactivateNotifyPane();
            showMessage(corePane, ClientMessage.TOOL_CARD_END_TURN_ACTIVATION, MessageType.INFO);
        });

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyDicePouredOver(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        DiceWrapper diceWrapper = parser.getDice(message);
        Platform.runLater(() -> historyMessages.add(new HistoryObject(diceWrapper, ObjectMessageType.DICE)));
    }

    /**
     * Add a new history message inside the list of history
     * @param message the history message to add
     */
    public void addHistoryMessage(HistoryObject message) {
        historyMessages.add(message);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ToolCardExecutorListener;
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }

    /**
     * Draw the history button regarding the tool card execution
     */
    private void drawHistoryButton(){
        JFXButton button = GraphicsUtils.getButton("Cronologia", NEGATIVE_BUTTON);
        button.setTranslateX(PADDING);
        button.setTranslateY(PADDING);
        button.setOnAction(event -> {
            JFXDialogLayout layout = new JFXDialogLayout();
            VBox historyBox = new VBox(10);
            showHistoryMessages(historyBox);
            layout.getBody().add(historyBox);
            JFXDialog dialog = new JFXDialog((StackPane) corePane.getParent(), layout, JFXDialog.DialogTransition.CENTER);
            dialog.show();
        });
        notifyPane.getChildren().add(button);
    }

    /**
     * Show the history messages regarding the tool card execution
     *
     * @param historyBox vertical box container where the messages will be shawn
     */
    private void showHistoryMessages(VBox historyBox) {
        historyMessages.forEach(historyObject -> {
            Label messageLabel = new Label(historyObject.getMessage());
            historyBox.getChildren().add(messageLabel);
        });
    }

    /**
     * Sends a dice to the controller
     *
     * @param actionEvent action event regarding the continue button
     * @param toggleGroup toggle group that contains the dice that will be sent
     */
    private void fireDiceEvent(ActionEvent actionEvent, ToggleGroup toggleGroup) {
        if (toggleGroup.getSelectedToggle() == null) {
            showMessage(notifyPane, ClientMessage.CHOOSE_DICE_ITA, MessageType.ERROR);
            return;
        }
        DiceView diceView = (DiceView) toggleGroup.getSelectedToggle().getUserData();
        DiceWrapper diceWrapper = diceView.getDiceWrapper();
        try {
            historyMessages.add(new HistoryObject(diceView.getDiceWrapper(), ObjectMessageType.DICE));
            controller.sendDiceObject(diceWrapper);
            toggleGroup.getToggles().forEach((toggle -> ((RadioButton) toggle).setDisable(true)));
            ((Button) actionEvent.getSource()).setDisable(true);
        } catch (IOException e) {
            historyMessages.remove(historyMessages.size() - 1);
            showMessage(notifyPane, ClientMessage.CONNECTION_ERROR, MessageType.ERROR);
        }
    }

    /**
     * Sends a color to the controller
     *
     * @param actionEvent action event regarding the continue button
     * @param toggleGroup toggle group that contains the color that will be sent
     */
    private void fireColorEvent(ActionEvent actionEvent, ToggleGroup toggleGroup) {
        if (toggleGroup.getSelectedToggle() == null) {
            showMessage(notifyPane, ClientMessage.CHOOSE_COLOR_ITA, MessageType.ERROR);
            return;
        }
        ColorView colorView = (ColorView) toggleGroup.getSelectedToggle().getUserData();
        ColorWrapper colorWrapper = colorView.getColorWrapper();
        try {
            historyMessages.add(new HistoryObject(colorWrapper, ObjectMessageType.COLOR));
            controller.sendColorObject(colorWrapper);
            toggleGroup.getToggles().forEach((toggle -> ((RadioButton) toggle).setDisable(true)));
            ((Button) actionEvent.getSource()).setDisable(true);
        } catch (IOException e) {
            historyMessages.remove(historyMessages.size() - 1);
            showMessage(notifyPane, ClientMessage.CONNECTION_ERROR, MessageType.ERROR);
        }
    }

    /**
     * Sends a position to the controller
     *
     * @param actionEvent action event regarding the continue button
     * @param schemaCardView schema card view that will contain the position that will sent
     */
    private void firePositionEvent(ActionEvent actionEvent, SchemaCardView schemaCardView) {
        try {
            if (schemaCardView.getUserData() == null) {
                showMessage(notifyPane, ClientMessage.CHOOSE_POSITION_ITA, MessageType.ERROR);
                return;
            }
            PositionWrapper positionWrapper = (PositionWrapper) schemaCardView.getUserData();
            historyMessages.add(new HistoryObject(positionWrapper, ObjectMessageType.POSITION));
            controller.sendPositionObject(positionWrapper);
            ((Button) actionEvent.getSource()).setDisable(true);
        } catch (IOException e) {
            historyMessages.remove(historyMessages.size() - 1);
            showMessage(notifyPane, ClientMessage.CONNECTION_ERROR, MessageType.ERROR);
        }
    }

    /**
     * Sends an answer to the controller
     *
     * @param event action event regarding the continue button
     * @param answer answer that will be sent
     * @param otherButton the button that was not pressed
     */
    private void fireAnswerEvent(ActionEvent event, boolean answer, Button otherButton) {
        try {
            historyMessages.add(new HistoryObject(answer, ObjectMessageType.ANSWER));
            controller.sendAnswerObject(answer);
            ((Button) event.getSource()).setDisable(true);
            otherButton.setDisable(true);
        } catch (IOException e) {
            historyMessages.remove(historyMessages.size() - 1);
            showCrashErrorMessage(ClientMessage.CONNECTION_ERROR);
        }
    }

    /**
     * Sends an integer value to the controller
     *
     * @param event action event regarding the continue button
     * @param toggleGroup toggle group that contains the value that will be sent
     */
    private void fireValueEvent(ActionEvent event, ToggleGroup toggleGroup) {
        if (toggleGroup.getSelectedToggle() == null) {
            showMessage(notifyPane, ClientMessage.CHOOSE_VALUE, MessageType.ERROR);
            return;
        }
        int value = ((DiceView) toggleGroup.getSelectedToggle().getUserData()).getDiceWrapper().getNumber();
        fireValueEvent(event, value);
    }

    /**
     * Sends an integer value to the controller
     *
     * @param event action event regarding the continue button
     * @param value value that will be sent
     */

    private void fireValueEvent(ActionEvent event, int value) {
        try {
            historyMessages.add(new HistoryObject(value, ObjectMessageType.VALUE));
            controller.sendValueObject(value);
            ((Button) event.getSource()).setDisable(true);
        } catch (IOException e) {
            historyMessages.remove(historyMessages.size() - 1);
            showCrashErrorMessage(ClientMessage.CONNECTION_ERROR);
        }
    }
}
