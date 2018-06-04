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
import org.poianitibaldizhou.sagrada.graphics.utils.WindowSize;

import java.net.URL;

public class ClientApp extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/start_menu.fxml"));

        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setStage(primaryStage);

        WindowSize size = WindowSize.BIG;
        Scene scene = new Scene(root, size.getWidth(), size.getHeight());
        primaryStage.setTitle("Sagrada: il Gioco");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
