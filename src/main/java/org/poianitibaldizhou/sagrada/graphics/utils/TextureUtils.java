package org.poianitibaldizhou.sagrada.graphics.utils;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TextureUtils {

    public static String convertNameIntoObjectiveCardKey(String name){
        String processedName = name.replace("-", "")
                .replace("  ", " ");
        return convertNameIntoCardKey(processedName);
    }

    public static String convertNameIntoCardKey(String name){
        String[] words = name.split(" ");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1);
        }

        String cardKey = String.join("", words);
        cardKey = cardKey.substring(0, 1).toLowerCase() + cardKey.substring(1);
        return cardKey;
    }

    public static ImageView getImageView(String keyName, String imageResourcePath, String jsonResourcePath, double scale){
        Image image = new Image(TextureUtils.class.getClassLoader().getResourceAsStream(imageResourcePath));
        ImageView imageView = new ImageView(image);
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
        return imageView;
    }

}
