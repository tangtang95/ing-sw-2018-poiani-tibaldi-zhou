package org.poianitibaldizhou.sagrada.game.model.cards;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NoConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NumberConstraint;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.poianitibaldizhou.sagrada.game.model.Color.PURPLE;

public class TileTest {

    @Test
    public void dicePositionableTest(){
        NoConstraint noc = mock(NoConstraint.class);

        ColorConstraint cc1 = mock(ColorConstraint.class);
        when(cc1.getColor()).thenReturn(PURPLE);

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
        when(cc3.getColor()).thenReturn(PURPLE);
        NumberConstraint nc3 = mock(NumberConstraint.class);
        when(nc3.getNumber()).thenReturn(3);

        Dice dice3purple = mock(Dice.class);
        when(dice3purple.getColorConstraint()).thenReturn(cc3);
        when(dice3purple.getNumberConstraint()).thenReturn(nc3);

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
    }

    @Test
    public void SetRemoveDiceTest(){
        NoConstraint noc = mock(NoConstraint.class);
        Tile noConstraintTile = new Tile(noc);

        ColorConstraint cc3 = mock(ColorConstraint.class);
        when(cc3.getColor()).thenReturn(PURPLE);
        NumberConstraint nc3 = mock(NumberConstraint.class);
        when(nc3.getNumber()).thenReturn(3);

        Dice dice3purple = mock(Dice.class);
        when(dice3purple.getColorConstraint()).thenReturn(cc3);
        when(dice3purple.getNumberConstraint()).thenReturn(nc3);
        when(dice3purple.getNumber()).thenReturn(3);
        when(dice3purple.getColor()).thenReturn(PURPLE);

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

    @Test
    public void equalsTest() {
        NoConstraint noc = mock(NoConstraint.class);

        ColorConstraint cc1 = mock(ColorConstraint.class);
        when(cc1.getColor()).thenReturn(PURPLE);

        NumberConstraint nc1 = mock(NumberConstraint.class);
        when(nc1.getNumber()).thenReturn(3);

        Tile noConstraintTile1 = new Tile(noc);
        Tile noConstraintTile2 = new Tile(noc);
        Tile tile3 = new Tile(nc1);

        //Define one dice with 5 and blue
        ColorConstraint cc2 = mock(ColorConstraint.class);
        when(cc2.getColor()).thenReturn(Color.BLUE);
        NumberConstraint nc2 = mock(NumberConstraint.class);
        when(nc2.getNumber()).thenReturn(5);

        Dice dice5blue = mock(Dice.class);
        when(dice5blue.getColorConstraint()).thenReturn(cc2);
        when(dice5blue.getNumberConstraint()).thenReturn(nc2);

        //Define one dice with 3 and purple
        ColorConstraint cc3 = mock(ColorConstraint.class);
        when(cc3.getColor()).thenReturn(PURPLE);
        NumberConstraint nc3 = mock(NumberConstraint.class);
        when(nc3.getNumber()).thenReturn(3);

        Dice dice3purple = mock(Dice.class);
        when(dice3purple.getColorConstraint()).thenReturn(cc3);
        when(dice3purple.getNumberConstraint()).thenReturn(nc3);

        //Testing
        assertTrue(noConstraintTile1.equals(noConstraintTile1));
        assertFalse(noConstraintTile1.equals(Dice.class));
        assertTrue(noConstraintTile1.equals(noConstraintTile2));
        assertFalse(noConstraintTile1.equals(tile3));

        try {
            when(noc.matches(cc2)).thenReturn(true);
            when(noc.matches(nc2)).thenReturn(true);
            noConstraintTile1.setDice(dice5blue);
            noConstraintTile2.setDice(dice5blue);
        } catch (RuleViolationException e) {
            fail("no exception expected");
        }

        assertTrue(noConstraintTile1.equals(noConstraintTile2));
        assertFalse(noConstraintTile1.equals(tile3));
        assertFalse(tile3.equals(noConstraintTile1));
    }

    @Test
    public void testHashCode(){
        NoConstraint noc = mock(NoConstraint.class);

        ColorConstraint cc1 = mock(ColorConstraint.class);
        when(cc1.getColor()).thenReturn(PURPLE);

        NumberConstraint nc1 = mock(NumberConstraint.class);
        when(nc1.getNumber()).thenReturn(3);

        Tile noConstraintTile1 = new Tile(noc);
        Tile noConstraintTile2 = new Tile(noc);
        Tile tile3 = new Tile(nc1);

        assertEquals(noConstraintTile1.hashCode(), noConstraintTile2.hashCode());
        assertNotEquals(noConstraintTile1.hashCode(), tile3.hashCode());
    }

    @Test
    public void newInstanceTest(){
        NoConstraint noc = mock(NoConstraint.class);

        ColorConstraint cc1 = mock(ColorConstraint.class);
        when(cc1.getColor()).thenReturn(PURPLE);

        NumberConstraint nc1 = mock(NumberConstraint.class);
        when(nc1.getNumber()).thenReturn(3);

        Tile noConstraintTile1 = new Tile(noc);
        Tile tile3 = new Tile(nc1);

        assertEquals(noConstraintTile1, Tile.newInstance(noConstraintTile1));
        assertNotEquals(noConstraintTile1, Tile.newInstance(tile3));
        assertNotEquals(tile3, Tile.newInstance(noConstraintTile1));
    }

    @Test
    public void testToJSON(){
        JSONParser parser = new JSONParser();
        JSONObject test = null;
        try {
            test = (JSONObject) parser.parse("{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":3}}");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        NoConstraint noc = mock(NoConstraint.class);

        ColorConstraint cc1 = mock(ColorConstraint.class);
        when(cc1.getColor()).thenReturn(PURPLE);

        NumberConstraint nc1 = mock(NumberConstraint.class);
        when(nc1.getNumber()).thenReturn(3);

        Tile noConstraintTile = new Tile(noc);
        Tile tilePurple = new Tile(cc1);
        Tile tile3 = new Tile(nc1);
        Tile tileDice = new Tile(noc);

        //define dice 3 blue
        ColorConstraint cc2 = mock(ColorConstraint.class);
        when(cc2.getColor()).thenReturn(Color.BLUE);
        NumberConstraint nc2 = mock(NumberConstraint.class);
        when(nc2.getNumber()).thenReturn(3);

        Dice dice3blue = mock(Dice.class);
        when(dice3blue.getColorConstraint()).thenReturn(cc2);
        when(dice3blue.getNumberConstraint()).thenReturn(nc2);
        when(dice3blue.toJSON()).thenReturn(test);

        when(noc.matches(cc2)).thenReturn(true);
        when(noc.matches(nc2)).thenReturn(true);
        assertTrue(noConstraintTile.isDicePositionable(dice3blue));

        try {
            tileDice.setDice(dice3blue);
        } catch (RuleViolationException e) {
            fail("no exception expected");
        }

        String message = "{\"type\":\"tile\",\"body\":{\"constraint\":null}}";
        assertTrue(message.equals(noConstraintTile.toJSON().toJSONString()));
        message = "{\"type\":\"tile\",\"body\":{\"constraint\":\"PURPLE\"}}";
        assertTrue(message.equals(tilePurple.toJSON().toJSONString()));
        message = "{\"type\":\"tile\",\"body\":{\"constraint\":3}}";
        assertTrue(message.equals(tile3.toJSON().toJSONString()));
        message = "{\"type\":\"tile\",\"body\":{\"dice\":{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":3}},\"constraint\":null}}";
        assertTrue(message.equals(tileDice.toJSON().toJSONString()));
    }

    @Test
    public void testToObject(){
        JSONParser parser = new JSONParser();
        JSONObject test = null;
        try {
            test = (JSONObject) parser.parse("{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":3}}");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        NoConstraint noc = mock(NoConstraint.class);

        ColorConstraint cc1 = mock(ColorConstraint.class);
        when(cc1.getColor()).thenReturn(PURPLE);

        NumberConstraint nc1 = mock(NumberConstraint.class);
        when(nc1.getNumber()).thenReturn(3);

        Tile noConstraintTile = new Tile(noc);
        Tile tilePurple = new Tile(cc1);
        Tile tile3 = new Tile(nc1);
        Tile tileDice = new Tile(noc);

        //define dice 3 blue
        ColorConstraint cc2 = mock(ColorConstraint.class);
        when(cc2.getColor()).thenReturn(Color.BLUE);
        NumberConstraint nc2 = mock(NumberConstraint.class);
        when(nc2.getNumber()).thenReturn(3);

        Dice dice3blue = mock(Dice.class);
        when(dice3blue.getColorConstraint()).thenReturn(cc2);
        when(dice3blue.getNumberConstraint()).thenReturn(nc2);

        when(noc.matches(cc2)).thenReturn(true);
        when(noc.matches(nc2)).thenReturn(true);
        assertTrue(noConstraintTile.isDicePositionable(dice3blue));


        try {
            tileDice.setDice(dice3blue);
        } catch (RuleViolationException e) {
            fail("no exception expected");
        }
        String message1 = "{\"constraint\":null}";
        String message2 = "{\"constraint\":\"PURPLE\"}";
        String message3 = "{\"constraint\":3}";
        String message4 = "{\"dice\":{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":3}},\"constraint\":null}";

        JSONParser jsonParser = new JSONParser();
        try {
            assertTrue(noConstraintTile.toObject((JSONObject) jsonParser.parse(message1)).equals(noConstraintTile));
            assertTrue(tile3.toObject((JSONObject) jsonParser.parse(message3)).equals(tile3));
            assertTrue(tilePurple.toObject((JSONObject) jsonParser.parse(message2)).equals(tilePurple));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}
