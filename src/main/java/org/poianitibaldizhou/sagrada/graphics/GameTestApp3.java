package org.poianitibaldizhou.sagrada.graphics;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.poianitibaldizhou.sagrada.graphics.controller.GameController;
import org.poianitibaldizhou.sagrada.graphics.utils.SceneManager;
import org.poianitibaldizhou.sagrada.graphics.utils.WindowSize;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.ConnectionType;

public class GameTestApp3 extends Application{
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        WindowSize fixedSize = WindowSize.BIG;

        StackPane scenes = new StackPane();
        SceneManager sceneManager = new SceneManager(scenes, fixedSize);

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/game.fxml"));

        Parent root = loader.load();
        GameController controller = loader.getController();
        controller.setSceneManager(sceneManager);
        ConnectionManager connectionManager = new ConnectionManager("localhost", ConnectionType.RMI.getPort(), ConnectionType.RMI);
        controller.initMultiPlayerGame(String.valueOf("cordero3".hashCode()), "cordero3", "corderoGame", connectionManager);
        sceneManager.pushScene(root);

        WindowSize startSize = WindowSize.MEDIUM;

        Scene scene = new Scene(scenes, startSize.getWidth(), startSize.getHeight());
        scene.setCamera(new PerspectiveCamera());
        String css = this.getClass().getClassLoader().getResource("stylesheet/visible-big.css").toExternalForm();
        scene.getStylesheets().add(css);

        root.scaleXProperty().bind(scene.widthProperty().divide(fixedSize.getWidth()));
        root.scaleYProperty().bind(scene.widthProperty().divide(fixedSize.getWidth()));

        primaryStage.setTitle("Sagrada: il Gioco");
        primaryStage.setFullScreenExitHint("");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
