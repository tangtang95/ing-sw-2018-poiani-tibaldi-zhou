package org.poianitibaldizhou.sagrada.graphics.view.component;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.Nullable;
import org.poianitibaldizhou.sagrada.graphics.utils.GraphicsUtils;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.PositionWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.SchemaCardWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.TileWrapper;

import java.io.IOException;

public class SchemaCardView extends Pane {

    private ImageView schemaCardImage;
    private SchemaCardWrapper schemaCardWrapper;
    private Canvas shadowImage;

    private DiceView[][] diceViews;

    private double scale;
    private double offsetX;
    private double offsetY;
    private double tileWidth;
    private double tileHeight;

    //Based on SchemaCard.png width
    private static final double OFFSET_X_PERCENT = 0.02439;
    private static final double OFFSET_Y_PERCENT = 0.02439;

    //Based on SchemaCard.png widh
    private static final double WIDTH_TILE_PERCENT = 0.1707;
    private static final double HEIGHT_TILE_PERCENT = 0.1707;

    //Based on SchemaCard.png tileHeight
    private static final double HEIGHT_NAME_PERCENT = 0.9;

    private static final double DICE_SCALE = 0.6;
    private static final double DIFFICULTY_SCALE = 0.4;

    private static final int NUMBER_OF_ROWS = 4;
    private static final int NUMBER_OF_COLUMNS = 5;

    private static final String SCHEMA_CARD_IMAGE_PATH = "images/schemaCards/SchemaCard.png";
    private static final String TILE_IMAGE_PATH = "images/schemaCards/tiles.png";
    private static final String TILE_JSON_PATH = "images/schemaCards/tiles.json";

    public SchemaCardView(SchemaCardWrapper schemaCard){
        this(schemaCard, 1);
    }

