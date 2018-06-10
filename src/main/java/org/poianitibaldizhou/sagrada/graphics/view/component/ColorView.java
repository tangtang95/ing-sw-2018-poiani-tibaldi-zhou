package org.poianitibaldizhou.sagrada.graphics.view.component;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.graphics.utils.GraphicsUtils;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.ColorWrapper;

public class ColorView extends Pane {

    private ImageView imageView;
    private ColorWrapper colorWrapper;

    private static final String TILE_IMAGE_PATH = "images/schemaCards/tiles.png";
    private static final String TILE_JSON_PATH = "images/schemaCards/tiles.json";

    public ColorView(ColorWrapper colorWrapper, double scale){
        String keyName = String.format("tile-%s.png", colorWrapper.name().toLowerCase());
        imageView = GraphicsUtils.getImageView(keyName, TILE_IMAGE_PATH, TILE_JSON_PATH, scale);
        this.getChildren().add(imageView);
    }

    public ColorWrapper getColorWrapper() {
        return colorWrapper;
    }
}
