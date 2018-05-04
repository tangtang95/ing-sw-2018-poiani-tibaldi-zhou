package org.poianitibaldizhou.sagrada.graphics;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LobbyApp extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent parent = new AnchorPane();
        Scene scene = new Scene(parent, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
