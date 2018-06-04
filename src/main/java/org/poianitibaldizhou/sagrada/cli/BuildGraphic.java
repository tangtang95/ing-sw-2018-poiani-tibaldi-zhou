package org.poianitibaldizhou.sagrada.cli;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;

import java.util.Map;

/**
 * class for create the graphic of all object in game, like dice, toolCard,
 * publicObjectiveCard, privateObjectiveCard, table of points and roundTrack.
 * All remote object is passed with a json file (string).
 */
public class BuildGraphic {

    private JSONParser jsonParser;
    private JSONArray jsonArray;

    /**
     * the unique stringBuilder whom append any graphic object.
     */
    private StringBuilder stringBuilder;

    /**
     * Error Messages.
     */
    public static final String NOT_A_NUMBER = "Is not a number, please retry.\n";
    public static final String COMMAND_NOT_FOUND = "Command not found, please retry.\n";
    public static final String ERROR_READING = ": Error while reading from keyboard.\n";
    public static final String NETWORK_ERROR = ": Network error.\n";

    /**
     * Card attributes.
     */
    private static final String NAME = "Card Name: ";
    private static final String DESCRIPTION = "Description:\n";
    private static final String JSON_CARD_NAME = "name";
    private static final String JSON_CARD_DESCRIPTION = "description";

    /**
     * constructor.
     */
    public BuildGraphic() {
        stringBuilder = new StringBuilder();
        jsonParser = new JSONParser();
        jsonArray = null;
    }

    /**
     * build the graphic of a list of dices starting from the point start and
     * finishing in the end point whit UTF-8 code.
     *
     * @param arrayOfDice array of dices whom create the graphics.
     * @param start the start point.
     * @param end the end point.
     */
    private void buildListDices(JSONArray arrayOfDice, int start, int end) {
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
            JSONObject dice = (JSONObject) arrayOfDice.get(i);
            String formatDice = dice.get("value") + "/" + dice.get("color");
            stringBuilder.append(formatDice);
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

    /**
     * Build the graphic of a list of dices, putting five dices on the same line.
     *
     * @param jsonDiceList list of dices whom create the graphics.
     * @return the BuildGraphic with the stringBuilder changed.
     */
    public BuildGraphic buildGraphicDices(String jsonDiceList) {
        if (jsonDiceList != null) {
            jsonArray = null;
            try {
                jsonArray = (JSONArray) jsonParser.parse(jsonDiceList);
                if (jsonArray.size() <= 5) {
                    buildListDices(jsonArray, 0, jsonArray.size());
                } else {
                    buildListDices(jsonArray, 0, 5);
                    buildListDices(jsonArray, 5, jsonArray.size());
                }
            } catch (ParseException e) {
                PrinterManager.consolePrint("Parse exception in buildGraphicDices.\n", Level.ERROR);
            }
        }
        return this;
    }

    /**
     * Build the graphic for a single dice.
     *
     * @param dice dice whom create the graphics.
     * @return the BuildGraphic with the stringBuilder changed.
     */
    public BuildGraphic buildGraphicDice(String dice) {
        if (dice != null) {
            jsonArray = null;
            try {
                jsonArray = (JSONArray) jsonParser.parse(dice);
                buildListDices(jsonArray, 0, 1);
            } catch (ParseException e) {
                PrinterManager.consolePrint("Parse exception in buildGraphicDice.\n", Level.ERROR);
            }
        }
        return this;
    }


    /**
     * Append a message to the stringBuilder.
     *
     * @param message to append.
     * @return the BuildGraphic with the stringBuilder changed.
     */
    public BuildGraphic buildMessage(String message){
        if (message != null)
            stringBuilder.append(message).append("\n");
        return this;
    }

    /**
     * Build the graphic of a list of toolCard.
     *
     * @param toolCards list of toolCards whom create the graphics.
     * @return the BuildGraphic with the stringBuilder changed.
     */
    public BuildGraphic buildGraphicToolCards(String toolCards) {
        if (toolCards != null) {
            jsonArray = null;
            try {
                jsonArray = (JSONArray) jsonParser.parse(toolCards);
                buildMessage("----------------------------TOOL CARDS---------------------------");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject toolCard = (JSONObject) jsonArray.get(i);
                    stringBuilder.append("[").append(i).append("]\n");
                    stringBuilder.append(NAME).append((String) toolCard.get(JSON_CARD_NAME)).append("\n");
                    stringBuilder.append("ColorWrapper:     ").append((String) toolCard.get("color")).append("\n");
                    stringBuilder.append(DESCRIPTION);
                    stringBuilder.append((String) toolCard.get(JSON_CARD_DESCRIPTION)).append("\n\n");
                }
            } catch (ParseException e) {
                PrinterManager.consolePrint("Parse exception in buildGraphicToolCards.\n", Level.ERROR);
            }
        }
        return this;
    }

