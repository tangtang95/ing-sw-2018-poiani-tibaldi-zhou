package org.poianitibaldizhou.sagrada.cli;

import org.poianitibaldizhou.sagrada.game.model.Dice;

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

    public BuildGraphic buildTable(Map<String, String> table){
        table.forEach((key, value) -> {
            stringBuilder.append(String.format("%10s %3s", key, value));
            stringBuilder.append("\n");
        });
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
