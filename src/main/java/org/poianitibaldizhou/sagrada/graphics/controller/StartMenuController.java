package org.poianitibaldizhou.sagrada.graphics.controller;

import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.poianitibaldizhou.sagrada.graphics.model.ConnectionModel;
import org.poianitibaldizhou.sagrada.graphics.utils.IPAddressValidator;
import org.poianitibaldizhou.sagrada.graphics.utils.UsernameValidator;
import org.poianitibaldizhou.sagrada.network.ConnectionType;

import java.net.URL;
import java.util.ResourceBundle;

public class StartMenuController extends Controller implements Initializable {

    @FXML
    public Parent rootPane;

    @FXML
    public BorderPane rightPane;

    @FXML
    public Label connectionType;

    @FXML
    public VBox connectionPane;

    @FXML
    public VBox multiPlayerPane;

    @FXML
    public VBox singlePlayerPane;

    //MultiPlayerPane
    @FXML
    public JFXTextField usernameTextField;

    //SinglePlayerPane
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
    }

    private void initializeMultiPlayerView() {
        RequiredFieldValidator requiredValidator = new RequiredFieldValidator();
        requiredValidator.setMessage("Obbligatorio");

        ValidatorBase usernameValidator = new UsernameValidator();
        usernameValidator.setMessage("Solo caratteri alfanumerici");

        usernameTextField.setValidators(requiredValidator, usernameValidator);
    }

    private void initializeConnectionView(){
        connectionModel = new ConnectionModel();
        connectionType.textProperty().bind(connectionModel.connectionTypeProperty());
        connectionToggleGroup = new ToggleGroup();
        radioButtonRMI.setToggleGroup(connectionToggleGroup);
        radioButtonSocket.setToggleGroup(connectionToggleGroup);

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
        difficultyToggleGroup = new ToggleGroup();
        radioButtonVeryEasy.setToggleGroup(difficultyToggleGroup);
        radioButtonEasy.setToggleGroup(difficultyToggleGroup);
        radioButtonMedium.setToggleGroup(difficultyToggleGroup);
        radioButtonHard.setToggleGroup(difficultyToggleGroup);
        radioButtonHell.setToggleGroup(difficultyToggleGroup);

        difficultyToggleGroup.selectToggle(radioButtonMedium);
    }


    @FXML
    public void startMultiPlayerGame(ActionEvent actionEvent) {
        closeEveryPane();
        multiPlayerPane.setVisible(true);
        multiPlayerPane.toFront();
    }

    @FXML
    public void startSinglePlayerGame(ActionEvent actionEvent) {
        closeEveryPane();
        singlePlayerPane.setVisible(true);
        singlePlayerPane.toFront();
    }

    @FXML
    public void changeConnectionMode(ActionEvent actionEvent) {
        updateConnectionPaneView();
        closeEveryPane();
        connectionPane.setVisible(true);
        connectionPane.toFront();
    }

    @FXML
    public void quitGame(ActionEvent actionEvent) {
        playSceneTransition(rootPane, (event) -> stage.close());
    }

    public void onMultiPlayerCloseButton(ActionEvent actionEvent) {
        closeEveryPane();
        usernameTextField.setText("");
    }

    public void onMultiPlayerPlayButton(ActionEvent actionEvent) {
        if(usernameTextField.validate()){

        }
    }

    public void onSinglePlayerCloseButton(ActionEvent actionEvent) {
        difficultyToggleGroup.selectToggle(radioButtonMedium);
        closeEveryPane();
    }

    public void onSinglePlayerPlayButton(ActionEvent actionEvent) {

    }

    public void onConnectionCloseButton(ActionEvent actionEvent) {
        closeEveryPane();
        updateConnectionPaneView();
    }

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

    private void switchScene(Parent rootPane) {
        Scene scene = new Scene(rootPane, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
    }

    private void closeEveryPane(){
        connectionPane.setVisible(false);
        singlePlayerPane.setVisible(false);
        multiPlayerPane.setVisible(false);

    }
}
