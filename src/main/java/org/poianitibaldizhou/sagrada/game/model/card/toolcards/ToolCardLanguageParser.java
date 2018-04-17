package org.poianitibaldizhou.sagrada.game.model.card.toolcards;

import java.util.*;

public class ToolCardLanguageParser {
    private Map<String, ICommand> grammar;

    public ToolCardLanguageParser(){
        setGrammar();
    }

    public List<ICommand> parseToolCard(String description){
        List<ICommand> commands = new ArrayList<ICommand>();
        ArrayList<String> processedText=  preprocessing(description);
        for(String s: processedText) {
            commands.add(grammar.get(s));
        }

        return commands;
    }

    private ArrayList<String> preprocessing(String text){
        return new ArrayList<String>(Arrays.asList(text.split(";")));
    }

    private void setGrammar(){
        grammar = new HashMap<String, ICommand>();
        grammar.put("diceChose", new ChooseDice());
    }
}
