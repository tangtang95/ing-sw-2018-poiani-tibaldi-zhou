package org.poianitibaldizhou.sagrada.network.protocol;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class ClientGetMessageTest {

    @DataPoint
    public static ClientGetMessage clientGetMessage;

    @Before
    public void setUp() throws Exception {
        clientGetMessage = new ClientGetMessage();

    }

    @Test
    public void getDiceElem() throws IOException {
        String message = "{\"elem\":{\"type\":\"dice\",\"body\":{\"color\":\"YELLOW\",\"value\":3}}}";
        DiceWrapper diceWrapper = new DiceWrapper(ColorWrapper.YELLOW, 3);
        assertEquals(diceWrapper, clientGetMessage.getDiceElem(message));
    }

    @Test
    public void getDiceElemList() throws IOException {
        String message = "{\"elemListKey\":{\"type\":\"collection\",\"body\":[" +
                "{\"type\":\"dice\",\"body\":{\"color\":\"YELLOW\",\"value\":5}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":4}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"RED\",\"value\":3}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"GREEN\",\"value\":2}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"PURPLE\",\"value\":1}}]}}";
        List<DiceWrapper> diceWrappers = new ArrayList<>();
        diceWrappers.add(new DiceWrapper(ColorWrapper.YELLOW,5));
        diceWrappers.add(new DiceWrapper(ColorWrapper.BLUE,4));
        diceWrappers.add(new DiceWrapper(ColorWrapper.RED,3));
        diceWrappers.add(new DiceWrapper(ColorWrapper.GREEN,2));
        diceWrappers.add(new DiceWrapper(ColorWrapper.PURPLE,1));
        assertEquals(diceWrappers, clientGetMessage.getDiceElemList(message));
    }

    @Test
    public void getDice() throws IOException {
        String message = "{\"dice\":{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":2}}}";
        DiceWrapper diceWrapper = new DiceWrapper(ColorWrapper.BLUE, 2);
        assertEquals(diceWrapper, clientGetMessage.getDice(message));
    }

    @Test
    public void getDiceList() throws IOException {
        String message = "{\"diceListKey\":{\"type\":\"collection\",\"body\":[" +
                "{\"type\":\"dice\",\"body\":{\"color\":\"YELLOW\",\"value\":1}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":2}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"RED\",\"value\":3}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"GREEN\",\"value\":4}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"PURPLE\",\"value\":5}}]}}";
        List<DiceWrapper> diceWrappers = new ArrayList<>();
        diceWrappers.add(new DiceWrapper(ColorWrapper.YELLOW,1));
        diceWrappers.add(new DiceWrapper(ColorWrapper.BLUE,2));
        diceWrappers.add(new DiceWrapper(ColorWrapper.RED,3));
        diceWrappers.add(new DiceWrapper(ColorWrapper.GREEN,4));
        diceWrappers.add(new DiceWrapper(ColorWrapper.PURPLE,5));
        assertEquals(diceWrappers, clientGetMessage.getDiceList(message));
    }

    @Test
    public void getUserWrapper() throws IOException {
        String message = "{\"user\":{\"type\":\"user\",\"body\":{\"userName\":\"test\"}}}";
        UserWrapper userWrapper = new UserWrapper("test");
        assertEquals(userWrapper,clientGetMessage.getUserWrapper(message));
    }

    @Test
    public void getTurnUserWrapper() throws IOException {
        String message = "{\"turnUserKey\":{\"type\":\"user\",\"body\":{\"userName\":\"test\"}}}";
        UserWrapper userWrapper = new UserWrapper("test");
        assertEquals(userWrapper,clientGetMessage.getTurnUserWrapper(message));
    }

    @Test
    public void getVictoryPoint() throws IOException {
        String message = "{\"victoryPointMapKey\":{\"type\":\"map\",\"body\":" +
                "{\"{\\\"type\\\":\\\"user\\\",\\\"body\\\":{\\\"userName\\\":\\\"test1\\\"}}\":\"{\\\"type\\\":\\\"integer\\\",\\\"body\\\":\\\"2\\\"}\",\"{\\\"type\\\":\\\"user\\\",\\\"body\\\":{\\\"userName\\\":\\\"test3\\\"}}\":\"{\\\"type\\\":\\\"integer\\\",\\\"body\\\":\\\"20\\\"}\",\"{\\\"type\\\":\\\"user\\\",\\\"body\\\":{\\\"userName\\\":\\\"test2\\\"}}\":\"{\\\"type\\\":\\\"integer\\\",\\\"body\\\":\\\"200\\\"}\",\"{\\\"type\\\":\\\"user\\\",\\\"body\\\":{\\\"userName\\\":\\\"test4\\\"}}\":\"{\\\"type\\\":\\\"integer\\\",\\\"body\\\":\\\"2000\\\"}\"}}}";
        Map<UserWrapper, Integer> victoryMap = new HashMap<>();
        victoryMap.put(new UserWrapper("test1"),2);
        victoryMap.put(new UserWrapper("test2"),200);
        victoryMap.put(new UserWrapper("test3"),20);
        victoryMap.put(new UserWrapper("test4"),2000);
        assertEquals(victoryMap,clientGetMessage.getVictoryPoint(message));
    }

    @Test
    public void getListOfUserWrapper() throws IOException {
        String message = "{\"userListKey\":{\"type\":\"collection\",\"body\":[" +
                "{\"type\":\"user\",\"body\":{\"userName\":\"test1\"}}," +
                "{\"type\":\"user\",\"body\":{\"userName\":\"test2\"}}," +
                "{\"type\":\"user\",\"body\":{\"userName\":\"test3\"}}]}}";
        List<UserWrapper> userWrappers = new ArrayList<>();
        userWrappers.add(new UserWrapper("test1"));
        userWrappers.add(new UserWrapper("test2"));
        userWrappers.add(new UserWrapper("test3"));
        assertEquals(userWrappers,clientGetMessage.getListOfUserWrapper(message));

    }

    @Test
    public void getGameName() throws IOException {
        String message = "{\"gameNameKey\":{\"type\":\"string\",\"body\":\"game\"}}";
        String gameName = "game";
        assertEquals(gameName,clientGetMessage.getGameName(message));
    }

    @Test
    public void getToken() throws IOException {
        String token = "123456789";
        String message = "{\"tokenKey\":{\"type\":\"string\",\"body\":\"123456789\"}}";
        assertEquals(token,clientGetMessage.getToken(message));
    }

    @Test
    public void getTimeout() throws IOException {
        String timeout = "4years";
        String message = "{\"timeoutKey\":{\"type\":\"string\",\"body\":\"4years\"}}";
        assertEquals(timeout,clientGetMessage.getTimeout(message));
    }

    @Test
    public void getValue() throws IOException {
        Integer value = 34;
        String message = "{\"integer\":{\"type\":\"integer\",\"body\":\"34\"}}";
        assertEquals(value,clientGetMessage.getValue(message));
    }

    @Test
    public void getOutcome() throws IOException {
        String outcome = "WIN";
        String message = "{\"outcome\":{\"type\":\"outcome\",\"body\":\"WIN\"}}";
        assertEquals(outcome,clientGetMessage.getOutcome(message));
    }

    @Test
    public void getOldDice() throws IOException {
        String message = "{\"oldDiceKey\":{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":2}}," +
                "\"newDiceKey\":{\"type\":\"dice\",\"body\":{\"color\":\"YELLOW\",\"value\":5}}}";
        DiceWrapper diceWrapper = new DiceWrapper(ColorWrapper.BLUE, 2);
        assertEquals(diceWrapper, clientGetMessage.getOldDice(message));
    }

    @Test
    public void getNewDice() throws IOException {
        String message = "{\"oldDiceKey\":{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":2}}," +
                "\"newDiceKey\":{\"type\":\"dice\",\"body\":{\"color\":\"YELLOW\",\"value\":5}}}";
        DiceWrapper diceWrapper = new DiceWrapper(ColorWrapper.YELLOW, 5);
        assertEquals(diceWrapper, clientGetMessage.getNewDice(message));
    }

    @Test
    public void getPosition() throws IOException {
        String message = "{\"position\":{\"type\":\"position\",\"body\":{\"column\":1,\"row\":1}}}";
        PositionWrapper positionWrapper = new PositionWrapper(1,1);
        assertEquals(positionWrapper,clientGetMessage.getPosition(message));
    }

    @Test
    public void getColorList() throws IOException {
        List<ColorWrapper> colors = new ArrayList<>();
        colors.add(ColorWrapper.YELLOW);
        colors.add(ColorWrapper.GREEN);
        colors.add(ColorWrapper.RED);
        String message = "{\"colorListKey\":{\"type\":\"collection\",\"body\":[" +
                "{\"type\":\"color\",\"body\":\"YELLOW\"}," +
                "{\"type\":\"color\",\"body\":\"GREEN\"}," +
                "{\"type\":\"color\",\"body\":\"RED\"}]}}";
        assertEquals(colors,clientGetMessage.getColorList(message));
    }

    @Test
    public void getDiceValue() throws IOException {
        Integer diceValue = 5;
        String message = "{\"diceValueKey\":{\"type\":\"integer\",\"body\":\"5\"}}";
        assertEquals(diceValue, clientGetMessage.getDiceValue(message));
    }

    @Test
    public void getPublicObjectiveCards() throws IOException {
        List<PublicObjectiveCardWrapper> publicObjectiveCardList = new ArrayList<>();
        publicObjectiveCardList.add(new PublicObjectiveCardWrapper("test1","test12",5));
        publicObjectiveCardList.add(new PublicObjectiveCardWrapper("test2","test21",5));
        publicObjectiveCardList.add(new PublicObjectiveCardWrapper("test3","test23",5));
        String message = "{\"publicObjectiveCardListKey\":{\"type\":\"collection\",\"body\":[" +
                "{\"type\":\"publicObjectiveCard\",\"body\":{\"name\":\"test1\",\"description\":\"test12\",\"cardPoint\":5}}," +
                "{\"type\":\"publicObjectiveCard\",\"body\":{\"name\":\"test2\",\"description\":\"test21\",\"cardPoint\":5}}," +
                "{\"type\":\"publicObjectiveCard\",\"body\":{\"name\":\"test3\",\"description\":\"test23\",\"cardPoint\":5}}]}}";
        assertEquals(publicObjectiveCardList, clientGetMessage.getPublicObjectiveCards(message));
    }

    @Test
    public void getToolCards() throws IOException {
        String message = "{\"toolCardListKey\":{\"type\":\"collection\",\"body\":[" +
                "{\"type\":\"toolCard\",\"body\":{\"cost\":1,\"color\":\"PURPLE\",\"name\":\"Pinza Sgrossatrice\",\"description\":\"Dopo aver scelto un dado, aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6\",\"token\":0}}]}}";
        List<ToolCardWrapper> toolCardWrapperList = new ArrayList<>();
        toolCardWrapperList.add(new ToolCardWrapper("Pinza Sgrossatrice",
                "Dopo aver scelto un dado, aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6",
                ColorWrapper.PURPLE,
                0));
        assertEquals(toolCardWrapperList, clientGetMessage.getToolCards(message));
    }

    @Test
    public void getPrivateObjectiveCards() throws IOException {
        List<PrivateObjectiveCardWrapper> privateObjectiveCardList = new ArrayList<>();
        privateObjectiveCardList.add(new PrivateObjectiveCardWrapper("test", "desc", ColorWrapper.YELLOW));
        String message = "{\"privateObjectiveCardListKey\":{\"type\":\"collection\",\"body\":[" +
                "{\"type\":\"privateObjectiveCard\",\"body\":{\"color\":\"YELLOW\",\"name\":\"test\",\"description\":\"desc\"}}]}}";
        assertEquals(privateObjectiveCardList, clientGetMessage.getPrivateObjectiveCards(message));
    }

    @Test
    public void getFrontBackSchemaCards() throws IOException {
        List<FrontBackSchemaCardWrapper> frontBackSchemaCardList = new ArrayList<>();
        frontBackSchemaCardList.add(new FrontBackSchemaCardWrapper());
        String message = "{\"frontBackSchemaCardListKey\":{\"type\":\"collection\",\"body\":[{\"type\":\"frontBackSchemaCard\",\"body\":{\"type\":\"collection\",\"body\":[]}}]}}";
        assertEquals(frontBackSchemaCardList, clientGetMessage.getFrontBackSchemaCards(message));
    }

    @Test
    public void getSchemaCard() throws IOException {
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
        TileWrapper[][] constraints = new TileWrapper[SchemaCardWrapper.NUMBER_OF_ROWS][SchemaCardWrapper.NUMBER_OF_COLUMNS];
        for (int i = 0; i < SchemaCardWrapper.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCardWrapper.NUMBER_OF_COLUMNS; j++) {
                constraints[i][j] = new TileWrapper(null);
            }
        }
        SchemaCardWrapper emptySchemaCard = new SchemaCardWrapper("test1", 1, constraints);
        assertEquals(emptySchemaCard, clientGetMessage.getSchemaCard(message));
    }

    @Test
    public void getColor() throws IOException {
        String message = "{\"color\":{\"type\":\"color\",\"body\":\"PURPLE\"}}";
        ColorWrapper colorWrapper = ColorWrapper.PURPLE;
        assertEquals(colorWrapper,clientGetMessage.getColor(message));
    }

    @Test
    public void getRoundTrack() throws IOException {
        List<List<DiceWrapper>> round = new ArrayList<>();
        for (int i = 0; i < RoundTrackWrapper.NUMBER_OF_TRACK; i++) {
            round.add(new ArrayList<>());
        }
        RoundTrackWrapper roundTrack = new RoundTrackWrapper(round);

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
        assertEquals(roundTrack, clientGetMessage.getRoundTrack(message));
    }

    @Test
    public void getSchemaCards() throws IOException {
        Map<UserWrapper,SchemaCardWrapper> schemaCardMap = new HashMap<>();
        TileWrapper[][] constraints = new TileWrapper[SchemaCardWrapper.NUMBER_OF_ROWS][SchemaCardWrapper.NUMBER_OF_COLUMNS];
        for (int i = 0; i < SchemaCardWrapper.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCardWrapper.NUMBER_OF_COLUMNS; j++) {
                constraints[i][j] = new TileWrapper(null);
            }
        }
        SchemaCardWrapper emptySchemaCard = new SchemaCardWrapper("test1", 1, constraints);
        schemaCardMap.put(new UserWrapper("test1"), emptySchemaCard);
        schemaCardMap.put(new UserWrapper("test2"), emptySchemaCard);
        schemaCardMap.put(new UserWrapper("test3"), emptySchemaCard);
        schemaCardMap.put(new UserWrapper("test4"), emptySchemaCard);
        String message = "{\"mapSchemaCardKey\":{\"type\":\"map\",\"body\":" +
                "{\"{\\\"type\\\":\\\"user\\\",\\\"body\\\":" +
                "{\\\"userName\\\":\\\"test1\\\"}}\":\"{\\\"type\\\":\\\"schemaCard\\\",\\\"body\\\":" +
                "{\\\"difficulty\\\":1,\\\"name\\\":\\\"test1\\\",\\\"matrix\\\":[[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}],[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}],[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}],[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}]]}}\",\"{\\\"type\\\":\\\"user\\\",\\\"body\\\":{\\\"userName\\\":\\\"test3\\\"}}\":\"{\\\"type\\\":\\\"schemaCard\\\",\\\"body\\\":{\\\"difficulty\\\":1,\\\"name\\\":\\\"test1\\\",\\\"matrix\\\":[[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}],[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}],[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}],[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}]]}}\",\"{\\\"type\\\":\\\"user\\\",\\\"body\\\":{\\\"userName\\\":\\\"test2\\\"}}\":\"{\\\"type\\\":\\\"schemaCard\\\",\\\"body\\\":{\\\"difficulty\\\":1,\\\"name\\\":\\\"test1\\\",\\\"matrix\\\":[[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}],[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}],[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}],[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}]]}}\",\"{\\\"type\\\":\\\"user\\\",\\\"body\\\":{\\\"userName\\\":\\\"test4\\\"}}\":\"{\\\"type\\\":\\\"schemaCard\\\",\\\"body\\\":{\\\"difficulty\\\":1,\\\"name\\\":\\\"test1\\\",\\\"matrix\\\":[[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}],[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}],[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}],[{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}},{\\\"type\\\":\\\"tile\\\",\\\"body\\\":{\\\"constraint\\\":null}}]]}}\"}}}";
        assertEquals(schemaCardMap,clientGetMessage.getSchemaCards(message));
    }

    @Test
    public void getDraftPool() throws IOException {
        Collection<DiceWrapper> diceWrappers = new ArrayList<>();
        DraftPoolWrapper draftPool = new DraftPoolWrapper(diceWrappers);
        String message = "{\"draftPool\":{\"type\":\"draftPool\",\"body\":{\"diceList\":{\"type\":\"collection\",\"body\":[]}}}}";
        assertEquals(draftPool, clientGetMessage.getDraftPool(message));
    }

    @Test
    public void getRoundUser() throws IOException {
        UserWrapper user = new UserWrapper("test");
        String message = "{\"roundUserKey\":{\"type\":\"user\",\"body\":{\"userName\":\"test\"}}}";
        assertEquals(user, clientGetMessage.getRoundUser(message));
    }

    @Test
    public void getMyCoins() throws IOException {
        Integer coins = 56;
        String message = "{\"integer\":{\"type\":\"integer\",\"body\":\"56\"}}";
        assertEquals(coins, clientGetMessage.getMyCoins(message));
    }

    @Test
    public void getPlayersCoins() throws IOException {
        Map<UserWrapper, Integer> coinsMap = new HashMap<>();
        coinsMap.put(new UserWrapper("test1"), 34);
        coinsMap.put(new UserWrapper("test2"), 4);
        coinsMap.put(new UserWrapper("test3"), 3);
        coinsMap.put(new UserWrapper("test4"), 34);
        String message = "{\"mapPlayersCoinsKey\":{\"type\":\"map\",\"body\":" +
                "{\"{\\\"type\\\":\\\"user\\\",\\\"body\\\":{\\\"userName\\\":\\\"test1\\\"}}\":\"{\\\"type\\\":\\\"integer\\\",\\\"body\\\":\\\"34\\\"}\",\"{\\\"type\\\":\\\"user\\\",\\\"body\\\":{\\\"userName\\\":\\\"test3\\\"}}\":\"{\\\"type\\\":\\\"integer\\\",\\\"body\\\":\\\"3\\\"}\",\"{\\\"type\\\":\\\"user\\\",\\\"body\\\":{\\\"userName\\\":\\\"test2\\\"}}\":\"{\\\"type\\\":\\\"integer\\\",\\\"body\\\":\\\"4\\\"}\",\"{\\\"type\\\":\\\"user\\\",\\\"body\\\":{\\\"userName\\\":\\\"test4\\\"}}\":\"{\\\"type\\\":\\\"integer\\\",\\\"body\\\":\\\"34\\\"}\"}}}";
        assertEquals(coinsMap, clientGetMessage.getPlayersCoins(message));
    }

    @Test
    public void getTurnValue() throws IOException {
        Integer value = 12;
        String message = "{\"turnValueKey\":{\"type\":\"integer\",\"body\":\"12\"}}";
        assertEquals(value, clientGetMessage.getTurnValue(message));
    }

    @Test
    public void getCommandFlow() throws IOException {
        String message = "{\"commandFlow\":{\"type\":\"commandFlow\",\"body\":\"NOT_DICE_IN_DRAFT_POOL\"}}";
        assertEquals("NOT_DICE_IN_DRAFT_POOL", clientGetMessage.getCommandFlow(message));
    }

    @Test
    public void hasTerminateGameError() {
        String message = "{\"errorTerminateGameKey\":{\"type\":\"Game has terminated\",\"body\":\"Game has terminated\"}}";
        assertEquals(true, clientGetMessage.hasTerminateGameError(message));
        message = "{}";
        assertEquals(false, clientGetMessage.hasTerminateGameError(message));
    }

    @Test
    public void getToolCard() throws IOException {
        ToolCardWrapper toolCard = new ToolCardWrapper("Pinza Sgrossatrice",
                "Dopo aver scelto un dado, aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6",
                ColorWrapper.PURPLE, 0);
        String message = "{\"toolCard\":{\"type\":\"toolCard\",\"body\":" +
                "{\"cost\":1,\"color\":\"PURPLE\",\"name\":\"Pinza Sgrossatrice\",\"description\":\"Dopo aver scelto un dado, aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6\",\"token\":0}}}";
        assertEquals(toolCard,clientGetMessage.getToolCard(message));
    }

    @Test(expected = Exception.class)
    public void getDiceElem1() throws IOException {
        String message = "{";
        clientGetMessage.getDiceElem(message);
    }

    @Test(expected = Exception.class)
    public void getDiceElemList1() throws IOException {
        String message = "{";
        clientGetMessage.getDiceElemList(message);
    }

    @Test(expected = Exception.class)
    public void getDice1() throws IOException {
        String message = "{";
        clientGetMessage.getDice(message);
    }

    @Test(expected = Exception.class)
    public void getDiceList1() throws IOException {
        String message = "{";
        clientGetMessage.getDiceList(message);
    }

    @Test(expected = Exception.class)
    public void getUserWrapper1() throws IOException {
        String message = "{";
        clientGetMessage.getUserWrapper(message);
    }

    @Test(expected = Exception.class)
    public void getTurnUserWrapper1() throws IOException {
        String message = "{";
        clientGetMessage.getTurnUserWrapper(message);
    }

    @Test(expected = Exception.class)
    public void getVictoryPoint1() throws IOException {
        String message = "{";
        clientGetMessage.getVictoryPoint(message);
    }

    @Test(expected = Exception.class)
    public void getListOfUserWrapper1() throws IOException {
        String message = "{";
        clientGetMessage.getListOfUserWrapper(message);
    }

    @Test(expected = Exception.class)
    public void getGameName1() throws IOException {
        String message = "{";
        clientGetMessage.getGameName(message);
    }

    @Test(expected = Exception.class)
    public void getToken1() throws IOException {
        String message = "{";
        clientGetMessage.getToken(message);
    }

    @Test(expected = Exception.class)
    public void getTimeout1() throws IOException {
        String message = "{";
        clientGetMessage.getTimeout(message);
    }

    @Test(expected = Exception.class)
    public void getValue1() throws IOException {
        String message = "{";
        clientGetMessage.getValue(message);
    }

    @Test(expected = Exception.class)
    public void getOutcome1() throws IOException {
        String message = "{";
        clientGetMessage.getOutcome(message);
    }

    @Test(expected = Exception.class)
    public void getOldDice1() throws IOException {
        String message = "{";
        clientGetMessage.getOldDice(message);
    }

    @Test(expected = Exception.class)
    public void getNewDice1() throws IOException {
        String message = "{";
        clientGetMessage.getNewDice(message);
    }

    @Test(expected = Exception.class)
    public void getPosition1() throws IOException {
        String message = "{";
        clientGetMessage.getPosition(message);
    }

    @Test(expected = Exception.class)
    public void getColorList1() throws IOException {
        String message = "{";
        clientGetMessage.getColorList(message);
    }

    @Test(expected = Exception.class)
    public void getDiceValue1() throws IOException {
        String message = "{";
        clientGetMessage.getDiceValue(message);
    }

    @Test(expected = Exception.class)
    public void getPublicObjectiveCards1() throws IOException {
        String message = "{";
        clientGetMessage.getPublicObjectiveCards(message);
    }

    @Test(expected = Exception.class)
    public void getToolCards1() throws IOException {
        String message = "{";
        clientGetMessage.getToolCards(message);
    }

    @Test(expected = Exception.class)
    public void getPrivateObjectiveCards1() throws IOException {
        String message = "{";
        clientGetMessage.getPrivateObjectiveCards(message);
    }

    @Test(expected = Exception.class)
    public void getFrontBackSchemaCards1() throws IOException {
        String message = "{";
        clientGetMessage.getFrontBackSchemaCards(message);
    }

    @Test(expected = Exception.class)
    public void getSchemaCard1() throws IOException {
        String message = "{";
        clientGetMessage.getSchemaCard(message);
    }

    @Test(expected = Exception.class)
    public void getColor1() throws IOException {
        String message = "{";
        clientGetMessage.getColor(message);
    }

    @Test(expected = Exception.class)
    public void getRoundTrack1() throws IOException {
        String message = "{";
        clientGetMessage.getRoundTrack(message);
    }

    @Test(expected = Exception.class)
    public void getSchemaCards1() throws IOException {
        String message = "{";
        clientGetMessage.getSchemaCards(message);
    }

    @Test(expected = Exception.class)
    public void getDraftPool1() throws IOException {
        String message = "{";
        clientGetMessage.getDraftPool(message);
    }

    @Test(expected = Exception.class)
    public void getRoundUser1() throws IOException {
        String message = "{";
        clientGetMessage.getRoundUser(message);
    }

    @Test(expected = Exception.class)
    public void getMyCoins1() throws IOException {
        String message = "{";
        clientGetMessage.getMyCoins(message);
    }

    @Test(expected = Exception.class)
    public void getPlayersCoins1() throws IOException {
        String message = "{";
        clientGetMessage.getPlayersCoins(message);
    }

    @Test(expected = Exception.class)
    public void getToolCard1() throws IOException {
        String message = "{";
        clientGetMessage.getToolCard(message);
    }

    @Test(expected = Exception.class)
    public void getTurnValue1() throws IOException {
        String message = "{";
        clientGetMessage.getTurnValue(message);
    }

    @Test(expected = Exception.class)
    public void getCommandFlow1() throws IOException {
        String message = "{";
        clientGetMessage.getCommandFlow(message);
    }
}