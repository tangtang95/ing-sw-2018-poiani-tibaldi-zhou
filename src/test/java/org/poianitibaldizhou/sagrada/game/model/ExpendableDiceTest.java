package org.poianitibaldizhou.sagrada.game.model;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.exception.NoCoinsExpendableException;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Dependency test with:
 * - ColorConstraint
 */
public class ExpendableDiceTest {

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
        expendableDice = new ExpendableDice(draftPool);
        when(draftPool.getDices()).thenReturn(diceDraftPool);
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
     * Test use method when it is possible to use a dice
     */
    @Test
    public void useTest() throws Exception {
        when(toolCard.getColor()).thenReturn(Color.PURPLE);
        when(dice1.getColorConstraint()).thenReturn(new ColorConstraint(Color.PURPLE));
        when(dice2.getColorConstraint()).thenReturn(new ColorConstraint(Color.BLUE));
        when(dice3.getColorConstraint()).thenReturn(new ColorConstraint(Color.RED));
        when(dice4.getColorConstraint()).thenReturn(new ColorConstraint(Color.RED));
        expendableDice.use(toolCard);
        verify(draftPool).useDice(dice1);
    }

    /**
     * Test NoCoinsExpendableException thrown by use method
     */
    @Test
    public void useTestException() throws Exception {
        when(toolCard.getColor()).thenReturn(Color.PURPLE);
        when(dice1.getColorConstraint()).thenReturn(new ColorConstraint(Color.BLUE));
        when(dice2.getColorConstraint()).thenReturn(new ColorConstraint(Color.BLUE));
        when(dice3.getColorConstraint()).thenReturn(new ColorConstraint(Color.RED));
        when(dice4.getColorConstraint()).thenReturn(new ColorConstraint(Color.RED));
        try {
            expendableDice.use(toolCard);
            fail("exception expected");
        }catch (NoCoinsExpendableException e){
            assertNotEquals("exception is null", null, e);
        }
    }

    @Test
    public void getCoins() throws Exception {
        assertEquals(diceDraftPool.size() ,expendableDice.getCoins());
    }
}
