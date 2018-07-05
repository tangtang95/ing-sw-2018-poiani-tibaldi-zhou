package org.poianitibaldizhou.sagrada.graphics.view.component;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.graphics.utils.GraphicsUtils;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.ColorWrapper;

public final class ColorView extends Pane {

    private final ColorWrapper colorWrapper;

    private static final String TILE_IMAGE_PATH = "src/test/images/schemaCards/tiles.png";
    private static final String TILE_JSON_PATH = "src/test/images/schemaCards/tiles.json";

    /**
     * Create a ColorView Pane containing an imageView of the color chosen
     *
     * @param colorWrapper the color chosen
     * @param scale the scale value
     */
    public ColorView(ColorWrapper colorWrapper, double scale){
        this.colorWrapper = colorWrapper;
        String keyName = String.format("tile-%s.png", colorWrapper.name().toLowerCase());
        ImageView imageView = GraphicsUtils.getImageView(keyName, TILE_IMAGE_PATH, TILE_JSON_PATH, scale);
        this.getChildren().add(imageView);
    }

    public ColorWrapper getColorWrapper() {
        return colorWrapper;
    }
}
