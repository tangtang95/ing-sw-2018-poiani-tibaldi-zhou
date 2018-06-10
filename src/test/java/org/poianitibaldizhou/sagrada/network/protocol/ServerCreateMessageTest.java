package org.poianitibaldizhou.sagrada.network.protocol;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.FrontBackSchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.Tile;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.*;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.players.Outcome;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.util.*;

import static org.junit.Assert.*;

public class ServerCreateMessageTest {

    @DataPoint
    public static ServerCreateMessage serverCreateMessage;

    @Before
    public void setUp() throws Exception {
        serverCreateMessage = new ServerCreateMessage();
    }

    @Test
    public void createDiceMessage() {
        Dice dice = new Dice(2, Color.BLUE);
        String message = "{\"dice\":{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":2}}}";
        assertEquals(message,serverCreateMessage.createDiceMessage(dice).buildMessage());
    }

    @Test
    public void getErrorMessage() {
        String message = "{\"errorKey\":\"Can't get because you're not logged or you are not part of the specified game or the specified does not exist or the requested object does not exist\"}";
        assertEquals(message,serverCreateMessage.getErrorMessage());
    }

    @Test
    public void getGameTerminatedErrorMessage() {
        String message = "{\"errorTerminateGameKey\":{\"type\":\"Game has terminated\",\"body\":\"Game has terminated\"}}";
        assertEquals(message,serverCreateMessage.getGameTerminatedErrorMessage());
    }

    @Test
    public void reconnectErrorMessage() {
        String message = "{\"errorKey\":\"Error reconnecting\"}";
        assertEquals(message,serverCreateMessage.reconnectErrorMessage());
    }

    @Test
    public void createTokenMessage() {
        String token = "123456789";
        String message = "{\"tokenKey\":{\"type\":\"string\",\"body\":\"123456789\"}}";
        assertEquals(message,serverCreateMessage.createTokenMessage(token).buildMessage());
    }

    @Test
    public void createDiceSwapMessage() {
        Dice oldDice = new Dice(2,Color.BLUE);
        Dice newDice = new Dice(5, Color.YELLOW);
        String message = "{\"oldDiceKey\":{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":2}}," +
                "\"newDiceKey\":{\"type\":\"dice\",\"body\":{\"color\":\"YELLOW\",\"value\":5}}}";
        assertEquals(message,serverCreateMessage.createDiceSwapMessage(oldDice,newDice).buildMessage());
    }

    @Test
    public void createDiceList() {
        List<Dice> diceList = new ArrayList<>();
        diceList.add(new Dice(1, Color.YELLOW));
        diceList.add(new Dice(2, Color.BLUE));
        diceList.add(new Dice(3, Color.RED));
        diceList.add(new Dice(4, Color.GREEN));
        diceList.add(new Dice(5, Color.PURPLE));
        String message = "{\"diceListKey\":{\"type\":\"collection\",\"body\":[" +
                "{\"type\":\"dice\",\"body\":{\"color\":\"YELLOW\",\"value\":1}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":2}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"RED\",\"value\":3}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"GREEN\",\"value\":4}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"PURPLE\",\"value\":5}}]}}";
        assertEquals(message,serverCreateMessage.createDiceList(diceList).buildMessage());
    }

    @Test
    public void createTimeoutMessage() {
        String timeout = "4years";
        String message = "{\"timeoutKey\":{\"type\":\"string\",\"body\":\"4years\"}}";
        assertEquals(message,serverCreateMessage.createTimeoutMessage(timeout).buildMessage());
    }

    @Test
    public void createElem() {
        Dice dice = new Dice(3,Color.BLUE);
        String message = "{\"elem\":{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":3}}}";
        assertEquals(message,serverCreateMessage.createElem(dice).buildMessage());
    }

    @Test
    public void createElemList() {
        List<Dice> diceList = new ArrayList<>();
        diceList.add(new Dice(5, Color.YELLOW));
        diceList.add(new Dice(4, Color.BLUE));
        diceList.add(new Dice(3, Color.RED));
        diceList.add(new Dice(2, Color.GREEN));
        diceList.add(new Dice(1, Color.PURPLE));
        String message = "{\"elemListKey\":{\"type\":\"collection\",\"body\":[" +
                "{\"type\":\"dice\",\"body\":{\"color\":\"YELLOW\",\"value\":5}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":4}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"RED\",\"value\":3}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"GREEN\",\"value\":2}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"PURPLE\",\"value\":1}}]}}";
        assertEquals(message,serverCreateMessage.createElemList(diceList).buildMessage());
    }

    @Test
    public void createUserList() {
        List<User> userList = new ArrayList<>();
        userList.add(new User("test1","1"));
        userList.add(new User("test2","2"));
        userList.add(new User("test3","3"));
        String message = "{\"userListKey\":{\"type\":\"collection\",\"body\":[" +
                "{\"type\":\"user\",\"body\":{\"userName\":\"test1\"}}," +
                "{\"type\":\"user\",\"body\":{\"userName\":\"test2\"}}," +
                "{\"type\":\"user\",\"body\":{\"userName\":\"test3\"}}]}}";
        assertEquals(message,serverCreateMessage.createUserList(userList).buildMessage());
    }

    @Test
    public void createPublicObjectiveCardList() {
        List<PublicObjectiveCard> publicObjectiveCardList = new ArrayList<>();
        publicObjectiveCardList.add(new DiagonalPublicObjectiveCard("test1","test12",5,
                ObjectiveCardType.COLOR));
        publicObjectiveCardList.add(new ColumnPublicObjectiveCard("test2","test21",5,
                ObjectiveCardType.NUMBER));
        publicObjectiveCardList.add(new RowPublicObjectiveCard("test3","test23",5,
                ObjectiveCardType.COLOR));
        String message = "{\"publicObjectiveCardListKey\":{\"type\":\"collection\",\"body\":[" +
                "{\"type\":\"publicObjectiveCard\",\"body\":{\"name\":\"test1\",\"description\":\"test12\",\"cardPoint\":5}}," +
                "{\"type\":\"publicObjectiveCard\",\"body\":{\"name\":\"test2\",\"description\":\"test21\",\"cardPoint\":5}}," +
                "{\"type\":\"publicObjectiveCard\",\"body\":{\"name\":\"test3\",\"description\":\"test23\",\"cardPoint\":5}}]}}";
        assertEquals(message,serverCreateMessage.createPublicObjectiveCardList(publicObjectiveCardList).buildMessage());
    }

    @Test
    public void createToolCardList() {
        List<ToolCard> toolCardList = new ArrayList<>();
        toolCardList.add(new ToolCard(Color.PURPLE,"Pinza Sgrossatrice",
                "Dopo aver scelto un dado, aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6",
                "[1-Choose dice][2-Remove dice from DraftPool][4-Modify dice value by 1][8-Place new dice][16-CA]"));
        String message = "{\"toolCardListKey\":{\"type\":\"collection\",\"body\":[" +
                "{\"type\":\"toolCard\",\"body\":{\"cost\":1,\"color\":\"PURPLE\",\"name\":\"Pinza Sgrossatrice\",\"description\":\"Dopo aver scelto un dado, aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6\",\"token\":0}}]}}";
        assertEquals(message,serverCreateMessage.createToolCardList(toolCardList).buildMessage());
    }

    @Test
    public void createPrivateObjectiveCardList() {
        List<PrivateObjectiveCard> privateObjectiveCardList = new ArrayList<>();
        privateObjectiveCardList.add(new PrivateObjectiveCard("test", "desc", new ColorConstraint(Color.YELLOW)));
        String message = "{\"privateObjectiveCardListKey\":{\"type\":\"collection\",\"body\":[" +
                "{\"type\":\"privateObjectiveCard\",\"body\":{\"color\":\"YELLOW\",\"name\":\"test\",\"description\":\"desc\"}}]}}";
        assertEquals(message,serverCreateMessage.createPrivateObjectiveCardList(privateObjectiveCardList).buildMessage());
    }

    @Test
    public void createFrontBackSchemaCardList() {
        List<FrontBackSchemaCard> frontBackSchemaCardList = new ArrayList<>();
        frontBackSchemaCardList.add(new FrontBackSchemaCard());
        String message = "{\"frontBackSchemaCardListKey\":{\"type\":\"collection\",\"body\":[{\"type\":\"frontBackSchemaCard\",\"body\":{\"type\":\"collection\",\"body\":[]}}]}}";
        assertEquals(message,serverCreateMessage.createFrontBackSchemaCardList(frontBackSchemaCardList).buildMessage());
    }

    @Test
    public void createMessageValue() {
        int value = 34;
        String message = "{\"integer\":{\"type\":\"integer\",\"body\":\"34\"}}";
        assertEquals(message,serverCreateMessage.createMessageValue(value).buildMessage());
    }

    @Test
    public void createTurnValueMessage() {
        int value = 12;
        String message = "{\"turnValueKey\":{\"type\":\"integer\",\"body\":\"12\"}}";
        assertEquals(message,serverCreateMessage.createTurnValueMessage(value).buildMessage());
    }

    @Test
    public void createOutcomeMessage() {
        Outcome outcome = Outcome.WIN;
        String message = "{\"outcome\":{\"type\":\"outcome\",\"body\":\"WIN\"}}";
        assertEquals(message,serverCreateMessage.createOutcomeMessage(outcome).buildMessage());
    }

    @Test
    public void createUserMessage() {
        User user = new User("test", "12345678");
        String message = "{\"user\":{\"type\":\"user\",\"body\":{\"userName\":\"test\"}}}";
        assertEquals(message,serverCreateMessage.createUserMessage(user).buildMessage());
    }

    @Test
    public void createRoundUserMessage() {
        User user = new User("test", "12345678");
        String message = "{\"roundUserKey\":{\"type\":\"user\",\"body\":{\"userName\":\"test\"}}}";
        assertEquals(message,serverCreateMessage.createRoundUserMessage(user).buildMessage());
    }

    @Test
    public void createTurnUserMessage() {
        User user = new User("test", "12345678");
        String message = "{\"turnUserKey\":{\"type\":\"user\",\"body\":{\"userName\":\"test\"}}}";
        assertEquals(message,serverCreateMessage.createTurnUserMessage(user).buildMessage());
    }

    @Test
    public void createBooleanMessage() {
        String message = "{\"boolean\":{\"type\":\"boolean\",\"body\":\"true\"}}";
        assertEquals(message,serverCreateMessage.createBooleanMessage(true).buildMessage());
    }

    @Test
    public void createPositionMessage() {
        Position position = new Position(1,1);
        String message = "{\"position\":{\"type\":\"position\",\"body\":{\"column\":1,\"row\":1}}}";
        assertEquals(message,serverCreateMessage.createPositionMessage(position).buildMessage());
    }

    @Test
    public void createVictoryPointMapMessage() {
        Map<User, Integer> victoryMap = new HashMap<>();
        victoryMap.put(new User("test1", "1"),2);
        victoryMap.put(new User("test2", "2"),200);
        victoryMap.put(new User("test3", "3"),20);
        victoryMap.put(new User("test4", "4"),2000);
        String message = "{\"victoryPointMapKey\":{\"type\":\"map\",\"body\":" +
                "{\"{\\\"type\\\":\\\"user\\\",\\\"body\\\":{\\\"userName\\\":\\\"test1\\\"}}\":\"{\\\"type\\\":\\\"integer\\\",\\\"body\\\":\\\"2\\\"}\",\"{\\\"type\\\":\\\"user\\\",\\\"body\\\":{\\\"userName\\\":\\\"test3\\\"}}\":\"{\\\"type\\\":\\\"integer\\\",\\\"body\\\":\\\"20\\\"}\",\"{\\\"type\\\":\\\"user\\\",\\\"body\\\":{\\\"userName\\\":\\\"test2\\\"}}\":\"{\\\"type\\\":\\\"integer\\\",\\\"body\\\":\\\"200\\\"}\",\"{\\\"type\\\":\\\"user\\\",\\\"body\\\":{\\\"userName\\\":\\\"test4\\\"}}\":\"{\\\"type\\\":\\\"integer\\\",\\\"body\\\":\\\"2000\\\"}\"}}}";
        assertEquals(message,serverCreateMessage.createVictoryPointMapMessage(victoryMap).buildMessage());
    }

    @Test
    public void createColorListMessage() {
        List<Color> colors = new ArrayList<>();
        colors.add(Color.YELLOW);
        colors.add(Color.GREEN);
        colors.add(Color.RED);
        String message = "{\"colorListKey\":{\"type\":\"collection\",\"body\":[" +
                "{\"type\":\"color\",\"body\":\"YELLOW\"}," +
                "{\"type\":\"color\",\"body\":\"GREEN\"}," +
                "{\"type\":\"color\",\"body\":\"RED\"}]}}";
        assertEquals(message,serverCreateMessage.createColorListMessage(colors).buildMessage());
    }

    @Test
    public void createDiceValueMessage() {
        int diceValue = 5;
        String message = "{\"diceValueKey\":{\"type\":\"integer\",\"body\":\"5\"}}";
        assertEquals(message,serverCreateMessage.createDiceValueMessage(diceValue).buildMessage());
    }

    @Test
    public void createRoundTrackMessage() {
        RoundTrack roundTrack = new RoundTrack();
        String message = "{\"roundTrack\":{\"type\":\"roundTrack\",\"body\":{\"roundList\":[" +
                "{\"diceList\":{\"type\":\"collection\",\"body\":[]}," +
                "\"round\":0},{\"diceList\":{\"type\":\"collection\",\"body\":[]}," +
                "\"round\":1},{\"diceList\":{\"type\":\"collection\",\"body\":[]}," +
                "\"round\":2},{\"diceList\":{\"type\":\"collection\",\"body\":[]}," +
                "\"round\":3},{\"diceList\":{\"type\":\"collection\",\"body\":[]}," +
                "\"round\":4},{\"diceList\":{\"type\":\"collection\",\"body\":[]}," +
                "\"round\":5},{\"diceList\":{\"type\":\"collection\",\"body\":[]}," +
                "\"round\":6},{\"diceList\":{\"type\":\"collection\",\"body\":[]}," +
                "\"round\":7},{\"diceList\":{\"type\":\"collection\",\"body\":[]}," +
                "\"round\":8},{\"diceList\":{\"type\":\"collection\",\"body\":[]}," +
                "\"round\":9}]}}}";
        assertEquals(message,serverCreateMessage.createRoundTrackMessage(roundTrack).buildMessage());
    }

    @Test
    public void createColorMessage() {
        String message = "{\"color\":{\"type\":\"color\",\"body\":\"PURPLE\"}}";
        assertEquals(message,serverCreateMessage.createColorMessage(Color.PURPLE).buildMessage());
    }

    @Test
    public void createCommandFlowMessage() {
        String message = "{\"commandFlow\":{\"type\":\"commandFlow\",\"body\":\"NOT_DICE_IN_DRAFTPOOL\"}}";
        assertEquals(message,serverCreateMessage.createCommandFlowMessage(CommandFlow.NOT_DICE_IN_DRAFTPOOL).buildMessage());
    }

    @Test
    public void createGameNameMessage() {
        String gameName = "game";
        String message = "{\"gameNameKey\":{\"type\":\"string\",\"body\":\"game\"}}";
        assertEquals(message,serverCreateMessage.createGameNameMessage(gameName).buildMessage());
    }

    @Test
    public void createToolCardMessage() {
        ToolCard toolCard = new ToolCard(Color.PURPLE,"Pinza Sgrossatrice",
                "Dopo aver scelto un dado, aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6",
                "[1-Choose dice][2-Remove dice from DraftPool][4-Modify dice value by 1][8-Place new dice][16-CA]");
        String message = "{\"toolCard\":{\"type\":\"toolCard\",\"body\":" +
                "{\"cost\":1,\"color\":\"PURPLE\",\"name\":\"Pinza Sgrossatrice\",\"description\":\"Dopo aver scelto un dado, aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6\",\"token\":0}}}";
        assertEquals(message,serverCreateMessage.createToolCardMessage(toolCard).buildMessage());
    }

    @Test
    public void createSchemaCardMessage() {
        Tile[][] constraints = new Tile[SchemaCard.NUMBER_OF_ROWS][SchemaCard.NUMBER_OF_COLUMNS];
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                constraints[i][j] = new Tile(null);
            }
        }
        SchemaCard emptySchemaCard = new SchemaCard("test1", 1, constraints);
        String message = "{\"schemaCard\":{\"type\":\"schemaCard\",\"body\":" +
                "{\"difficulty\":1,\"name\":\"test1\",\"matrix\":[" +
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
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}" +
                ",{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]," +
                "[{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}," +
                "{\"type\":\"tile\",\"body\":{\"constraint\":null}}]]}}}";

        assertEquals(message,serverCreateMessage.createSchemaCardMessage(emptySchemaCard).buildMessage());
    }

    @Test
    public void createSchemaCardMapMessage() {
        Map<User,SchemaCard> schemaCardMap = new HashMap<>();
        Tile[][] constraints = new Tile[SchemaCard.NUMBER_OF_ROWS][SchemaCard.NUMBER_OF_COLUMNS];
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                constraints[i][j] = new Tile(null);
            }
        }
        SchemaCard emptySchemaCard = new SchemaCard("test1", 1, constraints);
        schemaCardMap.put(new User("test1", "test1"), emptySchemaCard);
        schemaCardMap.put(new User("test2", "test2"), emptySchemaCard);
        schemaCardMap.put(new User("test3", "test3"), emptySchemaCard);
        schemaCardMap.put(new User("test4", "test4"), emptySchemaCard);
        String message = "{\"mapSchemaCardKey\":{\"type\":\"map\",\"body\":" +
                "{\"{\\\"type\\\":\\\"user\\\",\\\"body\\\":" +
                "{\\\"userName\\\":\\\"test1\\\"}}\":\"{\\\"type\\\":\\\"schemaCard\\\",\\\"body\\\":" +
                "{\\\"difficulty\\\":1,\\\"name\\\":\\\"test1\\\",\\\"matrix\\\":[[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}],[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}],[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}],[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}]]}}\",\"{\\\"type\\\":\\\"user\\\",\\\"body\\\":{\\\"userName\\\":\\\"test3\\\"}}\":\"{\\\"type\\\":\\\"schemaCard\\\",\\\"body\\\":{\\\"difficulty\\\":1,\\\"name\\\":\\\"test1\\\",\\\"matrix\\\":[[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}],[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}],[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}],[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}]]}}\",\"{\\\"type\\\":\\\"user\\\",\\\"body\\\":{\\\"userName\\\":\\\"test2\\\"}}\":\"{\\\"type\\\":\\\"schemaCard\\\",\\\"body\\\":{\\\"difficulty\\\":1,\\\"name\\\":\\\"test1\\\",\\\"matrix\\\":[[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}],[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}],[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}],[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}]]}}\",\"{\\\"type\\\":\\\"user\\\",\\\"body\\\":{\\\"userName\\\":\\\"test4\\\"}}\":\"{\\\"type\\\":\\\"schemaCard\\\",\\\"body\\\":{\\\"difficulty\\\":1,\\\"name\\\":\\\"test1\\\",\\\"matrix\\\":[[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}],[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}],[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}],[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}]]}}\"}}}";
        assertEquals(message,serverCreateMessage.createSchemaCardMapMessage(schemaCardMap).buildMessage());
    }

    @Test
    public void createDraftPoolMessage() {
        DraftPool draftPool = new DraftPool();
        String message = "{\"draftPool\":{\"type\":\"draftPool\",\"body\":{\"diceList\":{\"type\":\"collection\",\"body\":[]}}}}";
        assertEquals(message,serverCreateMessage.createDraftPoolMessage(draftPool).buildMessage());
    }

    @Test
    public void createCoinsMessage() {
        int coins = 56;
        String message = "{\"integer\":{\"type\":\"integer\",\"body\":\"56\"}}";
        assertEquals(message,serverCreateMessage.createCoinsMessage(coins).buildMessage());
    }

    @Test
    public void createPlayersCoinsMessage() {
        Map<User, Integer> coinsMap = new HashMap<>();
        coinsMap.put(new User("test1", "test1"), 34);
        coinsMap.put(new User("test2", "test2"), 4);
        coinsMap.put(new User("test3", "test3"), 3);
        coinsMap.put(new User("test4", "test4"), 34);
        String message = "{\"mapPlayersCoinsKey\":{\"type\":\"map\",\"body\":" +
                "{\"{\\\"type\\\":\\\"user\\\",\\\"body\\\":{\\\"userName\\\":\\\"test1\\\"}}\":\"{\\\"type\\\":\\\"integer\\\",\\\"body\\\":\\\"34\\\"}\",\"{\\\"type\\\":\\\"user\\\",\\\"body\\\":{\\\"userName\\\":\\\"test3\\\"}}\":\"{\\\"type\\\":\\\"integer\\\",\\\"body\\\":\\\"3\\\"}\",\"{\\\"type\\\":\\\"user\\\",\\\"body\\\":{\\\"userName\\\":\\\"test2\\\"}}\":\"{\\\"type\\\":\\\"integer\\\",\\\"body\\\":\\\"4\\\"}\",\"{\\\"type\\\":\\\"user\\\",\\\"body\\\":{\\\"userName\\\":\\\"test4\\\"}}\":\"{\\\"type\\\":\\\"integer\\\",\\\"body\\\":\\\"34\\\"}\"}}}";
        assertEquals(message,serverCreateMessage.createPlayersCoinsMessage(coinsMap).buildMessage());
    }

}