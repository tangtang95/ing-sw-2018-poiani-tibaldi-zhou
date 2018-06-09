package org.poianitibaldizhou.sagrada.graphics.view.component;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.graphics.utils.TextureUtils;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.PrivateObjectiveCardWrapper;

public class PrivateObjectiveCardView extends Pane {

    private ImageView cardView;

    private PrivateObjectiveCardWrapper privateObjectiveCardWrapper;

    private static final String CARD_IMAGE_PATH = "images/cards/private-objective-cards.png";
    private static final String CARD_JSON_PATH = "images/cards/private-objective-cards.json";

    /**
     * Constructor.
     * Create a pane with the component of the retro card
     *
     * @param scale the number to scale the component
     */
    public PrivateObjectiveCardView(double scale){
        privateObjectiveCardWrapper = null;
        cardView = TextureUtils.getImageView( "retro.png", CARD_IMAGE_PATH, CARD_JSON_PATH, scale);
        this.getChildren().add(cardView);
    }

    public PrivateObjectiveCardView(PrivateObjectiveCardWrapper privateObjectiveCard){
        this(privateObjectiveCard, 1);
    }

    public PrivateObjectiveCardView(PrivateObjectiveCardWrapper privateObjectiveCard, double scale){
        this.privateObjectiveCardWrapper = privateObjectiveCard;
        String cardKey = TextureUtils.convertNameIntoObjectiveCardKey(privateObjectiveCard.getName());
        cardView = TextureUtils.getImageView(cardKey + ".png", CARD_IMAGE_PATH, CARD_JSON_PATH, scale);
        this.getChildren().add(cardView);
    }

    public PrivateObjectiveCardWrapper getPrivateObjectiveCardWrapper() {
        if(privateObjectiveCardWrapper == null)
            throw new IllegalStateException();
        return privateObjectiveCardWrapper;
    }
}
