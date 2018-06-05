package org.poianitibaldizhou.sagrada.cli;

import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * class for create the graphic of all object in game, like dice, toolCard,
 * publicObjectiveCard, privateObjectiveCard, table of points and roundTrack.
 * All remote object is passed with a json file (string).
 */
public class BuildGraphic {

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

    /**
     * constructor.
     */
    public BuildGraphic() {
        stringBuilder = new StringBuilder();
    }

    /**
     * build the graphic of a list of dices starting from the point start and
     * finishing in the end point whit UTF-8 code.
     *
     * @param diceWrappers array of dices whom create the graphics.
     * @param start        the start point.
     * @param end          the end point.
     */
    private void buildListDices(List<DiceWrapper> diceWrappers, int start, int end) {
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
            stringBuilder.append(diceWrappers.get(i).toString());
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
     * @param diceListWrapper list of dices whom create the graphics.
     * @return the BuildGraphic with the stringBuilder changed.
     */
    public BuildGraphic buildGraphicDices(List<DiceWrapper> diceListWrapper) {
        if (diceListWrapper != null) {
            if (diceListWrapper.size() <= 5)
                buildListDices(diceListWrapper, 0, diceListWrapper.size());
            else {
                buildListDices(diceListWrapper, 0, 5);
                buildListDices(diceListWrapper, 5, diceListWrapper.size());
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
    public BuildGraphic buildGraphicDice(DiceWrapper dice) {
        if (dice != null) {
            List<DiceWrapper> diceWrappers = new ArrayList<>();
            diceWrappers.add(dice);
            buildListDices(diceWrappers, 0, 1);
        }
        return this;
    }


    /**
     * Append a message to the stringBuilder.
     *
     * @param message to append.
     * @return the BuildGraphic with the stringBuilder changed.
     */
    public BuildGraphic buildMessage(String message) {
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
    public BuildGraphic buildGraphicToolCards(List<ToolCardWrapper> toolCards) {
        if (toolCards != null) {
            buildMessage("----------------------------TOOL CARDS---------------------------");
            for (int i = 0; i < toolCards.size(); i++) {
                ToolCardWrapper toolCard = toolCards.get(i);
                stringBuilder.append("[").append(i).append("]\n");
                stringBuilder.append(NAME).append(toolCard.getName()).append("\n");
                stringBuilder.append("Color:     ").append(toolCard.getColor()).append("\n");
                stringBuilder.append(DESCRIPTION);
                stringBuilder.append(toolCard.getDescription()).append("\n\n");
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
    public BuildGraphic buildGraphicPublicObjectiveCards(List<PublicObjectiveCardWrapper> publicObjectiveCards) {
        if (publicObjectiveCards != null) {

            buildMessage("------------------------PUBLIC OBJECTIVE CARDS-----------------------");
            for (int i = 0; i < publicObjectiveCards.size(); i++) {
                PublicObjectiveCardWrapper poc = publicObjectiveCards.get(i);
                stringBuilder.append("[").append(i).append("]\n");
                stringBuilder.append(NAME).append(poc.getName()).append("\n");
                stringBuilder.append("Point:     ").append(poc.getCardPoint()).append("\n");
                stringBuilder.append(DESCRIPTION);
                stringBuilder.append(poc.getDescription()).append("\n\n");
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
    public BuildGraphic buildGraphicPrivateObjectiveCards(List<PrivateObjectiveCardWrapper> privateObjectiveCards) {
        if (privateObjectiveCards != null) {
            buildMessage("------------------------PRIVATE OBJECTIVE CARDS-----------------------");
            for (int i = 0; i < privateObjectiveCards.size(); i++) {
                PrivateObjectiveCardWrapper poc = privateObjectiveCards.get(i);
                stringBuilder.append("[").append(i).append("]\n");
                stringBuilder.append(NAME).append(poc.getName()).append("\n");
                stringBuilder.append(DESCRIPTION);
                stringBuilder.append(poc.getDescription()).append("\n\n");
            }
        }
        return this;
    }

    public BuildGraphic buildGraphicPrivateObjectiveCard(PrivateObjectiveCardWrapper privateObjectiveCard) {
        if (privateObjectiveCard != null) {
            buildMessage("------------------------PRIVATE OBJECTIVE CARD-----------------------");
            stringBuilder.append(NAME).append(privateObjectiveCard.getName()).append("\n");
            stringBuilder.append(DESCRIPTION);
            stringBuilder.append(privateObjectiveCard.getDescription()).append("\n\n");
        }
        return this;
    }

    /**
     * build a formatted table.
     *
     * @param table to build.
     * @return the BuildGraphic with the stringBuilder changed.
     */
    public BuildGraphic buildGraphicTable(Map<String, String> table) {
        if (table != null) {
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
    public BuildGraphic buildGraphicRoundTrack(RoundTrackWrapper roundTrack) {
        if (roundTrack != null) {
            buildMessage("----------------------------ROUND TRACK---------------------------");
            for (int i = 0; i < RoundTrack.NUMBER_OF_TRACK; i++) {
                buildMessage("Round " + "[" + (i + 1) + "]");
                buildGraphicDices(roundTrack.getDicesPerRound(i));
            }
        }
        return this;
    }

    public BuildGraphic buildGraphicSchemaCard(SchemaCardWrapper schemaCard) {
        if (schemaCard != null) {
            buildMessage("---------------------------SCHEMA CARD---------------------------");
            stringBuilder.append(schemaCard.toString()).append("\n");
        }
        return this;
    }

    public BuildGraphic buildGraphicDraftPool(DraftPoolWrapper draftPoolWrapper) {
        if (draftPoolWrapper != null) {
            buildMessage("---------------------------DRAFT POOL---------------------------");
            buildGraphicDices(draftPoolWrapper.getDices());
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
