package org.poianitibaldizhou.sagrada.game.model.cards;

public class DiagonalPublicObjectiveCard extends PublicObjectiveCard{

    protected DiagonalPublicObjectiveCard(String name, String description, int cardPoints) {
        super(name, description, cardPoints);
    }

    @Override
    public int getScore(SchemaCard schema) {
        return 0;
    }

}
