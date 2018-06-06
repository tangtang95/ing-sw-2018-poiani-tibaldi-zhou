package org.poianitibaldizhou.sagrada.game.model.cards.objectivecards;

import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;

/**
 * OVERVIEW: A class that implements this interface can returns a score following some certain rules that depends
 * on the concrete object.
 */
public interface IScore {
    int getScore(final SchemaCard schema);
}
