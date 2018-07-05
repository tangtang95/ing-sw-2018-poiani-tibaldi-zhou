package org.poianitibaldizhou.sagrada.graphics.view.component;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.graphics.utils.GraphicsUtils;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.PublicObjectiveCardWrapper;

/**
 * OVERVIEW: Represents the view of a public objective card
 */
public class PublicObjectiveCardView extends Pane {

    private static final String CARD_IMAGE_PATH = "src/test/images/cards/public-objective-cards.png";
    private static final String CARD_JSON_PATH = "src/test/images/cards/public-objective-cards.json";

    /**
     * Constructor.
     * Create a PublicObjectiveCardView that contains the imageView of the publicObjectiveCard passed
     *
     * @param publicObjectiveCard the model of the public objective card
     * @param scale the scale value
     */
    public PublicObjectiveCardView(PublicObjectiveCardWrapper publicObjectiveCard, double scale){
        String cardKey = GraphicsUtils.convertNameIntoObjectiveCardKey(publicObjectiveCard.getName());
        ImageView cardView = GraphicsUtils.getImageView(cardKey + ".png", CARD_IMAGE_PATH, CARD_JSON_PATH, scale);

        this.getChildren().add(cardView);
    }

}
