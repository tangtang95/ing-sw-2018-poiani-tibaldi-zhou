package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

import org.poianitibaldizhou.sagrada.game.model.Node;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.dice.DiceRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement.PlacementRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public Node<ICommand> parseToolCard(String description) throws IllegalArgumentException {
        Node<ICommand> commands_root = null;

        Pattern p = Pattern.compile("\\[(.*?)\\]");
        Matcher m = p.matcher(description);

        boolean firstCheck = m.find();

        if(!firstCheck)
            throw new IllegalArgumentException("This is not a command " + description);

        while(firstCheck) {
            String temp  = m.group(1);
            String[] c = temp.split("-");
            if(grammar.get(c[1]) == null || c.length != 2)
                throw new IllegalArgumentException("Command not recognized " + c[1]);
            if(commands_root == null)
                commands_root = new Node<>(grammar.get(c[1]));
            else
                commands_root.addAtIndex(grammar.get(c[1]),Integer.parseInt(c[0]));
            firstCheck = m.find();
        }

        return commands_root;
    }

    /**
     * Preprocesses the string, dividing sentences separeted with ";".
     *
     * @param text string that needs to be preprocessed
     * @return a list of string contained in text separeted by ";"
     */
    private ArrayList<String> preprocessing(String text){
        return new ArrayList<>(Arrays.asList(text.split(";")));
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
        grammar.put("Reroll dice", new RerollDice());
        grammar.put("Place dice", new PlaceDice(PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.NORMAL));
        grammar.put("Place dice ignoring number constraints", new PlaceDice(PlacementRestrictionType.COLOR, DiceRestrictionType.NORMAL));
        grammar.put("Place dice ignoring color constraints", new PlaceDice(PlacementRestrictionType.NUMBER, DiceRestrictionType.NORMAL));
        grammar.put("Place isolated dice", new PlaceDice(PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.ISOLATED));
        grammar.put("Add dice to DraftPool", new AddDiceToDraftPool());
        grammar.put("Add dice to Dicebag", new AddDiceToDiceBag());
        grammar.put("Draw dice from Dicebag", new DrawDiceFromDicebag());
        grammar.put("Modify dice value", new ModifyDiceValue());
        grammar.put("Reroll DraftPool", new RerollDraftPool());
        grammar.put("Check second turn", new CheckTurn(2));
        grammar.put("Check first turn", new CheckTurn(1));
        grammar.put("Check before choose dice", new CheckBeforeDiceChosen());
        grammar.put("Check turn over", new CheckTurnEnd());
        grammar.put("Skip second turn", new SkipTurn(2));
        grammar.put("Skip first turn", new SkipTurn(1));
        grammar.put("Pour over dice", new PourOverDice());
        grammar.put("Choose color from RoundTrack", new ChooseColorFromRoundTrack());
        grammar.put("If Dice placeable", new IfDicePlaceable());
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
