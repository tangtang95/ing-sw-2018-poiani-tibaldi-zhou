package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.*;
import org.poianitibaldizhou.sagrada.network.observers.fakeobservers.ToolCardFakeObserver;
import org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces.IToolCardFakeObserver;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.*;
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
        observerList.forEach((key, value) -> toolCard.attachToolCardObserver(key, value));
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
    public void attachObserverTest(){
        ToolCard card = new ToolCard(Color.BLUE,"name", "description",
                "[1-Choose dice][2-Add dice to DraftPool][4-Reroll dice]");
        card.attachToolCardObserver("obs1", observer1);
        assertEquals(observer1, card.getObserverMap().get("obs1"));
    }

    @Test
    public void detachObserverTest() {
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
    public void getterTest()  {
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
    public void addTokens(){
        int actualTokens = toolCard.getTokens();
        int numberOfFavorTokenToAdd = 1;
        toolCard.addTokens(numberOfFavorTokenToAdd);
        assertEquals(actualTokens + numberOfFavorTokenToAdd, toolCard.getTokens());
        for (IToolCardFakeObserver obs: toolCard.getObserverMap().values()) {
            verify(obs).onTokenChange(numberOfFavorTokenToAdd);
        }
    }

    @Test
    public void destroyToolCard(){
        toolCard.destroyToolCard();
        for (IToolCardFakeObserver obs: toolCard.getObserverMap().values()) {
            verify(obs).onCardDestroy();
        }
    }

    @Test
    public void newInstance() {
        // TODO
    }

    @Test
    public void hashCodeTest()  {
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

    @Test
    public void testToJSON(){
        String message = "{\"type\":\"toolCard\",\"body\":" +
                "{\"cost\":1,\"color\":\"RED\",\"name\":\"testName\",\"description\":\"testDescription\",\"token\":0}}";
        assertTrue(message.equals(toolCard.toJSON().toJSONString()));

    }

    @Test
    public void testToObject(){
        ToolCard trueToolCard = new ToolCard(Color.BLUE,
                "Martelletto",
                "Tira nuovamente tutti i dadi della riserva. Questa carta può essere usata solo durante il tuo secondo turno, prima di scegliere il secondo dado",
                "[1-Check second turn][2-Check before choose dice][4-Reroll DraftPool][8-CA]");
        String message = "{\"name\":\"Martelletto\",\"token\":0}";
        JSONParser jsonParser = new JSONParser();
        try {
            assertTrue((Objects.requireNonNull(ToolCard.toObject((JSONObject) jsonParser.parse(message)))).equals(trueToolCard));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testToObjectException () throws Exception{
        String message = "{\"name\":\"Martelletto\",\"token\":\"aaa\"}";
        JSONParser jsonParser = new JSONParser();

        assertNull(ToolCard.toObject((JSONObject) jsonParser.parse(message)));
    }

    @Test
    public void testNewInstance() {
        assertEquals(toolCard, ToolCard.newInstance(toolCard));

        ToolCard tempToolCard = new ToolCard(Color.GREEN, "name", "descr", "[1-CA]");

        assertNotEquals(toolCard, tempToolCard);
    }
}
