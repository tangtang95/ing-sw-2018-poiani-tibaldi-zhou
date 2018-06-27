package org.poianitibaldizhou.sagrada.graphics.view.listener;

import com.jfoenix.controls.JFXButton;
import javafx.animation.*;
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
import org.poianitibaldizhou.sagrada.graphics.controller.GameGraphicsController;
import org.poianitibaldizhou.sagrada.graphics.utils.GraphicsUtils;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;
import org.poianitibaldizhou.sagrada.graphics.view.MessageType;
import org.poianitibaldizhou.sagrada.graphics.view.component.DiceView;
import org.poianitibaldizhou.sagrada.graphics.view.component.SchemaCardView;
import org.poianitibaldizhou.sagrada.graphics.view.component.TimeoutView;
import org.poianitibaldizhou.sagrada.graphics.view.component.ToolCardView;
import org.poianitibaldizhou.sagrada.graphics.view.HistoryObject;
import org.poianitibaldizhou.sagrada.graphics.view.ObjectMessageType;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IStateObserver;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StateListener extends AbstractView implements IStateObserver {

    private final transient SequentialTransition sequentialTransition;
    private final transient TimeoutView timeoutView;

    private transient HBox helperBox;
    private transient HBox topBarBox;

    private transient Map<UserWrapper, Integer> victoryPoints;

    private static final double DURATION_IN_MILLIS = 1500;

    private static final double SCHEMA_CARD_SHOW_SIZE = 1;
    private static final double TOOL_CARD_SHOW_SIZE = 1;
    private static final double DICE_SHOW_SIZE = 0.6;

    private static final String NEGATIVE_BUTTON = "negative-button";
    private static final String POSITIVE_BUTTON = "positive-button";

    /**
     * Constructor.
     * Create a State Listener that show messages to the client every time a notify is called
     *
     * @param controller the game controller of the GUI
     * @param corePane the core view of the game
     * @param notifyPane the view of the game to show the image on a greater size
     * @throws RemoteException network error
     */
    public StateListener(GameGraphicsController controller, Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
        sequentialTransition = new SequentialTransition();
        timeoutView = new TimeoutView();
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
    public void onSetupGame() throws IOException {
        Platform.runLater(() -> {
            timeoutView.stopTimeout();
            clearNotifyPane(false);
            deactivateNotifyPane();
        });

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetupPlayer() throws IOException {
        Platform.runLater(() -> {
            clearNotifyPane(false);
            activateNotifyPane();
            Label stateMessageLabel = createLabelMessage("Setup Player");
            notifyPane.getChildren().add(stateMessageLabel);
            if (controller.getGameViewStrategy().hasTimeout()) {
                timeoutView.startTimeout(getTimeout());
                timeoutView.setTranslateX(PADDING);
                timeoutView.setTranslateY(PADDING);
                notifyPane.getChildren().add(timeoutView);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRoundStart(String message) throws IOException {
         /* NOT NEEDED */
    }

    /**
     * {@inheritDoc}
     */
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
                    getOrdinalNumberStrings().get(round), turn, turnUser.getUsername().toUpperCase()));
            topBarBox.setOpacity(0.7);
            topBarBox.setAlignment(Pos.CENTER);
            JFXButton button = GraphicsUtils.getButton("Esci", NEGATIVE_BUTTON);
            button.setOnAction(this::onQuitGameButtonAction);
            topBarBox.getChildren().add(button);

            if (controller.getGameViewStrategy().hasTimeout()) {
                timeoutView.stopTimeout();
                corePane.getChildren().remove(timeoutView);
                timeoutView.startTimeout(getTimeout());
                timeoutView.translateXProperty().bind(getWidth().subtract(timeoutView.widthProperty()).subtract(PADDING));
                timeoutView.translateYProperty().bind(getPivotY(getCenterY(), timeoutView.heightProperty(), 0.5));
                corePane.getChildren().add(timeoutView);
            }

            if (turnUser.getUsername().equals(controller.getUsername())) {
                //SHOW COMMANDS
                helperBox = showHelperText(corePane, ClientMessage.CHOOSE_ACTION_ITA);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.SOMETIMES);

                JFXButton placeDiceButton = GraphicsUtils.getButton("Piazza un dado", POSITIVE_BUTTON);
                JFXButton useCardButton = GraphicsUtils.getButton("Usa una Carta Utensile", POSITIVE_BUTTON);
                JFXButton endTurnButton = GraphicsUtils.getButton("Termina il tuo turno", NEGATIVE_BUTTON);

                placeDiceButton.setOnAction(this::onPlaceDiceButtonPressed);
                useCardButton.setOnAction(this::onUseCardButtonPressed);
                endTurnButton.setOnAction(event -> {
                    placeDiceButton.setDisable(true);
                    useCardButton.setDisable(true);
                    endTurnButton.setDisable(true);
                    onEndTurnButtonPressed(event);
                });

                helperBox.getChildren().addAll(spacer, placeDiceButton, useCardButton, endTurnButton);
            } else {
                helperBox = showHelperText(corePane, String.format(ClientMessage.PLAYER_TURN,
                        getOrdinalNumberStrings().get(turn - 1), turnUser.getUsername()));
            }
            helperBox.setOpacity(0.7);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRoundEnd(String message) throws IOException {
        /* NOT NEEDED */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEndGame(String roundUser) throws IOException {
        /* NOT NEEDED */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSkipTurnState(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        UserWrapper turnUser = parser.getTurnUserWrapper(message);
        Platform.runLater(() -> {
            Label stateMessageLabel = createLabelMessage(String.format(ClientMessage.PLAYER_SKIP_TURN,
                    turnUser.getUsername()));
            corePane.getChildren().add(stateMessageLabel);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPlaceDiceState(String message) throws IOException {
        /*NOT IMPORTANT FOR THE GUI*/
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUseCardState(String message) throws IOException {
        /*NOT IMPORTANT FOR THE GUI*/
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void onVictoryPointsCalculated(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        victoryPoints = parser.getVictoryPoint(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResultGame(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        UserWrapper winner = parser.getUserWrapper(message);
        Platform.runLater(() -> {
            if (victoryPoints == null)
                victoryPoints = new HashMap<>();
            controller.pushScorePlayerScene(winner, victoryPoints);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onGameTerminationBeforeStarting() throws IOException {
        // TODO :)
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StateListener;
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }

    private void onPlaceDiceButtonPressed(ActionEvent actionEvent) {
        clearNotifyPane(false);
        activateNotifyPane();

        HBox helperPane = showHelperText(notifyPane, ClientMessage.PLACE_DICE_FROM_DRAFT_POOL);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.SOMETIMES);

        JFXButton cancelButton = GraphicsUtils.getButton("Annulla", NEGATIVE_BUTTON);

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
                schemaCardView.drawShadow(x, y);
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
                        + ((draftPool.size() - 1) * PADDING * 2);
                double startDiceX = getCenterX().get() - totalWidth / 2;
                double x = startDiceX + (i * PADDING * 2) + (diceView.getImageWidth() * i);

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
            Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
            showCrashErrorMessage(ClientMessage.CONNECTION_ERROR);
        }
    }

    private long getTimeout() {
        long requestTime = System.currentTimeMillis();
        long millisTimeout = 0;
        try {
            millisTimeout = controller.getMillisTimeout();
        } catch (IOException e) {
            showCrashErrorMessage(ClientMessage.CONNECTION_ERROR);
            Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
        }
        long endRequestTime = System.currentTimeMillis();
        return millisTimeout - (endRequestTime - requestTime);
    }

    private void onSchemaCardDragDropped(DragEvent event, SchemaCardView schemaCardView) {
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
            Logger.getAnonymousLogger().log(Level.SEVERE, ClientMessage.PARSE_EXCEPTION);
        } catch (IOException e) {
            showCrashErrorMessage(ClientMessage.CONNECTION_ERROR);
        }
    }

    private void onUseCardButtonPressed(ActionEvent actionEvent) {
        clearNotifyPane(false);
        activateNotifyPane();

        HBox helperPane = showHelperText(notifyPane, ClientMessage.CHOOSE_TOOL_CARD);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.SOMETIMES);

        JFXButton continueButton = GraphicsUtils.getButton("Continua", POSITIVE_BUTTON);
        JFXButton cancelButton = GraphicsUtils.getButton("Annulla", NEGATIVE_BUTTON);


        cancelButton.setOnAction(this::onCancelButtonPressed);

        helperPane.getChildren().addAll(spacer, continueButton, cancelButton);

        try {
            List<ToolCardWrapper> toolCardWrappers = controller.getToolCards();
            if (toolCardWrappers.isEmpty()) {
                continueButton.setDisable(true);
                return;
            }
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
            Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
        }
    }

    private void onCancelButtonPressed(ActionEvent actionEvent) {
        clearNotifyPane(false);
        deactivateNotifyPane();
    }

    private void onUseCardContinueButtonPressed(ActionEvent actionEvent, ToggleGroup toggleGroup) {
        if (toggleGroup.getSelectedToggle() == null) {
            showMessage(notifyPane, ClientMessage.CHOOSE_TOOL_CARD, MessageType.ERROR);
            return;
        }
        ToolCardView toolCardView = (ToolCardView) toggleGroup.getSelectedToggle().getUserData();
        ToolCardWrapper toolCardWrapper = toolCardView.getToolCardWrapper();
        try {
            ToolCardExecutorListener toolCardExecutorObserver = new ToolCardExecutorListener(controller,
                    corePane, notifyPane);
            toolCardExecutorObserver.addHistoryMessage(new HistoryObject(toolCardWrapper, ObjectMessageType.TOOL_CARD));
            controller.useToolCard(toolCardWrapper, toolCardExecutorObserver);
            toggleGroup.getToggles().forEach(toggle -> ((RadioButton) toggle).setDisable(true));
            ((Button) actionEvent.getSource()).setDisable(true);
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
            showMessage(notifyPane, ClientMessage.CONNECTION_ERROR, MessageType.ERROR);
        }
    }

    private void onEndTurnButtonPressed(ActionEvent actionEvent) {
        try {
            controller.endTurn();
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
            showCrashErrorMessage(ClientMessage.CONNECTION_ERROR);
        }
    }

    private void onQuitGameButtonAction(ActionEvent actionEvent) {
        try {
            controller.quitGame();
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
            showCrashErrorMessage(ClientMessage.CONNECTION_ERROR);
        }
    }


    private Label createLabelMessage(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("state-message");
        label.setTextFill(Color.SNOW);
        label.translateXProperty().bind(getPivotX(getCenterX(), label.widthProperty(), 0.5));
        label.translateYProperty().bind(getCenterY().subtract(getCenterY().divide(1.3)));
        label.setOpacity(1);

        FadeTransition transition = new FadeTransition(Duration.millis(DURATION_IN_MILLIS), label);
        transition.setFromValue(0.6);
        transition.setToValue(1);
        transition.setInterpolator(Interpolator.LINEAR);
        transition.setCycleCount(4);
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

}
