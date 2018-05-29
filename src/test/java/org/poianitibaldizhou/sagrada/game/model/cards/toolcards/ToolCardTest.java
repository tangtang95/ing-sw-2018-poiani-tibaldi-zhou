package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.*;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.ToolCardFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IToolCardObserver;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.verify;

public class ToolCardTest {


    private ToolCard toolCard;
    private Node<ICommand> commands;
    private Map<String, ToolCardFakeObserver> observerList;

    @Mock
    private ToolCardFakeObserver observer1, observer2, observer3, observer4;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        toolCard = new ToolCard(Color.RED,"testName", "testDescription",
                "[1-Choose dice][2-Add dice to DraftPool][4-Reroll dice]");
        commands = new Node<>(new ChooseDice());
        commands.addAtIndex(new AddDiceToDraftPool(), 2);
        commands.addAtIndex(new RerollDice(), 4);

        observerList = new HashMap<>();
        observerList.put("obs1",observer1);
        observerList.put("obs2",observer2);
        observerList.put("obs3",observer3);
        observerList.put("obs4",observer4);
        observerList.forEach((key, value) -> {
            toolCard.attachToolCardObserver(key, value);
        });
        toolCard.detachToolCardObserver("obs4");
        observerList.remove("obs4");
    }

    @After
    public void tearDown() throws Exception {
        toolCard = null;
        commands = null;
        observerList = null;
    }

    @Test
    public void attachObserverTest() throws Exception {
        ToolCard card = new ToolCard(Color.BLUE,"name", "description",
                "[1-Choose dice][2-Add dice to DraftPool][4-Reroll dice]");
        card.attachToolCardObserver("obs1", observer1);
        assertEquals(observer1, card.getObserverMap().get("obs1"));
    }

    @Test
    public void detachObserverTest() throws Exception {
        ToolCard card = new ToolCard(Color.BLUE,"name", "description",
                "[1-Choose dice][2-Add dice to DraftPool][4-Reroll dice]");
        card.attachToolCardObserver("obs1", observer1);
        card.attachToolCardObserver("obs2", observer2);
        card.detachToolCardObserver("obs1");
        assertEquals(false, card.getObserverMap().containsKey("obs1"));
        assertEquals(true, card.getObserverMap().containsKey("obs2"));
        assertEquals(1, card.getObserverMap().size());
    }

    @Test
    public void getterTest() throws Exception {
        ToolCard card = new ToolCard(Color.BLUE,"name", "description",
                "[1-Choose dice][2-Add dice to DraftPool][4-Reroll dice]");
        Node<ICommand> commands = new Node<>(new ChooseDice());
        commands.addAtIndex(new AddDiceToDraftPool(), 2);
        commands.addAtIndex(new RerollDice(), 4);
        assertEquals("name", card.getName());
        assertEquals("description", card.getDescription());
        assertEquals(Color.BLUE, card.getColor());
        assertEquals(commands, card.getCommands());
        assertEquals(0, card.getTokens());
        assertEquals(0, card.getObserverMap().size());
    }

    @Test
    public void addTokens() throws Exception {
        int actualTokens = toolCard.getTokens();
        int numberOfFavorTokenToAdd = 1;
        toolCard.addTokens(numberOfFavorTokenToAdd);
        assertEquals(actualTokens + numberOfFavorTokenToAdd, toolCard.getTokens());
        for (IToolCardObserver obs: toolCard.getObserverMap().values()) {
            verify(obs).onTokenChange(numberOfFavorTokenToAdd);
        }
    }

    @Test
    public void destroyToolCard() throws Exception {
        toolCard.destroyToolCard();
        for (IToolCardObserver obs: toolCard.getObserverMap().values()) {
            verify(obs).onCardDestroy();
        }
    }

    @Test
    public void newInstance() throws Exception {
        // TODO
    }

    @Test
    public void hashCodeTest() throws Exception {
        ToolCard other;
        other = new ToolCard(Color.BLUE,"testName", "testDescription",
                "[1-Choose dice][2-Add dice to DraftPool][4-Reroll dice]");
        assertNotEquals(other.hashCode(), toolCard.hashCode());

        other = new ToolCard(Color.RED,"testName2", "testDescription",
                "[1-Choose dice][2-Add dice to DraftPool][4-Reroll dice]");
        assertNotEquals(other.hashCode(), toolCard.hashCode());

        other = new ToolCard(Color.RED,"testName", "testDescription2",
                "[1-Choose dice][2-Add dice to DraftPool][4-Reroll dice]");
        assertNotEquals(other.hashCode(), toolCard.hashCode());

        other = new ToolCard(Color.RED,"testName", "testDescription",
                "[1-Draw dice from Dicebag][2-Add dice to DraftPool][4-Reroll dice]");
        assertNotEquals(other.hashCode(), toolCard.hashCode());

        other = new ToolCard(Color.RED,"testName", "testDescription",
                "[1-Choose dice][2-Add dice to DraftPool][4-Reroll dice]");
        assertEquals(other.hashCode(), toolCard.hashCode());
    }

    @Test
    public void testEquals() {
        ToolCard other;
        other = new ToolCard(Color.BLUE,"testName", "testDescription",
                "[1-Choose dice][2-Add dice to DraftPool][4-Reroll dice]");
        assertNotEquals(other, toolCard);

        other = new ToolCard(Color.RED,"testName2", "testDescription",
                "[1-Choose dice][2-Add dice to DraftPool][4-Reroll dice]");
        assertNotEquals(other, toolCard);

        other = new ToolCard(Color.RED,"testName", "testDescription2",
                "[1-Choose dice][2-Add dice to DraftPool][4-Reroll dice]");
        assertNotEquals(other, toolCard);

        other = new ToolCard(Color.RED,"testName", "testDescription",
                "[1-Draw dice from Dicebag][2-Add dice to DraftPool][4-Reroll dice]");
        assertNotEquals(other, toolCard);

        other = new ToolCard(Color.RED,"testName", "testDescription",
                "[1-Choose dice][2-Add dice to DraftPool][4-Reroll dice]");
        assertEquals(other, toolCard);

    }
}