    /**
     * Build the graphic of a list of publicObjectiveCards.
     *
     * @param publicObjectiveCards list of publicObjectiveCards whom create the graphics.
     * @return the BuildGraphic with the stringBuilder changed.
     */
    public BuildGraphic buildGraphicPublicObjectiveCards(String publicObjectiveCards) {
        if (publicObjectiveCards != null) {
            jsonArray = null;
            try {
                jsonArray = (JSONArray) jsonParser.parse(publicObjectiveCards);
                buildMessage("------------------------PUBLIC OBJECTIVE CARDS-----------------------");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject poc = (JSONObject) jsonArray.get(i);
                    stringBuilder.append("[").append(i).append("]\n");
                    stringBuilder.append(NAME).append((String) poc.get(JSON_CARD_NAME)).append("\n");
                    stringBuilder.append("Point:     ").append((String) poc.get("point")).append("\n");
                    stringBuilder.append(DESCRIPTION);
                    stringBuilder.append((String) poc.get(JSON_CARD_DESCRIPTION)).append("\n\n");
                }
            } catch (ParseException e) {
                PrinterManager.consolePrint("Parse exception in buildGraphicPublicObjectiveCards.\n", Level.ERROR);
            }
        }
        return this;
    }

    /**
     * Build the graphic of a list of privateObjectiveCards.
     *
     * @param privateObjectiveCards list of privateObjectiveCards whom create the graphics.
     * @return the BuildGraphic with the stringBuilder changed.
     */
    public BuildGraphic buildGraphicPrivateObjectiveCards(String privateObjectiveCards) {
        if (privateObjectiveCards != null) {
            jsonArray = null;
            try {
                jsonArray = (JSONArray) jsonParser.parse(privateObjectiveCards);
                buildMessage("------------------------PRIVATE OBJECTIVE CARDS-----------------------");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject poc = (JSONObject) jsonArray.get(i);
                    stringBuilder.append("[").append(i).append("]\n");
                    stringBuilder.append(NAME).append((String) poc.get(JSON_CARD_NAME)).append("\n");
                    stringBuilder.append(DESCRIPTION);
                    stringBuilder.append((String) poc.get(JSON_CARD_DESCRIPTION)).append("\n\n");
                }
            } catch (ParseException e) {
                PrinterManager.consolePrint("Parse exception in buildGraphicPrivateObjectiveCards.\n", Level.ERROR);
            }
        }
        return this;
    }

    /**
     * build a formatted table.
     *
     * @param table to build.
     * @return the BuildGraphic with the stringBuilder changed.
     */
    public BuildGraphic buildGraphicTable(Map<String, String> table){
        if(table != null) {
            table.forEach((key, value) -> {
                stringBuilder.append(String.format("%10s %3s", key, value));
                stringBuilder.append("\n");
            });
        }
        return this;
    }

    /**
     * Build the command help.
     *
     * @param commandMap list of available command.
     * @return the BuildGraphic with the stringBuilder changed.
     */
    public BuildGraphic buildGraphicHelp(Map<String, Command> commandMap) {
        int maxLength = 0;
        Command command;

        if (commandMap != null) {
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
        }
        return this;
    }

    /**
     * Build the graphic logo of game.
     *
     * @return the BuildGraphic with the stringBuilder changed.
     */
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

    /**
     * Build the graphic of the round Track.
     *
     * @param roundTrack to build graphic.
     * @return the BuildGraphic with the stringBuilder changed.
     */
    public BuildGraphic buildGraphicRoundTrack(RoundTrack roundTrack) {
        if (roundTrack != null) {
            buildMessage("----------------------------ROUND TRACK---------------------------");
            for (int i = 0; i < RoundTrack.NUMBER_OF_TRACK; i++) {
                //TODO
                stringBuilder.append(buildMessage("Round " + "[" + (i + 1) + "]"));
            }
        }
        return this;
    }

    /**
     * @return the stringBuilder to string.
     */
    @Override
    public String toString() {
        return stringBuilder.toString();
    }

    /**
     * clean the stringBuilder.
     */
    public void clear() {
        stringBuilder.delete(0, stringBuilder.length());
    }
}
