package org.poianitibaldizhou.sagrada.graphics.view.component;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.graphics.utils.GraphicsUtils;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.PublicObjectiveCardWrapper;

public class PublicObjectiveCardView extends Pane {

    private ImageView cardView;

    private static final String CARD_IMAGE_PATH = "images/cards/public-objective-cards.png";
    private static final String CARD_JSON_PATH = "images/cards/public-objective-cards.json";

    public PublicObjectiveCardView(double scale){
        cardView = GraphicsUtils.getImageView( "retro.png", CARD_IMAGE_PATH, CARD_JSON_PATH, scale);
        this.getChildren().add(cardView);
    }

    public PublicObjectiveCardView(PublicObjectiveCardWrapper publicObjectiveCard){
        this(publicObjectiveCard, 1);
    }

    public PublicObjectiveCardView(PublicObjectiveCardWrapper publicObjectiveCard, double scale){
        String cardKey = GraphicsUtils.convertNameIntoObjectiveCardKey(publicObjectiveCard.getName());
        cardView = GraphicsUtils.getImageView(cardKey + ".png", CARD_IMAGE_PATH, CARD_JSON_PATH, scale);

        this.getChildren().add(cardView);
    }

}
