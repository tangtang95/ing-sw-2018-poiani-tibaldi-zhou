package org.poianitibaldizhou.sagrada.graphics.utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.Contract;

/**
 * OVERVIEW: Display an alert box
 */
public class AlertBox {

    @Contract(" -> fail")
    private AlertBox(){
        throw new IllegalStateException();
    }

    /**
     * Display an alert box
     *
     * @param title title of the alert box
     * @param text text of the alert box
     */
    public static void displayBox(String title, String text){
        Stage popup = new Stage();

        Label label = new Label(text);
        label.setWrapText(true);
        label.setPadding(new Insets(10));
        label.setTextAlignment(TextAlignment.CENTER);

        Button closeButton = new Button();
        closeButton.setText("Chiudi");
        closeButton.setOnAction(event -> popup.close());
        closeButton.setPadding(new Insets(10));

        VBox rootPane = new VBox(10);
        rootPane.setAlignment(Pos.CENTER);
        rootPane.getChildren().addAll(label, closeButton);

        Scene scene = new Scene(rootPane, 300, 150);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle(title);
        popup.setResizable(false);
        popup.setScene(scene);

        popup.showAndWait();
    }
}
