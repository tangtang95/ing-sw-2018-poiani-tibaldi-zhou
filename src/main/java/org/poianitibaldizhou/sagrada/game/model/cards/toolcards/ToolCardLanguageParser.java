package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

import org.poianitibaldizhou.sagrada.game.model.Node;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.dice.DiceRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement.PlacementRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.*;

import java.util.*;

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
     * Each command needs to be separated with ";" and no additional spaces have to be present.
     * Example
     * "Reroll dice;Add dice to Dicebag"
     *
     * Wrong Example
     * "Reroll dice; Add dice to Dicebag"
     *
     * @param description effects of a tool card
     * @return list of commands triggered by description
     * @throws IllegalArgumentException if a string isn't matching with any of the avaible commands.
     */
    public List<ICommand> parseToolCard(String description) throws IllegalArgumentException {
        List<ICommand> commands = new ArrayList<>();
        ArrayList<String> processedText=  preprocessing(description);
        for(String s: processedText) {
            if(grammar.get(s) != null)
                commands.add(grammar.get(s));
            else
                throw new IllegalArgumentException("Command not recognized: " + s);
        }

        return commands;
    }

    @Deprecated
    public Node<ICommand> parseToolCardNewLanguage(String description) throws IllegalArgumentException {
        Node<ICommand> commands = new Node<>(null,null);
        return commands;
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
        grammar.put("Check Dice placeble", new CheckDicePositionable());
        grammar.put("Wait turn end", new WaitTurnEnd());
        grammar.put("Remove dice from DraftPool", new RemoveDiceFromDraftPool());

        ICommand clearColor = (player, toolCardExecutorHelper, game) -> {
            toolCardExecutorHelper.setNeededColor(null);
            return true;
        };
        ICommand clearValue = (player, toolCardExecutorHelper, game) -> {
            toolCardExecutorHelper.setNeededValue(null);
            return true;
        };

        ICommand clearDice = (player, toolCardExecutorHelper, game) -> {
            toolCardExecutorHelper.setNeededDice(null);
            return true;
        };

        ICommand clearPosition =  (player, toolCardExecutorHelper, game) -> {
            toolCardExecutorHelper.setNeededPosition(null);
            return true;
        };

        ICommand clearTurnEndCondition = (player, toolCardExecutorHelper, game) -> {
            toolCardExecutorHelper.setTurnEnded(false);
            return true;
        };

        ICommand clearAll = (player, toolCardExecutorHelper, game) -> {
            clearColor.executeCommand(player, toolCardExecutorHelper, game);
            clearDice.executeCommand(player, toolCardExecutorHelper, game);
            clearPosition.executeCommand(player, toolCardExecutorHelper, game);
            clearTurnEndCondition.executeCommand(player, toolCardExecutorHelper, game);
            clearValue.executeCommand(player, toolCardExecutorHelper, game);
            return true;
        };

        grammar.put("CC", clearColor);
        grammar.put("CV", clearValue);
        grammar.put("CD", clearDice);
        grammar.put("CP", clearPosition);
        grammar.put("CTEC", clearTurnEndCondition);
        grammar.put("CA", clearAll);
    }
}
