package org.poianitibaldizhou.sagrada.game.model;

import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PlayerTest {


    private String token1 = "1234567890";
    private String token2 = "0987654321";

    @Mock
    private ToolCard toolCard;
    @Mock
    private SchemaCard schemaCard;
    @Mock
    private ICoin coin;
    @Mock
    private Game game;

    private Player player;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
    }


    @Test
    public void useCardTest() throws Exception {
        when(coin.getCoins()).thenReturn(4);
        player = new Player(token1, coin);
        player.useCard(toolCard);
        verify(coin).use(toolCard);
        verify(toolCard).invokeCommands(player, game);
    }

    @Test
    public void endTurn() throws Exception {
    }

    @Test
    public void equalsTest() throws Exception {
    }

    @Test
    public void hashCodeTest() throws Exception {
    }

    @Test
    public void getScoreFromPrivateCard() throws Exception {
    }

    @Test
    public void getScore() throws Exception {
    }

    @Test
    public void getFavorTokens() throws Exception {
    }

    @Test
    public void newInstance() throws Exception {
    }
}
