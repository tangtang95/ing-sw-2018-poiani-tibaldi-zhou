package org.poianitibaldizhou.sagrada.game.model.players;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.coin.ExpendableDice;
import org.poianitibaldizhou.sagrada.game.model.constraint.IConstraint;
import org.poianitibaldizhou.sagrada.game.model.observers.IPlayerObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.ISchemaCardObserver;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PlayerTest {


    @Mock
    private User user;

    private SchemaCard schemaCard;

    @Mock
    private PrivateObjectiveCard privateObjectiveCard1, privateObjectiveCard2;


    private List<PrivateObjectiveCard> privateObjectiveCardList;
    private Player player;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        privateObjectiveCardList = new ArrayList<>();
        privateObjectiveCardList.add(privateObjectiveCard1);
        privateObjectiveCardList.add(privateObjectiveCard2);
        schemaCard = spy(new SchemaCard("schemaName", 3,
                new IConstraint[SchemaCard.NUMBER_OF_ROWS][SchemaCard.NUMBER_OF_COLUMNS]));
        player = new MultiPlayer(user, schemaCard, privateObjectiveCardList);
    }

    @After
    public void tearDown() throws Exception {
        player = null;
        privateObjectiveCard1 = null;
        privateObjectiveCard2 = null;
        privateObjectiveCardList = null;
        schemaCard = null;
        user = null;
    }

    @Test
    public void getterTest() throws Exception {
        SchemaCard sc = new SchemaCard("schemaName", 3,
                new IConstraint[SchemaCard.NUMBER_OF_ROWS][SchemaCard.NUMBER_OF_COLUMNS]);
        Player p = new MultiPlayer(user, sc, privateObjectiveCardList);
        assertEquals(user, p.getUser());
        assertEquals(3, p.getCoins());
        assertFalse(sc == p.getSchemaCard());
        assertEquals(sc, p.getSchemaCard());
        assertEquals(0, p.getObserverMap().size());
        assertEquals(0, p.getSchemaCardObserverMap().size());
        assertTrue(p.getPrivateObjectiveCards().containsAll(privateObjectiveCardList));

        when(privateObjectiveCard1.getScore(ArgumentMatchers.any(SchemaCard.class))).thenReturn(10);
        assertEquals(10, p.getScoreFromPrivateCard());

        assertEquals(privateObjectiveCard1, p.getPrivateObjectiveCard());

    }

    @Test
    public void isCardUsableWithFavorToken() throws Exception {
        ToolCard toolCard = mock(ToolCard.class);
        when(toolCard.getCost()).thenReturn(1);
        assertTrue(player.isCardUsable(toolCard));
    }

    @Test
    public void attachObserverTest() throws Exception {
        assertEquals(0, player.getObserverMap().size());
        IPlayerObserver obs1 = mock(IPlayerObserver.class);
        player.attachObserver("obs1", obs1);
        assertEquals(1, player.getObserverMap().size());
        assertTrue(obs1 == player.getObserverMap().get("obs1"));
    }

    @Test
    public void attachSchemaCardObserverTest() throws Exception {
        assertEquals(0, player.getSchemaCardObserverMap().size());
        ISchemaCardObserver obs1 = mock(ISchemaCardObserver.class);
        player.attachSchemaCardObserver("obs1", obs1);
        assertEquals(1, player.getSchemaCardObserverMap().size());
        assertTrue(obs1 == player.getSchemaCardObserverMap().get("obs1"));
    }

    /**
     * Test deep copy inside setSchemaCard
     */
    @Test
    public void setSchemaCardTest() throws Exception {
        SchemaCard sc = new SchemaCard("newName", 2,
                new IConstraint[SchemaCard.NUMBER_OF_ROWS][SchemaCard.NUMBER_OF_COLUMNS]);
        assertNotEquals(sc, player.getSchemaCard());
        player.setSchemaCard(sc);
        assertEquals(sc, player.getSchemaCard());
        sc.setDice(new Dice(3, Color.RED), 0,0);
        assertNotEquals(sc, player.getSchemaCard());
    }

    @Test
    public void removeCoinsTest() throws Exception {
        for (int i = 0; i < 4; i++) {
            player.attachObserver("obs" + i, mock(IPlayerObserver.class));
        }

        int beforeCoin = player.getCoins();
        player.removeCoins(1);
        assertEquals(beforeCoin - 1, player.getCoins());
        for (IPlayerObserver obs: player.getObserverMap().values()) {
            verify(obs).onFavorTokenChange(1);
        }
    }

    @Test
    public void placeDice() throws Exception {
        Dice dice = new Dice(2, Color.RED);
        Position position = new Position(0,0);
        player.placeDice(dice, position);
        assertEquals(dice, player.getSchemaCard().getDice(position));
    }

    @Test
    public void setPrivateObjectiveCardSuceeedTest() throws Exception {
        assertEquals(privateObjectiveCard1, player.getPrivateObjectiveCard());
        player.setPrivateObjectiveCard(privateObjectiveCard2);
        assertEquals(privateObjectiveCard2, player.getPrivateObjectiveCard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setPrivateObjectiveCardFailedTest() throws Exception {
        player.setPrivateObjectiveCard(mock(PrivateObjectiveCard.class));
    }

    @Test
    public void equalsTest() throws Exception {
        //Different user
        User u = mock(User.class);
        Player p = new MultiPlayer(u, schemaCard, privateObjectiveCardList);
        assertFalse(player.equals(p));

        //Different schemaCard
        SchemaCard sc = new SchemaCard("other", 3
                , new IConstraint[SchemaCard.NUMBER_OF_ROWS][SchemaCard.NUMBER_OF_COLUMNS]);
        p = new MultiPlayer(user, sc, privateObjectiveCardList);
        assertFalse(player.equals(p));

        //Different token
        p = new MultiPlayer(user, schemaCard, privateObjectiveCardList);
        p.removeCoins(1);
        assertFalse(player.equals(p));

        //Different class player
        p = new SinglePlayer(user, new ExpendableDice(mock(Game.class)), schemaCard, privateObjectiveCardList);
        assertFalse(player.equals(p));

        //Different class
        assertFalse(player.equals(p));

        //Equals
        p = new MultiPlayer(user, schemaCard, privateObjectiveCardList);
        assertTrue(player.equals(p));
    }

    @Test
    public void hashCodeTest() throws Exception {
        //Different user
        User u = mock(User.class);
        Player p = new MultiPlayer(u, schemaCard, privateObjectiveCardList);
        assertNotEquals(p.hashCode(), player.hashCode());

        //Different schemaCard
        SchemaCard sc = new SchemaCard("other", 3
                , new IConstraint[SchemaCard.NUMBER_OF_ROWS][SchemaCard.NUMBER_OF_COLUMNS]);
        p = new MultiPlayer(user, sc, privateObjectiveCardList);
        assertNotEquals(p.hashCode(), player.hashCode());

        //Different token
        p = new MultiPlayer(user, schemaCard, privateObjectiveCardList);
        p.removeCoins(1);
        assertNotEquals(p.hashCode(), player.hashCode());

        //Different class player
        p = new SinglePlayer(user, new ExpendableDice(mock(Game.class)), schemaCard, privateObjectiveCardList);
        assertNotEquals(p.hashCode(), player.hashCode());

        //Different class
        assertNotEquals(user.hashCode(), player.hashCode());

        //Equals
        p = new MultiPlayer(user, schemaCard, privateObjectiveCardList);
        assertEquals(p.hashCode(), player.hashCode());
    }

}
