package org.poianitibaldizhou.sagrada.network.protocol;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.ColorWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;

import java.io.IOException;

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
        String message = "{\"dice\":{\"type\":\"dice\",\"body\":{\"color\":\"YELLOW\",\"value\":3}}}";
        DiceWrapper diceWrapper = new DiceWrapper(ColorWrapper.YELLOW, 3);
        assertEquals(diceWrapper, clientGetMessage.getDice(message));
    }

    @Test
    public void getDiceElemList() {
    }

    @Test
    public void getDice() {
    }

    @Test
    public void getDiceList() {
    }

    @Test
    public void getUserWrapper() {
    }

    @Test
    public void getTurnUserWrapper() {
    }

    @Test
    public void getVictoryPoint() {
    }

    @Test
    public void getListOfUserWrapper() {
    }

    @Test
    public void getGameName() {
    }

    @Test
    public void getToken() {
    }

    @Test
    public void getTimeout() {
    }

    @Test
    public void getValue() {
    }

    @Test
    public void getOutcome() {
    }

    @Test
    public void getOldDice() {
    }

    @Test
    public void getNewDice() {
    }

    @Test
    public void getPosition() {
    }

    @Test
    public void getColorList() {
    }

    @Test
    public void getDiceValue() {
    }

    @Test
    public void getPublicObjectiveCards() {
    }

    @Test
    public void getToolCards() {
    }

    @Test
    public void getPrivateObjectiveCards() {
    }

    @Test
    public void getFrontBackSchemaCards() {
    }

    @Test
    public void getSchemaCard() {
    }

    @Test
    public void getColor() {
    }

    @Test
    public void getRoundTrack() {
    }

    @Test
    public void getSchemaCards() {
    }

    @Test
    public void getDraftPool() {
    }

    @Test
    public void getRoundUser() {
    }

    @Test
    public void getMyCoins() {
    }

    @Test
    public void getPlayersCoins() {
    }

    @Test
    public void getTurnValue() {
    }

    @Test
    public void getCommandFlow() {
    }

    @Test
    public void hasTerminateGameError() {
    }
}