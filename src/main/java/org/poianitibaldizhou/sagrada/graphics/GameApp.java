package org.poianitibaldizhou.sagrada.graphics;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.poianitibaldizhou.sagrada.graphics.controller.GraphicsController;
import org.poianitibaldizhou.sagrada.graphics.utils.SceneManager;
import org.poianitibaldizhou.sagrada.graphics.utils.WindowSize;

public class GameApp extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        WindowSize size = WindowSize.BIG;

        StackPane scenes = new StackPane();
        SceneManager sceneManager = new SceneManager(scenes, size);

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/game.fxml"));

        Parent root = loader.load();
        GraphicsController controller = loader.getController();
        controller.setSceneManager(sceneManager);
        sceneManager.pushScene(root);

        Scene scene = new Scene(scenes, size.getWidth(), size.getHeight());
        scene.setCamera(new PerspectiveCamera());
        primaryStage.setTitle("Sagrada: il Gioco");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
