package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

import org.poianitibaldizhou.sagrada.exception.CommandNotFoundException;
import org.poianitibaldizhou.sagrada.game.model.cards.ConstraintType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.*;

import java.util.*;

public class ToolCardLanguageParser {
    private static Map<String, ICommand> grammar;

    public ToolCardLanguageParser(){
        setGrammar();
    }

    public List<ICommand> parseToolCard(String description) throws CommandNotFoundException {
        List<ICommand> commands = new ArrayList<ICommand>();
        ArrayList<String> processedText=  preprocessing(description);
        for(String s: processedText) {
            if(grammar.get(s) != null)
                commands.add(grammar.get(s));
            else
                throw new CommandNotFoundException("Command not recognized: " + s);
        }

        return commands;
    }

    private ArrayList<String> preprocessing(String text){
        return new ArrayList<String>(Arrays.asList(text.split(";")));
    }

    private void setGrammar(){
        grammar = new HashMap<String, ICommand>();
        grammar.put("Choose dice", new ChooseDice());
        grammar.put("Modify dice value by 1", new ModifyDiceValue(1));
        grammar.put("Remove dice from schema", new RemoveDice(ConstraintType.NONE));
        grammar.put("Remove dice of a certain color from schema", new RemoveDice(ConstraintType.COLOR));
        grammar.put("Swap dice with RoundTrack", new SwapDice());
        grammar.put("Reroll dice", new RerollDice());
        grammar.put("Place dice", new PlaceDice(ConstraintType.NONE));
        grammar.put("Place dice ignoring number constraints", new PlaceDice(ConstraintType.NUMBER));
        grammar.put("Place dice ignoring color constraints", new PlaceDice(ConstraintType.COLOR));
        grammar.put("Place isolated dice", new PlaceDice(ConstraintType.ISOLATED));
        grammar.put("Add dice to DraftPool", new AddDiceToDraftPool());
        grammar.put("Add dice to Dicebag", new AddDiceToDiceBag());
        grammar.put("Draw dice from Dicebag", new DrawDiceFromDicebag());
        grammar.put("Choose dice value", new ChooseDiceValue());
        grammar.put("Reroll DraftPool", new RerollDraftPool());
        grammar.put("Check second turn", new CheckTurn(2));
        grammar.put("Check first turn", new CheckTurn(1));
        grammar.put("Check before choose dice", new CheckBeforeDiceChose());
        grammar.put("Check turn over", new CheckTurnEnd());
        grammar.put("Skip second turn", new SkipTurn(2));
        grammar.put("Skip first turn", new SkipTurn(1));
        grammar.put("Pour over dice", new PourOverDice());
        grammar.put("Choose color from RoundTrack", new ChooseColorFromRoundTrack());
    }
}