    public SchemaCardView(SchemaCardWrapper schemaCard, double scale) {
        super();
        this.scale = scale;
        this.schemaCardWrapper = schemaCard;
        this.diceViews = new DiceView[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];

        Image image = new Image(getClass().getClassLoader()
                .getResourceAsStream(SCHEMA_CARD_IMAGE_PATH));
        schemaCardImage = new ImageView(image);
        schemaCardImage.setFitWidth(image.getWidth() * scale);
        schemaCardImage.setFitHeight(image.getHeight() * scale);
        this.getChildren().add(schemaCardImage);

        offsetX = Math.round(OFFSET_X_PERCENT * image.getWidth()) * scale;
        offsetY = Math.round(OFFSET_Y_PERCENT * image.getWidth()) * scale;
        tileWidth = Math.round(WIDTH_TILE_PERCENT * image.getWidth()) * scale;
        tileHeight = Math.round(HEIGHT_TILE_PERCENT * image.getWidth()) * scale;

        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                TileWrapper tileWrapper = schemaCard.getTile(new PositionWrapper(i, j));

                // DRAW TILE
                if(tileWrapper.getConstraint() != null) {
                    String tileName = tileWrapper.getConstraint().toLowerCase();
                    ImageView tileView = GraphicsUtils.getImageView("tile-" + tileName + ".png", TILE_IMAGE_PATH, TILE_JSON_PATH, scale);
                    tileView.setTranslateX(offsetX * (j + 1) + tileWidth * j);
                    tileView.setTranslateY(offsetY * (i + 1) + tileHeight * i);
                    this.getChildren().add(tileView);
                }
                // DRAW DICE
                DiceWrapper diceWrapper = tileWrapper.getDice();
                if(diceWrapper != null){
                    drawDice(diceWrapper, new PositionWrapper(i, j));
                }
            }
        }

        for (int i = 0; i < schemaCard.getDifficulty(); i++) {
            ImageView difficultyView = drawDifficultyToken(scale*DIFFICULTY_SCALE);
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

        shadowImage = new Canvas(tileWidth*1.5, tileHeight*1.5);
        GraphicsContext gc1 = shadowImage.getGraphicsContext2D();
        gc1.setFill(new Color(0, 0, 0, 0.3));
        gc1.fillOval(0,0, tileWidth*1.5, tileHeight*1.5);
        this.getChildren().add(shadowImage);
        shadowImage.setVisible(false);
    }

    private ImageView drawDifficultyToken(double scale){
        Image difficultyImage = new Image(getClass().getClassLoader().getResourceAsStream("images/schemaCards/difficulty.png"));
        ImageView imageView = new ImageView(difficultyImage);
        imageView.setFitWidth(difficultyImage.getWidth()*scale);
        imageView.setFitHeight(difficultyImage.getHeight()*scale);
        return imageView;
    }



    public ImageView getImageView() {
        return schemaCardImage;
    }

    public SchemaCardWrapper getSchemaCardWrapper() {
        return schemaCardWrapper;
    }

    public void drawDice(DiceWrapper diceWrapper, PositionWrapper pos) {
        double centerX = offsetX * (pos.getColumn() + 1) + tileWidth * pos.getColumn() + tileWidth/2;
        double centerY = offsetY * (pos.getRow() + 1) + tileHeight * pos.getRow() + tileHeight/2;

        DiceView diceView = new DiceView(diceWrapper, scale*DICE_SCALE);
        diceView.setTranslateX(centerX - diceView.getImageWidth()/2);
        diceView.setTranslateY(centerY - diceView.getImageHeight()/2);
        this.getChildren().add(diceView);
        diceViews[pos.getRow()][pos.getColumn()] = diceView;
    }

    public void removeDice(DiceWrapper diceWrapper, PositionWrapper pos) throws IOException {
        DiceView diceView = diceViews[pos.getRow()][pos.getColumn()];
        if(diceView == null || !diceView.getDiceWrapper().equals(diceWrapper)){
            throw new IOException();
        }
        this.getChildren().remove(diceView);
        diceViews[pos.getRow()][pos.getColumn()] = null;
    }

    public void drawShadow(double x, double y) {
        PositionWrapper position = getTilePosition(x, y);
        if(position != null){
            shadowImage.setVisible(true);
            Point2D topLeft = new Point2D(offsetX * (position.getColumn() + 1) + tileWidth * position.getColumn(),
                    offsetY * (position.getRow() + 1) + tileHeight * position.getRow());
            Point2D center = new Point2D(topLeft.getX() + tileWidth/2, topLeft.getY() + tileHeight/2);
            shadowImage.setTranslateX(center.getX() - shadowImage.getWidth()/2);
            shadowImage.setTranslateY(center.getY() - shadowImage.getHeight()/2);
        }
        else{
            removeShadow();
        }
    }

    public PositionWrapper getTilePosition(double x, double y){
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                Point2D topLeft = new Point2D(offsetX * (j + 1) + tileWidth * j,
                        offsetY * (i + 1) + tileHeight * i);
                Rectangle2D rectangle2D = new Rectangle2D(topLeft.getX(), topLeft.getY(), tileWidth, tileHeight);
                if(rectangle2D.contains(x, y)){
                    return new PositionWrapper(i, j);
                }
            }
        }
        return null;
    }

    @Nullable
    public DiceWrapper getDiceByPosition(PositionWrapper positionWrapper){
        if(diceViews[positionWrapper.getRow()][positionWrapper.getColumn()] == null)
            return null;
        return diceViews[positionWrapper.getRow()][positionWrapper.getColumn()].getDiceWrapper();
    }

    public void removeShadow() {
        shadowImage.setVisible(false);
    }

    public void drawSchemaCard(SchemaCardWrapper schemaCardWrapper) {
        clearAllDice();
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                if(schemaCardWrapper.getTile(new PositionWrapper(i, j)).getDice() != null)
                    drawDice(schemaCardWrapper.getTile(new PositionWrapper(i, j)).getDice(), new PositionWrapper(i,j));
            }
        }
    }

    private void clearAllDice() {
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                if(diceViews[i][j] != null){
                    this.getChildren().remove(diceViews[i][j]);
                    diceViews[i][j] = null;
                }
            }
        }
    }
}
