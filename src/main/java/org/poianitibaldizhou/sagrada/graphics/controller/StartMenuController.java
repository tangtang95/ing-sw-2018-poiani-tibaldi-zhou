package org.poianitibaldizhou.sagrada.graphics.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.IOException;

public class StartMenuController extends Controller{

    @FXML
    public Button multiPlayerButton;
    @FXML
    public Button singlePlayerButton;
    @FXML
    public Button changeConnectionButton;
    @FXML
    public Button quitButton;

    public void startMultiPlayerGame(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/lobby.fxml"));
        try {
            Scene scene = new Scene(loader.load(), 1280, 720);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void startSinglePlayerGame(ActionEvent actionEvent) {
    }


    public void changeConnectionMode(ActionEvent actionEvent) {

    }


    public void quitGame(ActionEvent actionEvent) {

    }

    public void onMouseEnter(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setTextFill(Color.TOMATO);
        button.setStyle("-fx-background-color: transparent; -fx-font-size: 4em");
    }


    public void onMouseExit(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setTextFill(Color.BLACK);
        button.setStyle("-fx-background-color: transparent; -fx-font-size: 3em");
    }


}
