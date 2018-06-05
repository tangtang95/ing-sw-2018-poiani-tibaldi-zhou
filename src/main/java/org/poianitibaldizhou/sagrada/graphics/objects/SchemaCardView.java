package org.poianitibaldizhou.sagrada.graphics.objects;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import org.json.simple.parser.ParseException;
import org.poianitibaldizhou.sagrada.graphics.utils.TextureJSONParser;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SchemaCardView extends Pane {

    private ImageView schemaCardImage;

    //Based on SchemaCard.png width
    private static final double OFFSET_X_PERCENT = 0.02439;
    private static final double OFFSET_Y_PERCENT = 0.02439;

    //Based on SchemaCard.png width
    private static final double WIDTH_TILE_PERCENT = 0.1707;
    private static final double HEIGHT_TILE_PERCENT = 0.1707;

    private static final int NUMBER_OF_ROWS = 4;
    private static final int NUMBER_OF_COLUMNS = 5;

    public SchemaCardView(String JSONString, double scale) {
        super();

        Image image = new Image(getClass().getClassLoader()
                .getResourceAsStream("images/schemaCards/SchemaCard.png"));
        schemaCardImage = new ImageView(image);
        schemaCardImage.setFitWidth(image.getWidth() * scale);
        schemaCardImage.setFitHeight(image.getHeight() * scale);
        this.getChildren().add(schemaCardImage);

        double offsetX = Math.round(OFFSET_X_PERCENT * image.getWidth()) * scale;
        double offsetY = Math.round(OFFSET_Y_PERCENT * image.getWidth()) * scale;
        double width = Math.round(WIDTH_TILE_PERCENT * image.getWidth()) * scale;
        double height = Math.round(HEIGHT_TILE_PERCENT * image.getWidth()) * scale;

        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                ImageView tileView = drawTile("3", scale);
                tileView.setTranslateX(offsetX * (j + 1) + width * j);
                tileView.setTranslateY(offsetY * (i + 1) + height * i);
                this.getChildren().add(tileView);
            }
        }


    }

    private ImageView drawTile(String type, double scale) {
        Image tileImage = new Image(getClass().getClassLoader().getResourceAsStream("images/schemaCards/tiles.png"));
        ImageView imageView = new ImageView(tileImage);
        try {
            TextureJSONParser textureParser = new TextureJSONParser("images/schemaCards/tiles.json");
            String imageName = "tile-%s.png";
            Rectangle2D rectangle2D = textureParser.getRectangleView(String.format(imageName, type));
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
