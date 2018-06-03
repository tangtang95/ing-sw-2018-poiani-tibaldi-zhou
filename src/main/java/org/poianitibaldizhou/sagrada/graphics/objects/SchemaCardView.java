package org.poianitibaldizhou.sagrada.graphics.objects;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.json.simple.parser.ParseException;

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

    public SchemaCardView(String JSONString) {
        super();

        Image image = new Image(getClass().getClassLoader()
                .getResourceAsStream("images/schemaCards/SchemaCard.png"));
        schemaCardImage = new ImageView(image);
        this.getChildren().add(schemaCardImage);

        double offsetX = Math.round(OFFSET_X_PERCENT * image.getWidth());
        double offsetY = Math.round(OFFSET_Y_PERCENT * image.getWidth());
        double width = Math.round(WIDTH_TILE_PERCENT * image.getWidth());
        double height = Math.round(HEIGHT_TILE_PERCENT * image.getWidth());

        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                ImageView tileView = drawTile("3", i, j);
                tileView.setTranslateX(offsetX * (j + 1) + width * j);
                tileView.setTranslateY(offsetY * (i + 1) + height * i);
                this.getChildren().add(tileView);
            }
        }

        this.setScaleX(0.3);
        this.setScaleY(0.3);

    }

    private ImageView drawTile(String type, int row, int width) {
        Image tileImage = new Image(getClass().getClassLoader().getResourceAsStream("images/schemaCards/tiles.png"));
        ImageView imageView = new ImageView(tileImage);
        try {
            TextureJSONParser textureParser = new TextureJSONParser("images/schemaCards/tiles.json");
            String imageName = "tile-%s.png";
            imageView.setViewport(textureParser.getRectangleView(String.format(imageName, type)));
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "File not founded");
        } catch (ParseException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Parse failed");
        }
        return imageView;
    }


}
