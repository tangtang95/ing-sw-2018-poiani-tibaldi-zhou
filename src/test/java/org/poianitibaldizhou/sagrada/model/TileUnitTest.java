package org.poianitibaldizhou.sagrada.model;

import org.junit.Test;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.Tile;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TileUnitTest {

    @Test
    public void dicePositionableTest(){
        NoConstraint noc = mock(NoConstraint.class);

        ColorConstraint cc1 = mock(ColorConstraint.class);
        when(cc1.getColor()).thenReturn(Color.PURPLE);

        NumberConstraint nc1 = mock(NumberConstraint.class);
        when(nc1.getNumber()).thenReturn(3);

        Tile noConstraintTile = new Tile(noc);
        Tile tilePurple = new Tile(cc1);
        Tile tile3 = new Tile(nc1);

        ColorConstraint cc2 = mock(ColorConstraint.class);
        when(cc2.getColor()).thenReturn(Color.BLUE);
        NumberConstraint nc2 = mock(NumberConstraint.class);
        when(nc2.getNumber()).thenReturn(5);

        Dice dice5blue = mock(Dice.class);
        when(dice5blue.getColorConstraint()).thenReturn(cc2);
        when(dice5blue.getNumberConstraint()).thenReturn(nc2);

        ColorConstraint cc3 = mock(ColorConstraint.class);
        when(cc3.getColor()).thenReturn(Color.PURPLE);
        NumberConstraint nc3 = mock(NumberConstraint.class);
        when(nc3.getNumber()).thenReturn(3);

        Dice dice3purple = mock(Dice.class);
        when(dice3purple.getColorConstraint()).thenReturn(cc3);
        when(dice3purple.getNumberConstraint()).thenReturn(nc3);

        try {
            when(noc.matches(cc2)).thenReturn(true);
            when(noc.matches(nc2)).thenReturn(true);
            assertTrue(noConstraintTile.isDicePositionable(dice5blue));

            when(cc1.matches(cc2)).thenReturn(false);
            when(cc1.matches(nc2)).thenReturn(true);
            assertFalse(tilePurple.isDicePositionable(dice5blue));

            when(cc1.matches(cc3)).thenReturn(true);
            when(cc1.matches(nc3)).thenReturn(true);
            assertTrue(tilePurple.isDicePositionable(dice3purple));

            when(nc1.matches(nc2)).thenReturn(false);
            when(nc1.matches(cc2)).thenReturn(true);
            assertFalse(tile3.isDicePositionable(dice5blue));

            when(nc1.matches(nc3)).thenReturn(true);
            when(nc1.matches(cc3)).thenReturn(true);
            assertTrue(tile3.isDicePositionable(dice3purple));
        } catch (Exception e) {
            fail("Error not excepted");
        }
    }

    @Test
    public void SetRemoveDiceTest(){
        NoConstraint noc = mock(NoConstraint.class);
        Tile noConstraintTile = new Tile(noc);

        ColorConstraint cc3 = mock(ColorConstraint.class);
        when(cc3.getColor()).thenReturn(Color.PURPLE);
        NumberConstraint nc3 = mock(NumberConstraint.class);
        when(nc3.getNumber()).thenReturn(3);

        Dice dice3purple = mock(Dice.class);
        when(dice3purple.getColorConstraint()).thenReturn(cc3);
        when(dice3purple.getNumberConstraint()).thenReturn(nc3);
        when(dice3purple.getNumber()).thenReturn(3);
        when(dice3purple.getColor()).thenReturn(Color.PURPLE);

        try {
            when(noc.matches(cc3)).thenReturn(true);
            when(noc.matches(nc3)).thenReturn(true);
            noConstraintTile.setDice(dice3purple);
        } catch (Exception e){
            fail("no exception expected");
        }
        assertTrue(dice3purple.equals(noConstraintTile.getDice()));
        Dice removed = noConstraintTile.removeDice();
        assertTrue(noConstraintTile.getDice() == null);
        assertTrue(removed == dice3purple);

    }
}
