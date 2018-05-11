package org.poianitibaldizhou.sagrada.game.model.cards;

import org.junit.Test;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.exception.RuleViolationType;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.ObjectiveCardType;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.constraint.NumberConstraint;

import static org.junit.Assert.*;

public class TileIntegrationTest {

    @Test
    public void dicePositionableTest(){
        Tile tile = new Tile();
        Tile tilePurple = new Tile(new ColorConstraint(Color.PURPLE));
        Tile tile3 = new Tile(new NumberConstraint(3));
        Dice dice5blue = null;
        Dice dice3purple = null;
        dice5blue = new Dice(5, Color.BLUE);
        dice3purple = new Dice(3, Color.PURPLE);

        try {
            assertTrue(tile.isDicePositionable(dice5blue));
            assertFalse(tilePurple.isDicePositionable(dice5blue));
            assertTrue(tilePurple.isDicePositionable(dice3purple));
            assertFalse(tile3.isDicePositionable(dice5blue));
            assertTrue(tile3.isDicePositionable(dice3purple));
        } catch (Exception e){
            fail("no exception expected");
        }
    }

    @Test
    public void SetRemoveDiceTest(){
        Tile tile = new Tile(new ColorConstraint(Color.BLUE));
        Dice d1 = null;
        try {
            d1 = new Dice(5, Color.BLUE);
            tile.setDice(d1, ObjectiveCardType.NONE);
        } catch (Exception e){
            fail("no exception expected");
        }
        Dice d2 = null;
        try {
             d2 = new Dice(3, Color.PURPLE);
            tile.setDice(d2);
            fail("exception expected");
        } catch (RuleViolationException e) {
            assertEquals(RuleViolationType.TILE_FILLED, e.getViolationType());
            assertTrue(d1.equals(tile.getDice()));
        }
        assertTrue(d1.equals(tile.getDice()));
        Dice removed = tile.removeDice();
        assertTrue(tile.getDice() == null);
        assertTrue(removed.equals(d1));

        try {
            tile.setDice(d2, ObjectiveCardType.NUMBER);
            tile.removeDice();
            tile.setDice(d2, ObjectiveCardType.COLOR);
            fail("exception expected");
        } catch (RuleViolationException e) {
            assertEquals(RuleViolationType.TILE_UNMATCHED, e.getViolationType());
            assertTrue(tile.getDice() == null);
        }
    }
}