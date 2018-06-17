package org.poianitibaldizhou.sagrada.graphics;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.poianitibaldizhou.sagrada.graphics.controller.GraphicsController;
import org.poianitibaldizhou.sagrada.graphics.utils.SceneManager;
import org.poianitibaldizhou.sagrada.graphics.utils.WindowSize;

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

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/start_menu.fxml"));

        Parent root = loader.load();
        GraphicsController controller = loader.getController();
        controller.setSceneManager(sceneManager);
        sceneManager.pushScene(root);

        Scene scene = new Scene(scenes, startSize.getWidth(), startSize.getHeight());
        scene.setCamera(new PerspectiveCamera());
        String css = this.getClass().getClassLoader().getResource("stylesheet/visible-big.css").toExternalForm();
        scene.getStylesheets().add(css);

        scenes.scaleXProperty().bind(Bindings.min(Bindings.min(scene.widthProperty().divide(fixedSize.getWidth()),
                scene.heightProperty().divide(fixedSize.getHeight())), 1.5));
        scenes.scaleYProperty().bind(Bindings.min(Bindings.min(scene.widthProperty().divide(fixedSize.getWidth()),
                scene.heightProperty().divide(fixedSize.getHeight())), 1.5));

        primaryStage.setTitle("Sagrada: il Gioco");
        primaryStage.setFullScreenExitHint("");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.show();
    }
}
