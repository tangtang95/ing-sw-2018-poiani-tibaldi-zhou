package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.dice.DiceRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement.PlacementRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.*;

import static org.junit.Assert.*;

public class ToolCardLanguageParserTest {

    @DataPoint
    private static ToolCardLanguageParser toolCardLanguageParser;

    @DataPoint
    private static Node<ICommand> actualCommands;

    @BeforeClass
    public static void setUpClass() {
        toolCardLanguageParser = new ToolCardLanguageParser();
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
        Node<ICommand> commands = new Node<>(new ChooseDice());

        commands.addAtIndex(new ModifyDiceValueByDelta(1), 2);
        commands.addAtIndex(new RemoveDice(PlacementRestrictionType.NONE),4);
        commands.addAtIndex(new RemoveDice(PlacementRestrictionType.COLOR),8);
        commands.addAtIndex(new SwapDiceWithRoundTrack(), 16);
        commands.addAtIndex(new RerollDice(), 32);
        commands.addAtIndex(new PlaceDice(DiceRestrictionType.NORMAL, PlacementRestrictionType.NUMBER_COLOR, false), 64);
        commands.addAtIndex(new PlaceDice(DiceRestrictionType.NORMAL, PlacementRestrictionType.COLOR, false), 128);
        commands.addAtIndex(new PlaceDice(DiceRestrictionType.NORMAL, PlacementRestrictionType.NUMBER, false), 256);
        commands.addAtIndex(new PlaceDice(DiceRestrictionType.ISOLATED, PlacementRestrictionType.NUMBER_COLOR, false), 512);
        commands.addAtIndex(new AddDiceToDraftPool(), 1024);
        commands.addAtIndex(new AddDiceToDiceBag(), 2048);
        commands.addAtIndex(new DrawDiceFromDicebag(), 4096);
        commands.addAtIndex(new ModifyDiceValue(), 8192);
        commands.addAtIndex(new RerollDraftPool(), 16384);
        commands.addAtIndex(new CheckTurn(2), 32768);
        commands.addAtIndex(new CheckTurn(1), 65536);
        commands.addAtIndex(new CheckBeforeDiceChosen(), 131072);
        commands.addAtIndex(new SkipTurn(2), 262144);
        commands.addAtIndex(new SkipTurn(1), 524288);
        commands.addAtIndex(new PourOverDice(),1048576);
        commands.addAtIndex(new ChooseColorFromRoundTrack(),2097152);

        String commandsString = new String("[1-Choose dice][2-Modify dice value by 1]" +
                "[4-Remove dice][8-Remove dice of a certain color]" +
                "[16-Swap dice with RoundTrack][32-Reroll dice][64-Place old dice][128-Place old dice ignoring number" +
                " constraints][256-Place old dice ignoring color constraints][512-Place isolated old dice]" +
                "[1024-Add dice to DraftPool][2048-Add dice to Dicebag][4096-Draw dice from Dicebag]" +
                "[8192-Modify dice value][16384-Reroll DraftPool][32768-Check second turn][65536-Check first turn]" +
                "[131072-Check before choose dice][262144-Skip second turn][524288-Skip first turn]" +
                "[1048576-Pour over dice][2097152-Choose color from RoundTrack]");


        try {
            actualCommands = toolCardLanguageParser.parseToolCard(commandsString);
            assertEquals(commands, actualCommands);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            fail("Exception not expected");
        }
    }

    @Test(expected = Exception.class)
    public void testNotRecognizedCommand() {
        String description = "[1-THIS IS NOT A COMMAND]";
        toolCardLanguageParser.parseToolCard(description);
    }

    @Test
    public void testNotPresentCommand() {
        String command = "thisIsNotACommand";
        Node<ICommand> commands = null;
        try {
            commands = toolCardLanguageParser.parseToolCard(command);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals(null, commands);
        }
    }

    @Test
    public void testSingleCommand() {
        String command = new String("[1-Reroll dice]");
        Node<ICommand> commands = new Node(new RerollDice());

        System.out.println(commands);

        try {
            Node<ICommand> actualCommands = toolCardLanguageParser.parseToolCard(command);
            System.out.println(actualCommands);
            assertEquals(true, commands.isRoot());
            assertEquals(commands, actualCommands);
        } catch (IllegalArgumentException e) {
            fail("No exceptione expected");
        }
    }
}

