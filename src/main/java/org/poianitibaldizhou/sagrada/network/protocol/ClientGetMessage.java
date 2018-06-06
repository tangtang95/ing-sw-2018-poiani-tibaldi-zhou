package org.poianitibaldizhou.sagrada.network.protocol;

import org.json.simple.parser.ParseException;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientGetMessage {
    private JSONClientProtocol jsonClientProtocol;

    public ClientGetMessage() {
        jsonClientProtocol = new JSONClientProtocol();
    }

    public DiceWrapper getDiceElem(String message) throws IOException {
        DiceWrapper diceWrapper;

        try {
            diceWrapper = (DiceWrapper) jsonClientProtocol.getResponseByKey(message, SharedConstants.ELEM);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }

        return diceWrapper;
    }

    public List<DiceWrapper> getDiceElemList(String message) throws IOException {
        List<DiceWrapper> diceWrapperList;

        try {
            diceWrapperList = (List<DiceWrapper>) jsonClientProtocol.getResponseByKey(message, SharedConstants.ELEM_LIST_KEY);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }

        return diceWrapperList;
    }

    public DiceWrapper getDice(String message) throws IOException {
        DiceWrapper diceWrapper;

        try {
            diceWrapper = (DiceWrapper) jsonClientProtocol.getResponseByKey(message, SharedConstants.DICE);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }

        return diceWrapper;
    }

    public List<DiceWrapper> getDiceList(String message) throws IOException {
        List<DiceWrapper> diceWrapper;

        try {
            diceWrapper = (List<DiceWrapper>) jsonClientProtocol.getResponseByKey(message, SharedConstants.DICE_LIST_KEY);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }

        return diceWrapper;
    }

    public UserWrapper getUserWrapper(String message) throws IOException {
        UserWrapper userWrapper;
        try {
            userWrapper = (UserWrapper) jsonClientProtocol.getResponseByKey(message, SharedConstants.USER);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return userWrapper;
    }

    public UserWrapper getTurnUserWrapper(String message) throws IOException {
        UserWrapper userWrapper;
        try {
            userWrapper = (UserWrapper) jsonClientProtocol.getResponseByKey(message, SharedConstants.TURN_USER);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return userWrapper;
    }

    public Map<UserWrapper, Integer> getVictoryPoint(String message) throws IOException {
        Map<UserWrapper, Integer> victoryPoint;
        try {
            victoryPoint = (Map<UserWrapper, Integer>) jsonClientProtocol.getResponseByKey(message, SharedConstants.VICTORY_POINT_MAP);
        } catch (ParseException e) {
            throw new IOException();
        }
        return victoryPoint;
    }

    public List<UserWrapper> getListOfUserWrapper(String message) throws IOException {
        List<UserWrapper> userWrappers;
        try {
            userWrappers = (List<UserWrapper>) jsonClientProtocol.getResponseByKey(message, SharedConstants.USER_LIST_KEY);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }

        return userWrappers;
    }

    public String getGameName(String message) throws IOException {
        String gameName;
        try {
            gameName = (String) jsonClientProtocol.getResponseByKey(message,SharedConstants.GAME_NAME_KEY);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return gameName;
    }

    public String getToken(String message) throws IOException {
        String token;
        try {
            token = (String) jsonClientProtocol.getResponseByKey(message,SharedConstants.TOKEN_KEY );
        } catch(ParseException | ClassCastException e) {
            throw new IOException();
        }
        return token;
    }

    public String getTimeout(String message) throws IOException {
        String timeout;
        try {
            timeout = (String) jsonClientProtocol.getResponseByKey(message, SharedConstants.TIMEOUT);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return timeout;
    }

    public Integer getValue(String message) throws IOException {
        Integer value;

        try {
            value = (Integer) jsonClientProtocol.getResponseByKey(message, SharedConstants.INTEGER);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }

        return value;
    }

    public String getOutcome(String message) throws IOException {
        String outcome;

        try {
            outcome = (String) jsonClientProtocol.getResponseByKey(message, SharedConstants.OUTCOME);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }

        return outcome;
    }

    public DiceWrapper getOldDice(String message) throws IOException {
        DiceWrapper diceWrapper;

        try {
            diceWrapper = (DiceWrapper) jsonClientProtocol.getResponseByKey(message, SharedConstants.OLD_DICE);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }

        return diceWrapper;
    }

    public DiceWrapper getNewDice (String message) throws IOException {
        DiceWrapper diceWrapper;

        try {
            diceWrapper = (DiceWrapper) jsonClientProtocol.getResponseByKey(message, SharedConstants.NEW_DICE);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }

        return diceWrapper;
    }

    public PositionWrapper getPosition(String message) throws IOException {
        PositionWrapper positionWrapper;

        try {
            positionWrapper = (PositionWrapper) jsonClientProtocol.getResponseByKey(message, SharedConstants.POSITION);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }

        return positionWrapper;
    }

    public List<ColorWrapper> getColorList(String message) throws IOException {
        List<ColorWrapper> colorWrappers;
        try {
            colorWrappers = (List<ColorWrapper>) jsonClientProtocol.getResponseByKey(message, SharedConstants.COLOR_LIST_KEY);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return colorWrappers;
    }

    public Integer getDiceValue(String message) throws IOException {
        Integer value;

        try {
            value = (Integer) jsonClientProtocol.getResponseByKey(message, SharedConstants.DICE_VALUE);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return value;
    }

    public List<PublicObjectiveCardWrapper> getPublicObjectiveCards(String message) throws IOException {
        List<PublicObjectiveCardWrapper> poc;
        try {
            poc = (List<PublicObjectiveCardWrapper>) jsonClientProtocol.getResponseByKey(message,
                    SharedConstants.PUBLIC_OBJECTIVE_CARD_LIST_KEY);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return poc;
    }

    public List<ToolCardWrapper> getToolCards(String message) throws IOException {
        List<ToolCardWrapper> toolCardWrappers;
        try {
            toolCardWrappers = (List<ToolCardWrapper>) jsonClientProtocol.getResponseByKey(message,
                    SharedConstants.TOOL_CARD_LIST_KEY);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return toolCardWrappers;
    }

    public List<PrivateObjectiveCardWrapper> getPrivateObjectiveCards(String message) throws IOException {
        List<PrivateObjectiveCardWrapper> poc;
        try {
            poc = (List<PrivateObjectiveCardWrapper>) jsonClientProtocol.getResponseByKey(message,
                    SharedConstants.PRIVATE_OBJECTIVE_CARD_LIST_KEY);
        } catch (ParseException | ClassCastException e) {
            e.printStackTrace();
            throw new IOException();
        }
        return poc;
    }

    public List<FrontBackSchemaCardWrapper> getFrontBackSchemaCards(String message) throws IOException {
        List<FrontBackSchemaCardWrapper> fbsc;
        try {
            fbsc = (List<FrontBackSchemaCardWrapper>) jsonClientProtocol.getResponseByKey(message,
                    SharedConstants.FRONT_CARD_LIST_KEY);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return fbsc;
    }

    public SchemaCardWrapper getSchemaCard(String message) throws IOException {
        SchemaCardWrapper schemaCardWrapper;
        try {
            schemaCardWrapper = (SchemaCardWrapper) jsonClientProtocol.getResponseByKey(message, SharedConstants.SCHEMA_CARD);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return schemaCardWrapper;
    }

    public ColorWrapper getColor(String message) throws IOException{
        ColorWrapper colorWrapper;
        try {
            colorWrapper = (ColorWrapper) jsonClientProtocol.getResponseByKey(message, SharedConstants.COLOR);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return colorWrapper;
    }

    public RoundTrackWrapper getRoundTrack(String message) throws IOException {
        RoundTrackWrapper roundTrack;
        try {
            roundTrack = (RoundTrackWrapper) jsonClientProtocol.getResponseByKey(message, SharedConstants.ROUND_TRACK);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return roundTrack;
    }

    public PrivateObjectiveCardWrapper getPrivateObjectiveCard(String message) throws IOException {
        PrivateObjectiveCardWrapper poc;
        try {
            poc = (PrivateObjectiveCardWrapper) jsonClientProtocol.getResponseByKey(message,
                    SharedConstants.PRIVATE_OBJECTIVE_CARD);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return poc;
    }

    public Map<UserWrapper,SchemaCardWrapper> getSchemaCards(String message) throws IOException {
        Map<UserWrapper,SchemaCardWrapper> schemaCardWrappers;
        try {
            schemaCardWrappers = (Map<UserWrapper, SchemaCardWrapper>) jsonClientProtocol.getResponseByKey(message,
                    SharedConstants.MAP_SCHEMA_CARD_KEY);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return schemaCardWrappers;
    }

    public DraftPoolWrapper getDraftPool(String message) throws IOException {
        DraftPoolWrapper draftPoolWrapper;
        try {
            draftPoolWrapper = (DraftPoolWrapper) jsonClientProtocol.getResponseByKey(message,
                    SharedConstants.DRAFT_POOL);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return draftPoolWrapper;
    }

    public UserWrapper getRoundUser(String message) throws IOException {
        UserWrapper userWrapper;

        try {
            userWrapper = (UserWrapper) jsonClientProtocol.getResponseByKey(message, SharedConstants.ROUND_USER);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }

        return userWrapper;
    }
}
