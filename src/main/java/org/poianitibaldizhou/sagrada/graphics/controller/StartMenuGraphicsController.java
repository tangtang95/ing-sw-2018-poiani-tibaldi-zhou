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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.poianitibaldizhou.sagrada.exception.NetworkException;
import org.poianitibaldizhou.sagrada.graphics.model.ConnectionModel;
import org.poianitibaldizhou.sagrada.graphics.utils.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.ConnectionType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeConnectionView();
        initializeSinglePlayerView();
        initializeMultiPlayerView();
        initializeReconnectView();
    }

    private void initializeReconnectView() {
        reconnectUsernameTextField.setValidators(getRequiredFieldValidator(), getUsernameFieldValidator());
    }

    private void initializeMultiPlayerView() {
        usernameTextField.setValidators(getRequiredFieldValidator(), getUsernameFieldValidator());
    }

    private void initializeConnectionView(){
        connectionModel = new ConnectionModel();
        connectionType.textProperty().bind(connectionModel.connectionTypeProperty());
        connectionToggleGroup = new ToggleGroup();
        radioButtonRMI.setToggleGroup(connectionToggleGroup);
        radioButtonSocket.setToggleGroup(connectionToggleGroup);
        radioButtonRMI.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue && portTextField.getText().equals(String.valueOf(ConnectionType.SOCKET.getPort()))){
                portTextField.setText(String.valueOf(ConnectionType.RMI.getPort()));
            }
        });
        radioButtonSocket.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue && portTextField.getText().equals(String.valueOf(ConnectionType.RMI.getPort()))){
                portTextField.setText(String.valueOf(ConnectionType.SOCKET.getPort()));
            }
        });

        NumberValidator numberValidator = new NumberValidator();
        numberValidator.setMessage("Solo numeri");
        portTextField.setValidators(numberValidator);
        portTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                portTextField.validate();
            }
        });

        IPAddressValidator ipAddressValidator = new IPAddressValidator();
        ipAddressValidator.setMessage("Non è un indirizzo IP");
        ipTextField.setValidators(ipAddressValidator);
        ipTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue)
                ipTextField.validate();
        });

    }

    private void initializeSinglePlayerView(){
        singlePlayerUsernameTextField.setValidators(getRequiredFieldValidator(), getUsernameFieldValidator());

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
    public void initStartMenuScene(){
        menuButtonPane.setPrefWidth(sceneManager.getSceneWidth()*0.4);
        menuButtonPane.setPadding(new Insets(sceneManager.getSceneHeight()/10, 0, 0, sceneManager.getSceneWidth()/20));
        rightPane.setPrefWidth(sceneManager.getSceneWidth()*0.6);
    }


    @FXML
    public void startMultiPlayerGame(ActionEvent actionEvent) {
        closeEveryPane();
        playOpenMenuPaneTransition(multiPlayerPane);
        multiPlayerPane.setVisible(true);
        multiPlayerPane.toFront();
    }

    @FXML
    public void startSinglePlayerGame(ActionEvent actionEvent) {
        closeEveryPane();
        playOpenMenuPaneTransition(singlePlayerPane);
        singlePlayerPane.setVisible(true);
        singlePlayerPane.toFront();
    }

    @FXML
    public void changeConnectionMode(ActionEvent actionEvent) {
        updateConnectionPaneView();
        closeEveryPane();
        playOpenMenuPaneTransition(connectionPane);
        connectionPane.setVisible(true);
        connectionPane.toFront();
    }

    private void playOpenMenuPaneTransition(Node node){
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(500), node);
        translateTransition.fromXProperty().bind(rightPane.translateXProperty().subtract(rightPane.widthProperty().divide(3)));
        translateTransition.toXProperty().bind(rightPane.translateXProperty());
        translateTransition.play();

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), node);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    @FXML
    public void quitGame(ActionEvent actionEvent) {
        playSceneTransition(sceneManager.getCurrentScene(), event -> {
            sceneManager.getPrimaryStage().close();
            System.exit(0);
        });
    }

    @FXML
    public void onReconnectButtonAction(ActionEvent actionEvent) {
        closeEveryPane();
        playOpenMenuPaneTransition(reconnectPane);
        reconnectPane.setVisible(true);
        reconnectPane.toFront();
    }

    @FXML
    public void onReconnectCloseButton(ActionEvent actionEvent) {
        closeEveryPane();
        reconnectUsernameTextField.setText("");
    }

    @FXML
    public void onReconnectPlayButton(ActionEvent actionEvent) {
        if(reconnectUsernameTextField.validate()){

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/game.fxml"));

            try {
                Parent root = loader.load();
                GameGraphicsController controller = loader.getController();
                controller.setSceneManager(sceneManager);
                ConnectionManager connectionManager = new ConnectionManager(connectionModel.getIpAddress(),
                        connectionModel.getPort(), ConnectionType.valueOf(connectionModel.getConnectionType().toUpperCase()));
                controller.initReconnectMultiPlayerGame(reconnectUsernameTextField.getText(), connectionManager);
                playSceneTransition(sceneManager.getCurrentScene(), event -> sceneManager.pushScene(root));
            } catch (IOException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
                Logger.getAnonymousLogger().log(Level.SEVERE, "Cannot load FXML loader");
            } catch (NetworkException e) {
                AlertBox.displayBox("Errore di connessione", "Non è stato possibile connettersi al server");
            } finally {
                onReconnectCloseButton(actionEvent);
            }
        }
    }

    @FXML
    public void onMultiPlayerCloseButton(ActionEvent actionEvent) {
        closeEveryPane();
        usernameTextField.setText("");
    }

    @FXML
    public void onMultiPlayerPlayButton(ActionEvent actionEvent) {
        if(usernameTextField.validate()){

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/lobby.fxml"));

            try {
                Parent root = loader.load();
                LobbyGraphicsController controller = loader.getController();
                controller.setSceneManager(sceneManager);
                ConnectionManager connectionManager = new ConnectionManager(connectionModel.getIpAddress(),
                        connectionModel.getPort(), ConnectionType.valueOf(connectionModel.getConnectionType().toUpperCase()));
                controller.initLobbyModel(usernameTextField.getText(), connectionManager);
                playSceneTransition(sceneManager.getCurrentScene(), event -> sceneManager.pushScene(root));
            } catch (IOException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
                Logger.getAnonymousLogger().log(Level.SEVERE, "Cannot load FXML loader");
            } catch (NetworkException e) {
                AlertBox.displayBox("Errore del server", e.getInnerException().getMessage());
            } finally {
                onMultiPlayerCloseButton(actionEvent);
            }
        }
    }

    @FXML
    public void onSinglePlayerCloseButton(ActionEvent actionEvent) {
        difficultyToggleGroup.selectToggle(radioButtonMedium);
        closeEveryPane();
        singlePlayerUsernameTextField.setText("");
    }

    @FXML
    public void onSinglePlayerPlayButton(ActionEvent actionEvent) {
        if(difficultyToggleGroup.getSelectedToggle() == null)
            return;
        if(singlePlayerUsernameTextField.validate()) {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/game.fxml"));

            try {
                Pane root = loader.load();
                GameGraphicsController controller = loader.getController();
                controller.setSceneManager(sceneManager);
                ConnectionManager connectionManager = new ConnectionManager(connectionModel.getIpAddress(),
                        connectionModel.getPort(), ConnectionType.valueOf(connectionModel.getConnectionType().toUpperCase()));
                Difficulty difficulty = (Difficulty) difficultyToggleGroup.getSelectedToggle().getUserData();
                controller.initSinglePlayerGame(singlePlayerUsernameTextField.getText(), difficulty, connectionManager);
                playSceneTransition(sceneManager.getCurrentScene(), event -> sceneManager.pushScene(root));
            } catch (IOException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
                Logger.getAnonymousLogger().log(Level.SEVERE, "Cannot load FXML loader");
            } catch (NetworkException e) {
                AlertBox.displayBox("Errore di connessione", "Non è possibile collegarsi al server");
            } finally {
                onMultiPlayerCloseButton(actionEvent);
            }
        }
    }

    @FXML
    public void onConnectionCloseButton(ActionEvent actionEvent) {
        closeEveryPane();
        updateConnectionPaneView();
    }

    @FXML
    public void onConnectionApplyButton(ActionEvent actionEvent) {
        if(ipTextField.validate() && portTextField.validate()) {
            connectionModel.setIpAddress(ipTextField.getText());
            connectionModel.setPort(Integer.parseInt(portTextField.getText()));
            connectionModel.setConnectionType(((RadioButton) connectionToggleGroup.getSelectedToggle()).getText());
            closeEveryPane();
        }
    }

    private void updateConnectionPaneView(){
        String ipText = (connectionModel.getIpAddress().equals("localhost")) ? "" : connectionModel.getIpAddress();
        ipTextField.setText(ipText);
        portTextField.setText(String.valueOf(connectionModel.getPort()));
        RadioButton selectedRadioButton = (connectionModel.getConnectionType().equals(ConnectionType.RMI.name()))
                ? radioButtonRMI: radioButtonSocket;
        connectionToggleGroup.selectToggle(selectedRadioButton);
    }

    private void closeEveryPane() {
        reconnectPane.setVisible(false);
        connectionPane.setVisible(false);
        singlePlayerPane.setVisible(false);
        multiPlayerPane.setVisible(false);
    }

    private ValidatorBase getRequiredFieldValidator(){
        ValidatorBase requiredValidator = new RequiredFieldValidator();
        requiredValidator.setMessage("Obbligatorio");
        return requiredValidator;
    }

    private ValidatorBase getUsernameFieldValidator(){
        ValidatorBase usernameValidator = new UsernameValidator();
        usernameValidator.setMessage("Solo caratteri alfanumerici");
        return  usernameValidator;
    }


}
