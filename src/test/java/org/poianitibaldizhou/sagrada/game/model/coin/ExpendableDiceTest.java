package org.poianitibaldizhou.sagrada.game.model.coin;

import org.junit.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.coin.ExpendableDice;
import org.poianitibaldizhou.sagrada.game.model.coin.ICoin;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Dependency test with:
 * - ColorConstraint
 */
public class ExpendableDiceTest {

    @Mock
    private Game game;
    @Mock
    private ToolCard toolCard;
    @Mock
    private DraftPool draftPool;
    @Mock
    private Dice dice1, dice2, dice3, dice4;

    private List<Dice> diceDraftPool;
    private ICoin expendableDice;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        diceDraftPool = new ArrayList<>();
        diceDraftPool.add(dice1);
        diceDraftPool.add(dice2);
        diceDraftPool.add(dice3);
        diceDraftPool.add(dice4);
        expendableDice = new ExpendableDice(game);
        when(draftPool.getDices()).thenReturn(diceDraftPool);
        when(game.getDraftPool()).thenReturn(draftPool);
    }

    @After
    public void tearDown() {
        expendableDice = null;
        draftPool = null;
        toolCard = null;
        diceDraftPool = null;
        dice1 = null;
        dice2 = null;
        dice3 = null;
        dice4 = null;
    }

    /**
     * Test isCardUsable when there is one dice with the same color of the toolCard
     */
    @Test
    public void isCardUsableTrueTest() throws Exception {
        when(toolCard.getColor()).thenReturn(Color.PURPLE);
        when(dice1.getColorConstraint()).thenReturn(new ColorConstraint(Color.PURPLE));
        when(dice2.getColorConstraint()).thenReturn(new ColorConstraint(Color.BLUE));
        when(dice3.getColorConstraint()).thenReturn(new ColorConstraint(Color.RED));
        when(dice4.getColorConstraint()).thenReturn(new ColorConstraint(Color.RED));
        assertEquals(true, expendableDice.isCardUsable(toolCard));
    }

    /**
     * Test isCardUsable when there is no dice with the same color of the toolCard
     */
    @Test
    public void isCardUsableFalseTest() throws Exception {
        when(toolCard.getColor()).thenReturn(Color.PURPLE);
        when(dice1.getColorConstraint()).thenReturn(new ColorConstraint(Color.BLUE));
        when(dice2.getColorConstraint()).thenReturn(new ColorConstraint(Color.BLUE));
        when(dice3.getColorConstraint()).thenReturn(new ColorConstraint(Color.RED));
        when(dice4.getColorConstraint()).thenReturn(new ColorConstraint(Color.RED));
        assertEquals(false, expendableDice.isCardUsable(toolCard));
    }

    @Test(expected = IllegalStateException.class)
    public void getCoins() throws Exception {
        expendableDice.getCoins();
    }

    @Test(expected = IllegalStateException.class)
    public void removeCoins() throws Exception {
        expendableDice.removeCoins(2);
    }
}
