package org.poianitibaldizhou.sagrada.game.model.players;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.coin.ExpendableDice;
import org.poianitibaldizhou.sagrada.game.model.constraint.IConstraint;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class MultiPlayerTest {
    @Mock
    private User user;

    private SchemaCard schemaCard;

    @Mock
    private PrivateObjectiveCard privateObjectiveCard1;


    private List<PrivateObjectiveCard> privateObjectiveCardList;
    private Player player;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        privateObjectiveCardList = new ArrayList<>();
        privateObjectiveCardList.add(privateObjectiveCard1);
        schemaCard = spy(new SchemaCard("schemaName", 3,
                new IConstraint[SchemaCard.NUMBER_OF_ROWS][SchemaCard.NUMBER_OF_COLUMNS]));
        player = new MultiPlayer(user, schemaCard, privateObjectiveCardList);
    }

    @After
    public void tearDown() throws Exception {
        player = null;
        privateObjectiveCard1 = null;
        privateObjectiveCardList = null;
        schemaCard = null;
        user = null;
    }

    @Test
    public void getVictoryPoints() throws Exception {
        int numberOfEmptyTile = schemaCard.getNumberOfEmptySpaces();
        when(privateObjectiveCard1.getScore(ArgumentMatchers.any(SchemaCard.class))).thenReturn(10);
        assertEquals(10 - numberOfEmptyTile + player.getCoins(), player.getVictoryPoints());
    }

    @Test
    public void newInstanceSucceed() throws Exception {
        Player p = Player.newInstance(player);
        assertTrue(player.equals(p));
    }

    @Test(expected = IllegalArgumentException.class)
    public void newInstanceFailed() throws Exception {
        Player p = MultiPlayer.newInstance(new SinglePlayer(user, new ExpendableDice(mock(Game.class)),
                schemaCard, privateObjectiveCardList));
    }
}