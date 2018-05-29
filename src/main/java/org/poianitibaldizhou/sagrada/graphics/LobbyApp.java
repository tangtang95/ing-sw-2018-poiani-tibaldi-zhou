package org.poianitibaldizhou.sagrada.graphics;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.poianitibaldizhou.sagrada.graphics.controller.Controller;
import org.poianitibaldizhou.sagrada.graphics.controller.StartMenuController;

import java.net.URL;

public class LobbyApp extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/start_menu.fxml"));
        String css = this.getClass().getClassLoader().getResource("stylesheet/visible-big.css").toExternalForm();

        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setStage(primaryStage);


        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(css);
        primaryStage.setTitle("Sagrada Board Game");
        primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
