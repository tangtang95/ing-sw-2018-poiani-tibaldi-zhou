package org.poianitibaldizhou.sagrada.graphics.view;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.graphics.utils.TextureUtils;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.PositionWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.SchemaCardWrapper;

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

    private static final String TILE_IMAGE_PATH = "images/schemaCards/tiles.png";
    private static final String TILE_JSON_PATH = "images/schemaCards/tiles.json";

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
                ImageView tileView = TextureUtils.getImageView("tile-" + tileName + ".png", TILE_IMAGE_PATH, TILE_JSON_PATH, scale);
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

    private ImageView drawDifficultyToken(double scale){
        Image difficultyImage = new Image(getClass().getClassLoader().getResourceAsStream("images/schemaCards/difficulty.png"));
        ImageView imageView = new ImageView(difficultyImage);
        imageView.setFitWidth(difficultyImage.getWidth()*scale);
        imageView.setFitHeight(difficultyImage.getHeight()*scale);
        return imageView;
    }


}
