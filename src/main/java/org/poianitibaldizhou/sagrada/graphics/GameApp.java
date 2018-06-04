package org.poianitibaldizhou.sagrada.graphics;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.poianitibaldizhou.sagrada.cli.ScreenManager;
import org.poianitibaldizhou.sagrada.game.view.CLIStartGameScreen;
import org.poianitibaldizhou.sagrada.graphics.controller.Controller;
import org.poianitibaldizhou.sagrada.graphics.utils.WindowSize;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.ConnectionType;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
public class GameApp extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/multi_game.fxml"));

        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setStage(primaryStage);

        WindowSize size = WindowSize.BIG;
        Scene scene = new Scene(root, size.getWidth(), size.getHeight());
        scene.setCamera(new PerspectiveCamera());
        primaryStage.setTitle("Sagrada: il Gioco");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
