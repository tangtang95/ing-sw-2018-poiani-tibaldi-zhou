package org.poianitibaldizhou.sagrada.model;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.exception.CommandNotFoundException;
import org.poianitibaldizhou.sagrada.game.model.cards.ConstraintType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardLanguageParser;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ToolCardLanguageParserTest {

    @DataPoint
    private static ToolCardLanguageParser toolCardLanguageParser;

    @DataPoint
    private static List<ICommand> actualCommands;

    @BeforeClass
    public static void setUpClass() {
        toolCardLanguageParser = new ToolCardLanguageParser();
    }

    @Before
    public void setUp() {
        actualCommands = new ArrayList<ICommand>();
    }

    @After
    public void tearDown() {
        actualCommands = null;
    }

    @AfterClass
    public static void tearDownClass() {
        toolCardLanguageParser = null;
        actualCommands = null;
    }


    @Test
    public void testEveryPresentCommand() {
        List<ICommand> commands = new ArrayList<ICommand>();

        commands.add(new ChooseDice());
        commands.add(new ModifyDiceValue(1));
        commands.add( new RemoveDice(ConstraintType.NONE));
        commands.add(new RemoveDice(ConstraintType.COLOR));
        commands.add(new SwapDice());
        commands.add(new RerollDice());
        commands.add(new PlaceDice(ConstraintType.NONE));
        commands.add(new PlaceDice(ConstraintType.NUMBER));
        commands.add(new PlaceDice(ConstraintType.COLOR));
        commands.add(new PlaceDice(ConstraintType.ISOLATED));
        commands.add(new AddDiceToDraftPool());
        commands.add(new AddDiceToDiceBag());
        commands.add(new DrawDiceFromDicebag());
        commands.add(new ChooseDiceValue());
        commands.add(new RerollDraftPool());
        commands.add(new CheckTurn(2));
        commands.add(new CheckTurn(1));
        commands.add(new CheckBeforeDiceChose());
        commands.add(new CheckTurnEnd());
        commands.add(new SkipTurn(2));
        commands.add(new SkipTurn(1));
        commands.add(new PourOverDice());
        commands.add(new ChooseColorFromRoundTrack());

        String commandsString = new String("Choose dice;Modify dice value by 1;" +
                "Remove dice from schema;Remove dice of a certain color from schema;" +
                "Swap dice with RoundTrack;Reroll dice;Place dice;Place dice ignoring number" +
                " constraints;Place dice ignoring color constraints;Place isolated dice;" +
                "Add dice to DraftPool;Add dice to Dicebag;Draw dice from Dicebag;" +
                "Choose dice value;Reroll DraftPool;Check second turn;Check first turn;" +
                "Check before choose dice;Check turn over;Skip second turn;Skip first turn;" +
                "Pour over dice;Choose color from RoundTrack");

        try {
            actualCommands = toolCardLanguageParser.parseToolCard(commandsString);
            assertEquals("Number of commands not matching", commands.size(), actualCommands.size());
            System.out.println(actualCommands);
            System.out.println(commands);
            for (int i = 0; i < actualCommands.size(); i++) {
                assertEquals("Not matchin commands ("+i+")", commands.get(i), actualCommands.get(i));
            }
        } catch (CommandNotFoundException e) {
            System.out.println(e.getMessage());
            fail("Exception not expected");
        }
    }

    @Test
    public void testNotPresentCommand() {
        String command = new String("thisIsNotACommand");
        List<ICommand> commands = null;
        try {
            commands = toolCardLanguageParser.parseToolCard(command);
            fail("Exception expected");
        } catch (CommandNotFoundException e) {
            assertEquals(commands, null);
        }
    }

    @Test
    public void testSingleCommand() {
        String command = new String("Reroll dice");
        List<ICommand> commands = new ArrayList<ICommand>();
        commands.add(new RerollDice());
        try {
            List<ICommand> actualCommands = toolCardLanguageParser.parseToolCard(command);
            assertEquals("Number of commands not matching",commands.size(),actualCommands.size());
            assertEquals(1, actualCommands.size());
            assertArrayEquals("Not matching commands",commands.toArray(), actualCommands.toArray());
        } catch (CommandNotFoundException e) {
            fail("No exceptione expected");
        }
    }
}
