package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

import org.poianitibaldizhou.sagrada.game.model.cards.restriction.dice.DiceRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement.PlacementRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.*;
import org.poianitibaldizhou.sagrada.utilities.ServerMessage;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * OVERVIEW: Represents the parser for the tool card language.
 */
public class ToolCardLanguageParser {
    private static Map<String, ICommand> grammar;

    /**
     * Constructor.
     * Creates an object needed for parsing the tool cards language.
     */
    public ToolCardLanguageParser(){
        setGrammar();
    }

    /**
     * Given a string that contains the description of the effects of a tool card, returns
     * the list of the commands associated with it.
     * The language supported in this method supports binary if.
     * Each command needs to be wrapped in [index-commandName], where index is the position of the command in the
     * execution flow, according to the following rules.
     * An example could be:
     * String string = "[1-Choose dice][2-Modify dice value by 1][4-Add dice to DraftPool][8-CA]";
     *
     * @param description description of the tool card that allow to construct an execution tree
     * @return execution tree
     * @throws IllegalArgumentException if a string isn't matching with any of the available commands.
     */
    public Node<ICommand> parseToolCard(String description) {
        Node<ICommand> commandsRoot = null;

        Pattern p = Pattern.compile("\\[(.*?)]");
        Matcher m = p.matcher(description);

        boolean firstCheck = m.find();

        if(!firstCheck)
            throw new IllegalArgumentException(ServerMessage.LANGUAGE_PARSER_ILLEGAL_ARGUMENT1 + description);

        while(firstCheck) {
            String temp  = m.group(1);
            String[] c = temp.split("-");
            if(grammar.get(c[1]) == null || c.length != 2)
                throw new IllegalArgumentException(ServerMessage.LANGUAGE_PARSER_ILLEGAL_ARGUMENT2+ c[1]);
            if(commandsRoot == null)
                commandsRoot = new Node<>(grammar.get(c[1]));
            else
                commandsRoot.addAtIndex(grammar.get(c[1]),Integer.parseInt(c[0]));
            firstCheck = m.find();
        }

        return commandsRoot;
    }

    /**
     * Sets the grammar of the language
     */
    private static void setGrammar(){
        grammar = new HashMap<>();
        grammar.put("Choose dice", new ChooseDice());
        grammar.put("Modify dice value by 1", new ModifyDiceValueByDelta(1));
        grammar.put("Remove dice", new RemoveDice(PlacementRestrictionType.NONE));
        grammar.put("Remove dice of a certain color", new RemoveDice(PlacementRestrictionType.COLOR));
        grammar.put("Swap dice with RoundTrack", new SwapDiceWithRoundTrack());
        grammar.put("Reroll dice", new ReRollDice());
        grammar.put("Place old dice", new PlaceDice(DiceRestrictionType.NORMAL,
                PlacementRestrictionType.NUMBER_COLOR, false));
        grammar.put("Place new dice", new PlaceDice(DiceRestrictionType.NORMAL,
                PlacementRestrictionType.NUMBER_COLOR, true));
        grammar.put("Place old dice ignoring number constraints", new PlaceDice(DiceRestrictionType.NORMAL,
                PlacementRestrictionType.COLOR, false));
        grammar.put("Place new dice ignoring number constraints", new PlaceDice(DiceRestrictionType.NORMAL,
                PlacementRestrictionType.COLOR, true));
        grammar.put("Place old dice ignoring color constraints", new PlaceDice(DiceRestrictionType.NORMAL,
                PlacementRestrictionType.NUMBER, false));
        grammar.put("Place new dice ignoring color constraints", new PlaceDice(DiceRestrictionType.NORMAL,
                PlacementRestrictionType.NUMBER, true));
        grammar.put("Place isolated old dice", new PlaceDice(DiceRestrictionType.ISOLATED,
                PlacementRestrictionType.NUMBER_COLOR, false));
        grammar.put("Place isolated new dice", new PlaceDice(DiceRestrictionType.ISOLATED,
                PlacementRestrictionType.NUMBER_COLOR, true));
        grammar.put("Add dice to DraftPool", new AddDiceToDraftPool());
        grammar.put("Add dice to Dicebag", new AddDiceToDiceBag());
        grammar.put("Draw dice from Dicebag", new DrawDiceFromDiceBag());
        grammar.put("Modify dice value", new ModifyDiceValue());
        grammar.put("Reroll DraftPool", new ReRollDraftPool());
        grammar.put("Check second turn", new CheckTurn(2));
        grammar.put("Check first turn", new CheckTurn(1));
        grammar.put("Check before choose dice", new CheckBeforeDiceChosen());
        grammar.put("Skip second turn", new SkipTurn(2));
        grammar.put("Skip first turn", new SkipTurn(1));
        grammar.put("Pour over dice", new PourOverDice());
        grammar.put("Choose color from RoundTrack", new ChooseColorFromRoundTrack());
        grammar.put("If Dice placeable", new IfDicePlaceable());
        grammar.put("If continue", new IfContinue());
        grammar.put("Wait turn end", new WaitTurnEnd());
        grammar.put("Remove dice from DraftPool", new RemoveDiceFromDraftPool());
        grammar.put("CC", new ClearColor());
        grammar.put("CV", new ClearValue());
        grammar.put("CD", new ClearDice());
        grammar.put("CP", new ClearPosition());
        grammar.put("CTEC", new ClearTurnEndCondition());
        grammar.put("CA", new ClearAll());
    }
}
