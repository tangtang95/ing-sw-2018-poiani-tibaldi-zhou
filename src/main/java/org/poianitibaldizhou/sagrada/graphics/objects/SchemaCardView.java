package org.poianitibaldizhou.sagrada.graphics.objects;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.json.simple.parser.ParseException;
import org.poianitibaldizhou.sagrada.graphics.utils.TextureJSONParser;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.PositionWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.SchemaCardWrapper;

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

    //Based on SchemaCard.png height
    private static final double HEIGHT_NAME_PERCENT = 0.9;

    private static final int NUMBER_OF_ROWS = 4;
    private static final int NUMBER_OF_COLUMNS = 5;

    public SchemaCardView(SchemaCardWrapper schemaCard){
        this(schemaCard, 1);
    }

    public SchemaCardView(SchemaCardWrapper schemaCard, double scale) {
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
                String tileName = schemaCard.getTile(new PositionWrapper(i,j)).getConstraint() == null ?
                        "empty" : schemaCard.getTile(new PositionWrapper(i,j)).getConstraint().toLowerCase();
                ImageView tileView = drawTile(tileName, scale);
                tileView.setTranslateX(offsetX * (j + 1) + width * j);
                tileView.setTranslateY(offsetY * (i + 1) + height * i);
                this.getChildren().add(tileView);
            }
        }

        for (int i = 0; i < schemaCard.getDifficulty(); i++) {
            ImageView difficultyView = drawDifficultyToken(scale);
            difficultyView.setTranslateX(image.getWidth()*scale - difficultyView.getFitWidth()*(i+2));
            difficultyView.setTranslateY(image.getHeight()*scale*HEIGHT_NAME_PERCENT + difficultyView.getFitHeight()/2);
            this.getChildren().add(difficultyView);
        }

        Label label = new Label(schemaCard.getName());
        label.setStyle("-fx-text-fill: white");
        label.translateXProperty().bind(image.widthProperty().multiply(scale).divide(2)
                .subtract(label.widthProperty().divide(2)));
        label.setTranslateY(image.getHeight()*scale*HEIGHT_NAME_PERCENT);
        this.getChildren().add(label);
    }

    private ImageView drawTile(String type, double scale) {
        Image tileImage = new Image(getClass().getClassLoader().getResourceAsStream("images/schemaCards/tiles.png"));
        ImageView imageView = new ImageView(tileImage);
        try {
            TextureJSONParser textureParser = new TextureJSONParser("images/schemaCards/tiles.json");
            Rectangle2D rectangle2D = textureParser.getRectangleView(String.format("tile-%s.png", type));
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

    private ImageView drawDifficultyToken(double scale){
        Image difficultyImage = new Image(getClass().getClassLoader().getResourceAsStream("images/schemaCards/difficulty.png"));
        ImageView imageView = new ImageView(difficultyImage);
        imageView.setFitWidth(difficultyImage.getWidth()*scale);
        imageView.setFitHeight(difficultyImage.getHeight()*scale);
        return imageView;
    }


}
