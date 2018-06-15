package org.poianitibaldizhou.sagrada.graphics;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
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
        WindowSize fixedSize = WindowSize.BIG;
        WindowSize startSize = WindowSize.MEDIUM;


        StackPane scenes = new StackPane();
        scenes.setBackground(Background.EMPTY);
        SceneManager sceneManager = new SceneManager(scenes, fixedSize);

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/score_scene.fxml"));

        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setSceneManager(sceneManager);
        sceneManager.pushScene(root);

        Scene scene = new Scene(scenes, startSize.getWidth(), startSize.getHeight());
        scene.setCamera(new PerspectiveCamera());
        String css = this.getClass().getClassLoader().getResource("stylesheet/visible-big.css").toExternalForm();
        scene.getStylesheets().add(css);

        scenes.scaleXProperty().bind(Bindings.min(Bindings.min(scene.widthProperty().divide(fixedSize.getWidth()),
                scene.heightProperty().divide(fixedSize.getHeight())), 1));
        scenes.scaleYProperty().bind(Bindings.min(Bindings.min(scene.widthProperty().divide(fixedSize.getWidth()),
                scene.heightProperty().divide(fixedSize.getHeight())), 1));

        primaryStage.setTitle("Sagrada: il Gioco");
        primaryStage.setFullScreenExitHint("");
        primaryStage.setMaxWidth(fixedSize.getWidth());
        primaryStage.setMaxHeight(fixedSize.getHeight());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
