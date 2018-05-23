package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Node;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.*;
import org.poianitibaldizhou.sagrada.game.model.observers.IToolCardObserver;

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
                "[1-Choose dice][2-Add dice to DraftPool][4-Reroll dice]");
        toolCardSingle = new ToolCard(Color.RED,"testName", "testDescription",
                "[1-Choose dice][2-Add dice to DraftPool][4-Reroll dice]");
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
    public void testEquals() {
        ToolCard toolCard;
        toolCard = new ToolCard(Color.BLUE,"testName", "testDescription",
                "[1-Choose dice][2-Add dice to DraftPool][4-Reroll dice]");
        assertNotEquals(toolCard, toolCardMulti);

        toolCard = new ToolCard(Color.RED,"testName2", "testDescription",
                "[1-Choose dice][2-Add dice to DraftPool][4-Reroll dice]");
        assertNotEquals(toolCard, toolCardMulti);

        toolCard = new ToolCard(Color.RED,"testName", "testDescription2",
                "[1-Choose dice][2-Add dice to DraftPool][4-Reroll dice]");
        assertNotEquals(toolCard, toolCardMulti);

        toolCard = new ToolCard(Color.RED,"testName", "testDescription",
                "[1-Draw dice from Dicebag][2-Add dice to DraftPool][4-Reroll dice]");
        assertNotEquals(toolCard, toolCardMulti);

        toolCard = new ToolCard(Color.RED,"testName", "testDescription",
                "[1-Choose dice][2-Add dice to DraftPool][4-Reroll dice]");
        assertNotEquals(toolCard, toolCardMulti);

        toolCard = new ToolCard(Color.RED,"testName", "testDescription",
                "[1-Choose dice][2-Add dice to DraftPool][4-Reroll dice]");
        assertEquals(toolCard, toolCardMulti);
    }
}
