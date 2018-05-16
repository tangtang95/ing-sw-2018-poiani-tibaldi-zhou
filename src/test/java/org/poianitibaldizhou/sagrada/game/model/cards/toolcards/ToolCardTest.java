package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Node;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.dice.DiceRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement.PlacementRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ToolCardTest {
    private ToolCard toolCardSingle;
    private ToolCard toolCardMulti;
    private Node<ICommand> commands;
    private List<IToolCardObserver> observerList;

    @Mock
    private IToolCardObserver observer1;

    @Mock
    private IToolCardObserver observer2;

    @Mock
    private IToolCardObserver observer3;

    @Mock
    private IToolCardObserver observer4;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        toolCardMulti = new ToolCard(Color.RED,"testName", "testDescription",
                "[1-Choose dice][2-Add dice to DraftPool][4-Reroll dice]", false);
        toolCardSingle = new ToolCard(Color.RED,"testName", "testDescription",
                "[1-Choose dice][2-Add dice to DraftPool][4-Reroll dice]", true);
        commands = new Node<>(new ChooseDice());
        commands.addAtIndex(new AddDiceToDraftPool(), 2);
        commands.addAtIndex(new RerollDice(), 4);

        observerList = new ArrayList<>();
        observerList.add(observer1);
        observerList.add(observer2);
        observerList.add(observer3);
        observerList.add(observer4);
        for(IToolCardObserver observer : observerList) {
            toolCardMulti.attachToolCardObserver(observer);
            toolCardSingle.attachToolCardObserver(observer);
        }
        toolCardMulti.detachToolCardObserver(observer4);
        toolCardSingle.detachToolCardObserver(observer4);
        observerList.remove(observer4);
    }

    @After
    public void tearDown() throws Exception {
        toolCardSingle = null;
        toolCardMulti = null;
    }

    @Test
    public void testUseCardSingle() {
        assertEquals(commands, toolCardSingle.useCard());
        for(IToolCardObserver observer : observerList)
            verify(observer, times(1)).onCardDestroy();
    }

    @Test
    public void testUseCardMulti() {
        assertEquals(commands, toolCardMulti.useCard());

        for(IToolCardObserver observer : observerList)
            verify(observer, times(1)).onTokenChange(toolCardMulti.getTokens());
    }

    @Test
    public void testEquals() {
        ToolCard toolCard;
        toolCard = new ToolCard(Color.BLUE,"testName", "testDescription",
                "[1-Choose dice][2-Add dice to DraftPool][4-Reroll dice]", false);
        assertNotEquals(toolCard, toolCardMulti);

        toolCard = new ToolCard(Color.RED,"testName2", "testDescription",
                "[1-Choose dice][2-Add dice to DraftPool][4-Reroll dice]", false);
        assertNotEquals(toolCard, toolCardMulti);

        toolCard = new ToolCard(Color.RED,"testName", "testDescription2",
                "[1-Choose dice][2-Add dice to DraftPool][4-Reroll dice]", false);
        assertNotEquals(toolCard, toolCardMulti);

        toolCard = new ToolCard(Color.RED,"testName", "testDescription",
                "[1-Draw dice from Dicebag][2-Add dice to DraftPool][4-Reroll dice]", false);
        assertNotEquals(toolCard, toolCardMulti);

        toolCard = new ToolCard(Color.RED,"testName", "testDescription",
                "[1-Choose dice][2-Add dice to DraftPool][4-Reroll dice]", true);
        assertNotEquals(toolCard, toolCardMulti);

        toolCard = new ToolCard(Color.RED,"testName", "testDescription",
                "[1-Choose dice][2-Add dice to DraftPool][4-Reroll dice]", false);
        assertEquals(toolCard, toolCardMulti);
    }
}
