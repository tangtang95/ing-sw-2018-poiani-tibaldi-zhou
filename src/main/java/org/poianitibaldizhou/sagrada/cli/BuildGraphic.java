package org.poianitibaldizhou.sagrada.cli;

import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BuildGraphic {
    private StringBuilder stringBuilder;

    public BuildGraphic() {
        stringBuilder = new StringBuilder();
    }

    private void buildListDices(List<Dice> diceList, int start, int end) {
        for (int i = start; i < end; i++)
            stringBuilder.append("  [").append(i + 1).append("]   ");
        stringBuilder.append("\n");
        for (int i = start; i < end; i++) {
            stringBuilder.append((char) 9556);
            stringBuilder.append((char) 9552);
            stringBuilder.append((char) 9552);
            stringBuilder.append((char) 9552);
            stringBuilder.append((char) 9552);
            stringBuilder.append((char) 9552);
            stringBuilder.append((char) 9552);
            stringBuilder.append((char) 9559 + " ");
        }
        stringBuilder.append("\n");
        for (int i = start; i < end; i++) {
            stringBuilder.append((char) 9553 + " ");
            stringBuilder.append(diceList.get(i).toString());
            stringBuilder.append(" " + (char) 9553 + " ");
        }
        stringBuilder.append("\n");
        for (int i = start; i < end; i++) {
            stringBuilder.append((char) 9562);
            stringBuilder.append((char) 9552);
            stringBuilder.append((char) 9552);
            stringBuilder.append((char) 9552);
            stringBuilder.append((char) 9552);
            stringBuilder.append((char) 9552);
            stringBuilder.append((char) 9552);
            stringBuilder.append((char) 9565 + " ");
        }
        stringBuilder.append("\n");
    }

    public BuildGraphic buildGraphicDices(List<Dice> diceList) {
        if (diceList.size() <= 5) {
            buildListDices(diceList, 0, diceList.size());
        } else {
            buildListDices(diceList, 0, 5);
            buildListDices(diceList, 5, diceList.size());
        }
        return this;
    }

    public BuildGraphic buildGraphicDice(Dice dice) {
        List<Dice> diceList = new ArrayList<>();
        diceList.add(dice);
        buildGraphicDices(diceList);
        return this;
    }

    public BuildGraphic buildMessage(String message){
        stringBuilder.append(message).append("\n");
        return this;
    }

    public BuildGraphic buildGraphicToolCards(List<ToolCard> toolCards) {
        buildMessage("----------------------------TOOL CARDS---------------------------");
        for (int i = 0; i < toolCards.size() ; i++) {
               stringBuilder.append("[").append(i).append("]\n");
               stringBuilder.append("Card Name: ").append(toolCards.get(i).getName()).append("\n");
               stringBuilder.append("Color:     ").append(toolCards.get(i).getColor()).append("\n");
               stringBuilder.append("Description:\n");
               stringBuilder.append(toolCards.get(i).getDescription()).append("\n\n");
        }
        return this;
    }

    public BuildGraphic buildGraphicPublicObjectiveCards(List<PublicObjectiveCard> publicObjectiveCards) {
        buildMessage("------------------------PUBLIC OBJECTIVE CARDS-----------------------");
        for (int i = 0; i < publicObjectiveCards.size() ; i++) {
            stringBuilder.append("[").append(i).append("]\n");
            stringBuilder.append("Card Name: ").append(publicObjectiveCards.get(i).getName()).append("\n");
            stringBuilder.append("Point:     ").append(publicObjectiveCards.get(i).getCardPoints()).append("\n");
            stringBuilder.append("Description:\n");
            stringBuilder.append(publicObjectiveCards.get(i).getDescription()).append("\n\n");
        }
        return this;
    }

    public BuildGraphic buildGraphicPrivateObjectiveCards(List<PrivateObjectiveCard> privateObjectiveCards) {
        buildMessage("------------------------PRIVATE OBJECTIVE CARDS-----------------------");
        for (int i = 0; i < privateObjectiveCards.size() ; i++) {
            stringBuilder.append("[").append(i).append("]\n");
            stringBuilder.append("Card Name: ").append(privateObjectiveCards.get(i).getName()).append("\n");
            stringBuilder.append("Description:\n");
            stringBuilder.append(privateObjectiveCards.get(i).getDescription()).append("\n\n");
        }
        return this;
    }

    public BuildGraphic buildGraphicTable(Map<String, String> table){
        table.forEach((key, value) -> {
            stringBuilder.append(String.format("%10s %3s", key, value));
            stringBuilder.append("\n");
        });
        return this;
    }

    public BuildGraphic buildGraphicHelp(Map<String, Command> commandMap) {
        int maxLength = 0;
        Command command;

        stringBuilder.append("Available commands:");
        stringBuilder.append("\n");
        for (Command com : commandMap.values())
            if (com.getCommandText().length() > maxLength)
                maxLength = com.getCommandText().length();
        for (int i = 0; i < commandMap.keySet().size(); i++) {
            command = commandMap.get(commandMap.keySet().toArray()[i].toString());
            stringBuilder.append("[");
            stringBuilder.append((i + 1));
            stringBuilder.append("] ");
            stringBuilder.append(command.getCommandText());
            for (int j = 0; j < maxLength - command.getCommandText().length(); j++)
                stringBuilder.append(" ");
            stringBuilder.append("\t\t");
            stringBuilder.append(command.getHelpText());
            stringBuilder.append("\n");
        }
        return this;
    }

    public BuildGraphic buildGraphicLogo() {
        int charNumber = 76;

        stringBuilder.append((char) 9556);
        for (int i = 0; i < charNumber; i++) {
            stringBuilder.append((char) 9552);
        }
        stringBuilder.append((char) 9559 + "\n" + (char) 9553);
        for (int i = 0; i < (charNumber - 13) / 3; i++) {
            stringBuilder.append(" ");
        }
        stringBuilder.append("     S A G R A D A     ");
        for (int i = 0; i < (charNumber - 13) / 3; i++) {
            stringBuilder.append(" ");
        }
        stringBuilder.append((char) 9553 + "\n" + (char) 9562);
        for (int i = 0; i < charNumber; i++) {
            stringBuilder.append((char) 9552);
        }
        stringBuilder.append((char) 9565 + "\n");
        return this;
    }

    public BuildGraphic buildGraphicRoundTrack(RoundTrack roundTrack) {
        buildMessage("----------------------------ROUND TRACK---------------------------");
        for (int i = 0; i < RoundTrack.NUMBER_OF_TRACK; i++) {
            List<Dice> diceList = roundTrack.getDices(i);
            stringBuilder.append(buildMessage("Round " + "[" + i + 1 + "]").buildGraphicDices(diceList).toString());
        }
        return this;
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }

    public void clear() {
        stringBuilder.delete(0, stringBuilder.length());
    }
}
