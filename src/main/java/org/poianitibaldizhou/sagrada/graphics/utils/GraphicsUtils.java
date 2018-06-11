package org.poianitibaldizhou.sagrada.graphics.utils;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GraphicsUtils {

    private GraphicsUtils(){
        throw new IllegalStateException("Cannot instantiate GraphicsUtils");
    }

    public static String convertNameIntoObjectiveCardKey(String name){
        String processedName = name.replace("-", "");
        return convertNameIntoCardKey(processedName);
    }

    public static String convertNameIntoCardKey(String name){
        String[] words = name.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1);
        }

        String cardKey = String.join("", words);
        cardKey = cardKey.substring(0, 1).toLowerCase() + cardKey.substring(1);
        return cardKey;
    }

    public static ImageView getImageView(String keyName, String imageResourcePath, String jsonResourcePath, double scale){
        Image image = new Image(GraphicsUtils.class.getClassLoader().getResourceAsStream(imageResourcePath));
        ImageView imageView = new ImageView(image);
        changeViewport(imageView, keyName, jsonResourcePath, scale);
        return imageView;
    }

    public static void changeViewport(ImageView imageView, String keyName, String jsonResourcePath, double scale) {
        try {
            TextureJSONParser textureParser = new TextureJSONParser(jsonResourcePath);
            Rectangle2D rectangle2D = textureParser.getRectangleView(keyName);
            imageView.setViewport(rectangle2D);
            imageView.setFitWidth(rectangle2D.getWidth() * scale);
            imageView.setFitHeight(rectangle2D.getHeight() * scale);
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "File not founded");
        } catch (ParseException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Parse failed");
        }
    }

    public static ImageView getSimpleImageView(String imageResourcePath, double scale){
        Image image = new Image(GraphicsUtils.class.getClassLoader().getResourceAsStream(imageResourcePath));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(image.getWidth()*scale);
        imageView.setFitHeight(image.getHeight()*scale);
        return imageView;
    }

    public static JFXButton getButton(String text, String classCSS){
        JFXButton button = new JFXButton(text);
        button.setButtonType(JFXButton.ButtonType.RAISED);
        button.getStyleClass().add(classCSS);
        return button;
    }

    public static JFXRadioButton getRadioButton(String text, String classCSS, Color unselectedColor, Color selectedColor){
        JFXRadioButton radioButton = new JFXRadioButton(text);
        radioButton.setUnSelectedColor(unselectedColor);
        radioButton.setSelectedColor(selectedColor);
        radioButton.getStyleClass().add(classCSS);
        radioButton.setAlignment(Pos.CENTER);
        radioButton.setFocusTraversable(false);
        return radioButton;
    }

    public static Point2D getImageSize(String jsonResourcePath){
        TextureJSONParser textureParser = null;
        try {
            textureParser = new TextureJSONParser(jsonResourcePath);
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "File not founded");
        } catch (ParseException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Parse failed");
        }
        return (textureParser == null) ? new Point2D(0, 0) : textureParser.getImageSize();
    }


    public static Image getImage(String resourceFolderPath, String imageKey, double width, double height) {
        return new Image(GraphicsUtils.class.getClassLoader().getResourceAsStream(resourceFolderPath + imageKey),
                width, height, true, true);
    }

    public static void drawImage(GraphicsContext gc, double x, double y, String keyName, String imageResourcePath,
                                 String jsonResourcePath, double scale) {

        TextureJSONParser textureParser = null;
        try {
            textureParser = new TextureJSONParser(jsonResourcePath);
            Rectangle2D rectangle2D = textureParser.getRectangleView(keyName);
            Point2D size = textureParser.getImageSize();
            Image image = new Image(GraphicsUtils.class.getClassLoader().getResourceAsStream(imageResourcePath),
                    size.getX()*scale, size.getY()*scale, true, true);
            gc.drawImage(image, rectangle2D.getMinX()*scale, rectangle2D.getMinY()*scale, rectangle2D.getWidth()*scale,
                    rectangle2D.getHeight()*scale, x, y, rectangle2D.getWidth()*scale,
                    rectangle2D.getHeight()*scale);
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "File not founded");
        } catch (ParseException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Parse failed");
        }

    }
}