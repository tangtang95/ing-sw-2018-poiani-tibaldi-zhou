package org.poianitibaldizhou.sagrada.network.protocol;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.Tile;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.UseCardAction;

import java.io.IOException;
import static org.junit.Assert.*;

public class ServerGetMessageTest {

    @DataPoint
    public static ServerGetMessage serverGetMessage;

    @Before
    public void setUp() throws Exception {
        serverGetMessage = new ServerGetMessage();
    }

    @Test
    public void getToken() throws IOException {
        String token = "123456789";
        String message = "{\"tokenKey\":{\"type\":\"string\",\"body\":\"123456789\"}}";
        assertEquals(token, serverGetMessage.getToken(message));
    }

    @Test
    public void getGameName() throws IOException {
        String gameName = "Emperor";
        String message = "{\"gameNameKey\":{\"type\":\"string\",\"body\":\"Emperor\"}}";
        assertEquals(gameName, serverGetMessage.getGameName(message));
    }

    @Test
    public void getSchemaCard() throws IOException {
        Tile[][] constraints = new Tile[SchemaCard.NUMBER_OF_ROWS][SchemaCard.NUMBER_OF_COLUMNS];
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                constraints[i][j] = new Tile(null);
            }
        }
        SchemaCard emptySchemaCard = new SchemaCard("test1", 1, constraints);
        String message = "{\"schemaCard\":{\"type\":\"schemaCard\",\"body\":" +
                "{\"difficulty\":1,\"name\":\"test1\",\"matrix\":" +
                "[[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]," +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]," +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]," +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]]}}}";
        assertEquals(emptySchemaCard, serverGetMessage.getSchemaCard(message));
    }

    @Test
    public void getPosition() throws IOException {
        Position position = new Position(1,1);
        String message = "{\"position\":{\"type\":\"position\",\"body\":{\"column\":1,\"row\":1}}}";
        assertEquals(position, serverGetMessage.getPosition(message));
    }

    @Test
    public void getDice() throws IOException {
        Dice dice = new Dice(5, Color.BLUE);
        String message = "{\"dice\":{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":5}}}";
        assertEquals(dice, serverGetMessage.getDice(message));
    }

    @Test
    public void getToolCard() throws IOException {
        String message = "{\"toolCard\":{\"type\":\"toolCard\",\"body\":{\"name\":\"Pinza Sgrossatrice\",\"token\":0}}}";
        ToolCard toolCard = new ToolCard(Color.PURPLE,"Pinza Sgrossatrice",
                "Dopo aver scelto un dado, aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6",
                "[1-Choose dice][2-Remove dice from DraftPool][4-Modify dice value by 1][8-Place new dice][16-CA]");
        assertEquals(toolCard, serverGetMessage.getToolCard(message));
    }

    @Test
    public void getActionCommand() throws IOException {
        String message = "{\"actionKey\":{\"type\":\"useToolCardAction\",\"body\":{}}}";
        IActionCommand iActionCommand = new UseCardAction();
        assertEquals(iActionCommand, serverGetMessage.getActionCommand(message));
    }

    @Test
    public void getInteger() throws IOException {
        Integer value = 5;
        String message = "{\"integer\":{\"type\":\"integer\",\"body\":\"5\"}}";
        assertEquals(value, serverGetMessage.getInteger(message));
    }

    @Test
    public void getColor() throws IOException {
        String message = "{\"color\":{\"type\":\"color\",\"body\":\"GREEN\"}}";
        Color color = Color.GREEN;
        assertEquals(color, serverGetMessage.getColor(message));
    }

    @Test
    public void getUserName() throws IOException {
        String username = "test";
        String message = "{\"usernameKey\":{\"type\":\"string\",\"body\":\"test\"}}";
        assertEquals(username, serverGetMessage.getUserName(message));
    }

    @Test
    public void getBoolean() throws IOException {
        String message = "{\"boolean\":{\"type\":\"boolean\",\"body\":\"true\"}}";
        assertEquals(true, serverGetMessage.getBoolean(message));
    }

    @Test
    public void getPrivateObjectiveCard() throws IOException {
        String message = "{\"privateObjectiveCard\":{\"type\":\"privateObjectiveCard\",\"body\":{\"color\":\"RED\",\"name\":\"Sfumature Rosse - Privata\",\"description\":\"test\"}}}";
        PrivateObjectiveCard privateObjectiveCard= new PrivateObjectiveCard("Sfumature Rosse - Privata", "Somma dei valori su tutti i dadi rossi", Color.RED);
        assertEquals(privateObjectiveCard, serverGetMessage.getPrivateObjectiveCard(message));
    }

    @Test(expected = Exception.class)
    public void getToken1() throws IOException {
        String message ="{";
        serverGetMessage.getToken(message);
    }

    @Test(expected = Exception.class)
    public void getGameName1() throws IOException {
        String message ="{";
        serverGetMessage.getGameName(message);
    }

    @Test(expected = Exception.class)
    public void getSchemaCard1() throws IOException {
        String message ="{";
        serverGetMessage.getSchemaCard(message);
    }

    @Test(expected = Exception.class)
    public void getPosition1() throws IOException {
        String message ="{";
        serverGetMessage.getPosition(message);
    }

    @Test(expected = Exception.class)
    public void getDice1() throws IOException {
        String message ="{";
        serverGetMessage.getDice(message);
    }

    @Test(expected = Exception.class)
    public void getToolCard1() throws IOException {
        String message ="{";
        serverGetMessage.getToolCard(message);
    }

    @Test(expected = Exception.class)
    public void getActionCommand1() throws IOException {
        String message ="{";
        serverGetMessage.getActionCommand(message);
    }

    @Test(expected = Exception.class)
    public void getPrivateObjectiveCard1() throws IOException {
        String message ="{";
        serverGetMessage.getPrivateObjectiveCard(message);
    }

    @Test(expected = Exception.class)
    public void getInteger1() throws IOException {
        String message ="{";
        serverGetMessage.getInteger(message);
    }

    @Test(expected = Exception.class)
    public void getColor1() throws IOException {
        String message ="{";
        serverGetMessage.getColor(message);
    }

    @Test(expected = Exception.class)
    public void getUserName1() throws IOException {
        String message ="{";
        serverGetMessage.getUserName(message);
    }

    @Test(expected = Exception.class)
    public void getBoolean1() throws IOException {
        String message ="{";
        serverGetMessage.getBoolean(message);
    }
}