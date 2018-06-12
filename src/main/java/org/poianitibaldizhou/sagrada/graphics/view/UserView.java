package org.poianitibaldizhou.sagrada.graphics.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.TextAlignment;
import org.poianitibaldizhou.sagrada.graphics.utils.GraphicsUtils;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;


public class UserView extends Pane {

    private ImageView imageView;
    private Canvas canvas;

    private static final String RETRO_IMAGE_PATH = "images/cards/tool-cards.png";
    private static final String RETRO_JSON_PATH = "images/cards/tool-cards.json";

    public UserView(double scale){
        String retroKeyName = "retro.png";
        imageView = GraphicsUtils.getImageView(retroKeyName, RETRO_IMAGE_PATH, RETRO_JSON_PATH, scale);
        canvas = new Canvas(imageView.getFitWidth(), imageView.getFitHeight());

        this.getStyleClass().add("user-view");

        this.getChildren().add(imageView);
        this.onRotateProperty().addListener(((observable, oldValue, newValue) ->{

        }));
    }

    public void drawRetro(){
        this.getChildren().clear();
        this.getChildren().add(imageView);
    }

    public void drawUserCanvas(UserWrapper userWrapper){
        this.getChildren().clear();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.color(1.0, 0.898, 0.7137));
        gc.fillRect(0,0, canvas.getWidth(), canvas.getHeight());
        gc.setFont(Font.font(canvas.getHeight()/10));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFill(Color.BLACK);
        gc.fillText(userWrapper.getUsername(), canvas.getWidth()/2, canvas.getHeight()/3);
        gc.setFont(Font.font(canvas.getHeight()/15));
        gc.setFill(Color.RED);
        gc.fillText("Ready!", canvas.getWidth()/2, canvas.getHeight()*2/3);
        gc.setFont(Font.font("System", FontPosture.ITALIC, canvas.getHeight()/15));
        this.getChildren().add(canvas);
    }



}
