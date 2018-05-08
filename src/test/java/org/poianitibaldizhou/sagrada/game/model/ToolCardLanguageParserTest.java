package org.poianitibaldizhou.sagrada.game.model;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.game.model.cards.DiceConstraintType;
import org.poianitibaldizhou.sagrada.game.model.cards.TileConstraintType;
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
        actualCommands = new ArrayList<>();
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
        List<ICommand> commands = new ArrayList<>();

        commands.add(new ChooseDice());
        commands.add(new ModifyDiceValueByDelta(1));
        commands.add(new RemoveDice(TileConstraintType.NONE));
        commands.add(new RemoveDice(TileConstraintType.COLOR));
        commands.add(new SwapDiceWithRoundTrack());
        commands.add(new RerollDice());
        commands.add(new PlaceDice(TileConstraintType.NUMBER_COLOR, DiceConstraintType.NORMAL));
        commands.add(new PlaceDice(TileConstraintType.COLOR, DiceConstraintType.NORMAL));
        commands.add(new PlaceDice(TileConstraintType.NUMBER, DiceConstraintType.NORMAL));
        commands.add(new PlaceDice(TileConstraintType.NUMBER_COLOR, DiceConstraintType.ISOLATED));
        commands.add(new AddDiceToDraftPool());
        commands.add(new AddDiceToDiceBag());
        commands.add(new DrawDiceFromDicebag());
        commands.add(new ModifyDiceValue());
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
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            fail("Exception not expected");
        }
    }

    @Test
    public void testNotPresentCommand() {
        String command = "thisIsNotACommand";
        List<ICommand> commands = null;
        try {
            commands = toolCardLanguageParser.parseToolCard(command);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals(commands, null);
        }
    }

    @Test
    public void testSingleCommand() {
        String command = new String("Reroll dice");
        List<ICommand> commands = new ArrayList<>();
        commands.add(new RerollDice());
        try {
            List<ICommand> actualCommands = toolCardLanguageParser.parseToolCard(command);
            assertEquals("Number of commands not matching",commands.size(),actualCommands.size());
            assertEquals(1, actualCommands.size());
            assertArrayEquals("Not matching commands",commands.toArray(), actualCommands.toArray());
        } catch (IllegalArgumentException e) {
            fail("No exceptione expected");
        }
    }
}

