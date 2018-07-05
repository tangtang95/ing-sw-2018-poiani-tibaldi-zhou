package org.poianitibaldizhou.sagrada.graphics.view.component;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.graphics.utils.GraphicsUtils;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.PrivateObjectiveCardWrapper;

/**
 * OVERVIEW: Represents the view for a private objective card
 */
public class PrivateObjectiveCardView extends Pane {

    private ImageView cardView;

    private PrivateObjectiveCardWrapper privateObjectiveCardWrapper;

    private static final String CARD_IMAGE_PATH = "src/test/images/cards/private-objective-cards.png";
    private static final String CARD_JSON_PATH = "src/test/images/cards/private-objective-cards.json";

    /**
     * Constructor.
     * Create a pane with the component of the retro card
     *
     * @param scale the number to scale the component
     */
    public PrivateObjectiveCardView(double scale){
        privateObjectiveCardWrapper = null;
        cardView = GraphicsUtils.getImageView( "retro.png", CARD_IMAGE_PATH, CARD_JSON_PATH, scale);
        this.getChildren().add(cardView);
    }

    /**
     * Constructor.
     * Create a PrivateObjectiveCardView (pane) that contains the imageView of the privateObjectiveCard passed
     *
     * @param privateObjectiveCard the model of the privateObjectiveCard
     * @param scale the scale value
     */
    public PrivateObjectiveCardView(PrivateObjectiveCardWrapper privateObjectiveCard, double scale){
        this.privateObjectiveCardWrapper = privateObjectiveCard;
        String cardKey = GraphicsUtils.convertNameIntoObjectiveCardKey(privateObjectiveCard.getName());
        cardView = GraphicsUtils.getImageView(cardKey + ".png", CARD_IMAGE_PATH, CARD_JSON_PATH, scale);
        this.getChildren().add(cardView);
    }

    /**
     * @return the model of the private objective card drawn
     */
    public PrivateObjectiveCardWrapper getPrivateObjectiveCardWrapper() {
        if(privateObjectiveCardWrapper == null)
            throw new IllegalStateException();
        return privateObjectiveCardWrapper;
    }
}
