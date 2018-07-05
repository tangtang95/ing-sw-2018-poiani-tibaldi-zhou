package org.poianitibaldizhou.sagrada.graphics.controller;

import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.poianitibaldizhou.sagrada.exception.NetworkException;
import org.poianitibaldizhou.sagrada.graphics.model.ConnectionModel;
import org.poianitibaldizhou.sagrada.graphics.utils.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.ConnectionType;
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * OVERVIEW:
 */
public class StartMenuGraphicsController extends GraphicsController implements Initializable {

    @FXML
    public Parent rootPane;

    @FXML
    public StackPane rightPane;

    @FXML
    public Label connectionType;

    @FXML
    public VBox menuButtonPane;

    @FXML
    public VBox connectionPane;

    @FXML
    public VBox reconnectPane;

    @FXML
    public VBox multiPlayerPane;

    @FXML
    public VBox singlePlayerPane;

    //ReconnectPane
    @FXML
    public JFXTextField reconnectUsernameTextField;

    //MultiPlayerPane
    @FXML
    public JFXTextField usernameTextField;

    //SinglePlayerPane
    @FXML
    public JFXTextField singlePlayerUsernameTextField;
    @FXML
    public JFXRadioButton radioButtonVeryEasy;
    @FXML
    public JFXRadioButton radioButtonEasy;
    @FXML
    public JFXRadioButton radioButtonMedium;
    @FXML
    public JFXRadioButton radioButtonHard;
    @FXML
    public JFXRadioButton radioButtonHell;

    private ToggleGroup difficultyToggleGroup;

    //ChangeConnectionPane
    @FXML
    public JFXTextField ipTextField;
    @FXML
    public JFXTextField portTextField;
    @FXML
    public JFXRadioButton radioButtonRMI;
    @FXML
    public JFXRadioButton radioButtonSocket;

    private ToggleGroup connectionToggleGroup;

    private ConnectionModel connectionModel;

