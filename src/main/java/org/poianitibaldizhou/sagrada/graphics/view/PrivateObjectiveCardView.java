package org.poianitibaldizhou.sagrada.graphics.view;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.graphics.utils.TextureUtils;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.PrivateObjectiveCardWrapper;

public class PrivateObjectiveCardView extends Pane {

    private ImageView cardView;

    private static final String CARD_IMAGE_PATH = "images/cards/private-objective-cards.png";
    private static final String CARD_JSON_PATH = "images/cards/private-objective-cards.json";

    /**
     * Constructor.
     * Create a pane with the image of the retro card
     *
     * @param scale the number to scale the image
     */
    public PrivateObjectiveCardView(double scale){
        cardView = TextureUtils.getImageView( "retro.png", CARD_IMAGE_PATH, CARD_JSON_PATH, scale);
        this.getChildren().add(cardView);
    }

    public PrivateObjectiveCardView(PrivateObjectiveCardWrapper privateObjectiveCard){
        this(privateObjectiveCard, 1);
    }

    public PrivateObjectiveCardView(PrivateObjectiveCardWrapper privateObjectiveCard, double scale){
        String cardKey = TextureUtils.convertNameIntoObjectiveCardKey(privateObjectiveCard.getName());
        System.out.println(cardKey);
        cardView = TextureUtils.getImageView(cardKey + ".png", CARD_IMAGE_PATH, CARD_JSON_PATH, scale);

        this.getChildren().add(cardView);
    }


}
