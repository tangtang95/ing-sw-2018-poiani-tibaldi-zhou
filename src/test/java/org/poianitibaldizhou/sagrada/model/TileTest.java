package org.poianitibaldizhou.sagrada.model;

import org.junit.Test;
import org.poianitibaldizhou.sagrada.exception.ConstraintTypeException;
import org.poianitibaldizhou.sagrada.exception.DiceInvalidNumberException;
import org.poianitibaldizhou.sagrada.exception.TileFilledException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.NumberConstraint;
import org.poianitibaldizhou.sagrada.game.model.cards.ConstraintType;
import org.poianitibaldizhou.sagrada.game.model.cards.Tile;

import static org.junit.Assert.*;

public class TileTest {

    @Test
    public void dicePositionableTest(){
        Tile tile = new Tile();
        Tile tilePurple = new Tile(new ColorConstraint(Color.PURPLE));
        Tile tile3 = new Tile(new NumberConstraint(3));
        Dice dice5blue = null;
        Dice dice3purple = null;
        try {
            dice5blue = new Dice(5, Color.BLUE);
            dice3purple = new Dice(3, Color.PURPLE);
        } catch (DiceInvalidNumberException e) {
            fail("Error not excepted");
        }
        try {
            assertTrue(tile.isDicePositionable(dice5blue));
            assertFalse(tilePurple.isDicePositionable(dice5blue));
            assertTrue(tilePurple.isDicePositionable(dice3purple));
            assertFalse(tile3.isDicePositionable(dice5blue));
            assertTrue(tile3.isDicePositionable(dice3purple));
        } catch (ConstraintTypeException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void SetRemoveDiceTest(){
        Tile tile = new Tile();
        Dice d1 = null;
        try {
            d1 = new Dice(5, Color.BLUE);
            tile.setDice(d1, ConstraintType.NONE);
        } catch (TileFilledException e) {
            fail("No TileFilledException expected");
        } catch (DiceInvalidNumberException e) {
            fail("No invalidNumber expected");
        } catch (ConstraintTypeException e) {
            fail("No exception expected");
        }
        assertTrue(d1.equals(tile.getDice()));
        Dice removed = tile.removeDice();
        assertTrue(tile.getDice() == null);
        assertTrue(removed == d1);

    }
}
