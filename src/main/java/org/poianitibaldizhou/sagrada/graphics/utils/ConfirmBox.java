package org.poianitibaldizhou.sagrada.graphics.utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {

    private static boolean answer;

    public static boolean displayBox(String title, String text){
        Stage popup = new Stage();

        Label label = new Label(text);
        label.setWrapText(true);
        label.setPadding(new Insets(10));
        label.setTextAlignment(TextAlignment.CENTER);

        Button yesButton = new Button("Sì");
        yesButton.setOnAction(event -> {
            answer = true;
            popup.close();
        });
        yesButton.setPrefSize(50, 20);
        yesButton.setPadding(new Insets(10));

        Button noButton = new Button("No");
        noButton.setOnAction(event -> {
            answer = false;
            popup.close();
        });
        noButton.setPrefSize(50, 20);
        noButton.setPadding(new Insets(10));

        HBox buttonPane = new HBox(10);
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.getChildren().addAll(yesButton, noButton);

        VBox rootPane = new VBox(10);
        rootPane.setAlignment(Pos.CENTER);
        rootPane.getChildren().addAll(label, buttonPane);

        Scene scene = new Scene(rootPane, 300, 150);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle(title);
        popup.setResizable(false);
        popup.setScene(scene);

        popup.showAndWait();
        return answer;
    }
}
