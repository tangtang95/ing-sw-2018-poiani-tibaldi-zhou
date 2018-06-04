package org.poianitibaldizhou.sagrada.graphics;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.poianitibaldizhou.sagrada.graphics.controller.Controller;
import org.poianitibaldizhou.sagrada.graphics.controller.StartMenuController;
import org.poianitibaldizhou.sagrada.graphics.utils.SceneManager;
import org.poianitibaldizhou.sagrada.graphics.utils.WindowSize;

import java.net.URL;

public class ClientApp extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        WindowSize size = WindowSize.BIG;

        StackPane scenes = new StackPane();
        SceneManager sceneManager = new SceneManager(scenes, size);

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/start_menu.fxml"));

        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setStage(primaryStage);
        controller.setSceneManager(sceneManager);
        sceneManager.pushScene(root);

        Scene scene = new Scene(scenes, sceneManager.getSceneWidth(), sceneManager.getSceneHeight());
        primaryStage.setTitle("Sagrada: il Gioco");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
    }
}