    private ConnectionManager connectionManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeConnectionView();
        initializeSinglePlayerView();
        initializeMultiPlayerView();
        initializeReconnectView();
        connectionManager = new ConnectionManager(connectionModel.getIpAddress(),
                connectionModel.getPort(), ConnectionType.valueOf(connectionModel.getConnectionType().toUpperCase()));
    }


    /**
     * Init the view for the reconnection
     */
    private void initializeReconnectView() {
        reconnectUsernameTextField.setValidators(getRequiredFieldValidator(), getUsernameFieldValidator());
        reconnectUsernameTextField.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)){
                onReconnectPlayButton(null);
            }
        });
    }

    /**
     * Init the view for the multi player game
     */
    private void initializeMultiPlayerView() {
        usernameTextField.setValidators(getRequiredFieldValidator(), getUsernameFieldValidator());
        usernameTextField.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)){
                onMultiPlayerPlayButton(null);
            }
        });
    }

    /**
     * Init the view for the changing the connection mode
     */
    private void initializeConnectionView() {
        connectionModel = new ConnectionModel();
        connectionType.textProperty().bind(connectionModel.connectionTypeProperty());
        connectionToggleGroup = new ToggleGroup();
        radioButtonRMI.setToggleGroup(connectionToggleGroup);
        radioButtonSocket.setToggleGroup(connectionToggleGroup);
        radioButtonRMI.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && portTextField.getText().equals(String.valueOf(ConnectionType.SOCKET.getPort()))) {
                portTextField.setText(String.valueOf(ConnectionType.RMI.getPort()));
            }
        });
        radioButtonSocket.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && portTextField.getText().equals(String.valueOf(ConnectionType.RMI.getPort()))) {
                portTextField.setText(String.valueOf(ConnectionType.SOCKET.getPort()));
            }
        });

        NumberValidator numberValidator = new NumberValidator();
        numberValidator.setMessage(ClientMessage.ONLY_NUMBER);
        portTextField.setValidators(numberValidator);
        portTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                portTextField.validate();
            }
        });

        IPAddressValidator ipAddressValidator = new IPAddressValidator();
        ipAddressValidator.setMessage(ClientMessage.IP_ERROR);
        ipTextField.setValidators(ipAddressValidator);
        ipTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue)
                ipTextField.validate();
        });

    }

    /**
     * Init the view for the single player
     */
    private void initializeSinglePlayerView() {
        singlePlayerUsernameTextField.setValidators(getRequiredFieldValidator(), getUsernameFieldValidator());
        singlePlayerUsernameTextField.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)){
                onSinglePlayerPlayButton(null);
            }
        });

        difficultyToggleGroup = new ToggleGroup();
        radioButtonVeryEasy.setUserData(Difficulty.VERY_EASY);
        radioButtonEasy.setUserData(Difficulty.EASY);
        radioButtonMedium.setUserData(Difficulty.MEDIUM);
        radioButtonHard.setUserData(Difficulty.HARD);
        radioButtonHell.setUserData(Difficulty.HELL);
        radioButtonVeryEasy.setToggleGroup(difficultyToggleGroup);
        radioButtonEasy.setToggleGroup(difficultyToggleGroup);
        radioButtonMedium.setToggleGroup(difficultyToggleGroup);
        radioButtonHard.setToggleGroup(difficultyToggleGroup);
        radioButtonHell.setToggleGroup(difficultyToggleGroup);

        difficultyToggleGroup.selectToggle(radioButtonMedium);
    }

    /**
     * Init method for StartMenuScene
     */
    public void initStartMenuScene() {
        menuButtonPane.setPrefWidth(sceneManager.getSceneWidth() * 0.4);
        menuButtonPane.setPadding(new Insets(sceneManager.getSceneHeight() / 10, 0, 0, sceneManager.getSceneWidth() / 20));
        rightPane.setPrefWidth(sceneManager.getSceneWidth() * 0.6);
    }


    /**
     * Start the multi player game
     *
     * @param actionEvent not used
     */
    @FXML
    public void startMultiPlayerGame(ActionEvent actionEvent) {
        closeEveryPane();
        playOpenMenuPaneTransition(multiPlayerPane);
        multiPlayerPane.setVisible(true);
        multiPlayerPane.toFront();
    }

    /**
     * Start the single player game
     *
     * @param actionEvent not used
     */
    @FXML
    public void startSinglePlayerGame(ActionEvent actionEvent) {
        closeEveryPane();
        playOpenMenuPaneTransition(singlePlayerPane);
        singlePlayerPane.setVisible(true);
        singlePlayerPane.toFront();
    }

    /**
     * Change the connection
     *
     * @param actionEvent not used
     */
    @FXML
    public void changeConnectionMode(ActionEvent actionEvent) {
        updateConnectionPaneView();
        closeEveryPane();
        playOpenMenuPaneTransition(connectionPane);
        connectionPane.setVisible(true);
        connectionPane.toFront();
    }

    /**
     * Handles the single animation when clicking a button of the main menu
     *
     * @param node object that is animated
     */
    private void playOpenMenuPaneTransition(Node node) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(500), node);
        translateTransition.fromXProperty().bind(rightPane.translateXProperty().subtract(rightPane.widthProperty().divide(3)));
        translateTransition.toXProperty().bind(rightPane.translateXProperty());
        translateTransition.play();

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), node);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    /**
     * Quit Sagrada client
     *
     * @param actionEvent not used
     */
    @FXML
    public void quitGame(ActionEvent actionEvent) {
        playSceneTransition(sceneManager.getCurrentScene(), event -> {
            sceneManager.getPrimaryStage().close();
            System.exit(0);
        });
    }

    /**
     * Shows the reconnect menu
     *
     * @param actionEvent not used
     */
    @FXML
    public void onReconnectButtonAction(ActionEvent actionEvent) {
        closeEveryPane();
        playOpenMenuPaneTransition(reconnectPane);
        reconnectPane.setVisible(true);
        reconnectPane.toFront();
    }

    /**
     * Close the reconnect menu
     *
     * @param actionEvent not used
     */
    @FXML
    public void onReconnectCloseButton(ActionEvent actionEvent) {
        closeEveryPane();
        reconnectUsernameTextField.setText("");
    }

    /**
     * Reconnect to a game
     *
     * @param actionEvent press button event
     */
    @FXML
    public void onReconnectPlayButton(ActionEvent actionEvent) {
        if (reconnectUsernameTextField.validate()) {

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/game.fxml"));

            try {
                Parent root = loader.load();
                GameGraphicsController controller = loader.getController();
                controller.setSceneManager(sceneManager);
                connectionManager.setIpAddress(connectionModel.getIpAddress());
                connectionManager.setPort(connectionModel.getPort());
                connectionManager.setNetworkType(ConnectionType.valueOf(connectionModel.getConnectionType().toUpperCase()));
                connectionManager.activateConnection();
                controller.initReconnectMultiPlayerGame(reconnectUsernameTextField.getText(), connectionManager);
                playSceneTransition(sceneManager.getCurrentScene(), event -> sceneManager.pushScene(root));
            } catch (IOException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
                Logger.getAnonymousLogger().log(Level.SEVERE, ClientMessage.LOAD_FXML_ERROR);
            } catch (NetworkException e) {
                AlertBox.displayBox("Errore di connessione", ClientMessage.CONNECTION_ERROR);
            } finally {
                onReconnectCloseButton(actionEvent);
            }
        }
    }

    /**
     * Close the multi player menu
     *
     * @param actionEvent not used
     */
    @FXML
    public void onMultiPlayerCloseButton(ActionEvent actionEvent) {
        closeEveryPane();
        usernameTextField.setText("");
    }

    /**
     * Join the lobby
     *
     * @param actionEvent play multi player button event
     */
    @FXML
    public void onMultiPlayerPlayButton(ActionEvent actionEvent) {
        if (usernameTextField.validate()) {

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/lobby.fxml"));

            try {
                Parent root = loader.load();
                LobbyGraphicsController controller = loader.getController();
                controller.setSceneManager(sceneManager);
                connectionManager.setIpAddress(connectionModel.getIpAddress());
                connectionManager.setPort(connectionModel.getPort());
                connectionManager.setNetworkType(ConnectionType.valueOf(connectionModel.getConnectionType().toUpperCase()));
                connectionManager.activateConnection();
                controller.initLobbyModel(usernameTextField.getText(), connectionManager);
                playSceneTransition(sceneManager.getCurrentScene(), event -> sceneManager.pushScene(root));
            } catch (IOException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
                Logger.getAnonymousLogger().log(Level.SEVERE, ClientMessage.LOAD_FXML_ERROR);
            } catch (NetworkException e) {
                AlertBox.displayBox("Errore del server", e.getInnerException().getMessage());
            } finally {
                onMultiPlayerCloseButton(actionEvent);
            }
        }
    }

    /**
     * Close single player menu
     *
     * @param actionEvent not used
     */
    @FXML
    public void onSinglePlayerCloseButton(ActionEvent actionEvent) {
        difficultyToggleGroup.selectToggle(radioButtonMedium);
        closeEveryPane();
        singlePlayerUsernameTextField.setText("");
    }

    /**
     * Init single player game
     *
     * @param actionEvent single player play button event
     */
    @FXML
    public void onSinglePlayerPlayButton(ActionEvent actionEvent) {
        if (difficultyToggleGroup.getSelectedToggle() == null)
            return;
        if (singlePlayerUsernameTextField.validate()) {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/game.fxml"));

            try {
                Pane root = loader.load();
                GameGraphicsController controller = loader.getController();
                controller.setSceneManager(sceneManager);
                connectionManager.setIpAddress(connectionModel.getIpAddress());
                connectionManager.setPort(connectionModel.getPort());
                connectionManager.setNetworkType(ConnectionType.valueOf(connectionModel.getConnectionType().toUpperCase()));
                connectionManager.activateConnection();
                Difficulty difficulty = (Difficulty) difficultyToggleGroup.getSelectedToggle().getUserData();
                controller.initSinglePlayerGame(singlePlayerUsernameTextField.getText(), difficulty, connectionManager);
                playSceneTransition(sceneManager.getCurrentScene(), event -> sceneManager.pushScene(root));
            } catch (IOException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
                Logger.getAnonymousLogger().log(Level.SEVERE, ClientMessage.LOAD_FXML_ERROR);
            } catch (NetworkException e) {
                AlertBox.displayBox("Errore di connessione", ClientMessage.CONNECTION_ERROR);
            } finally {
                onMultiPlayerCloseButton(actionEvent);
            }
        }
    }

    /**
     * Close the change connection menu
     *
     * @param actionEvent not used
     */
    @FXML
    public void onConnectionCloseButton(ActionEvent actionEvent) {
        closeEveryPane();
        updateConnectionPaneView();
    }

    /**
     * Apply the changes to the connection
     *
     * @param actionEvent not used
     */
    @FXML
    public void onConnectionApplyButton(ActionEvent actionEvent) {
        if (ipTextField.validate() && portTextField.validate()) {
            connectionModel.setIpAddress(ipTextField.getText());
            connectionModel.setPort(Integer.parseInt(portTextField.getText()));
            connectionModel.setConnectionType(((RadioButton) connectionToggleGroup.getSelectedToggle()).getText());
            closeEveryPane();
        }
    }

    /**
     * Update the pane regarding the change of the connection
     */
    private void updateConnectionPaneView() {
        String ipText = (connectionModel.getIpAddress().equals("localhost")) ? "" : connectionModel.getIpAddress();
        ipTextField.setText(ipText);
        portTextField.setText(String.valueOf(connectionModel.getPort()));
        RadioButton selectedRadioButton = (connectionModel.getConnectionType().equals(ConnectionType.RMI.name()))
                ? radioButtonRMI : radioButtonSocket;
        connectionToggleGroup.selectToggle(selectedRadioButton);
    }

    /**
     * Close every pane: reconnect, connection, single player and multi player
     */
    private void closeEveryPane() {
        reconnectPane.setVisible(false);
        connectionPane.setVisible(false);
        singlePlayerPane.setVisible(false);
        multiPlayerPane.setVisible(false);
    }

    /**
     * @return return the validator for obligatory fields
     */
    private ValidatorBase getRequiredFieldValidator() {
        ValidatorBase requiredValidator = new RequiredFieldValidator();
        requiredValidator.setMessage(ClientMessage.OBLIGATORY);
        return requiredValidator;
    }

    /**
     * @return return the validator for only character fields
     */
    private ValidatorBase getUsernameFieldValidator() {
        ValidatorBase usernameValidator = new UsernameValidator();
        usernameValidator.setMessage(ClientMessage.ONLY_CHARACTER);
        return usernameValidator;
    }
}
