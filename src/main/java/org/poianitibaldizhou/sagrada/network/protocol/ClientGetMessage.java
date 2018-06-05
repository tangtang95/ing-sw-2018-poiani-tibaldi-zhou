package org.poianitibaldizhou.sagrada.network.protocol;

import org.json.simple.parser.ParseException;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;

import java.io.IOException;
import java.util.List;

public class ClientGetMessage {
    private JSONClientProtocol jsonClientProtocol;

    public ClientGetMessage() {
        jsonClientProtocol = new JSONClientProtocol();
    }

    public DiceWrapper getDice(String message) throws IOException {
        return null;
    }

    public List<DiceWrapper> getDiceList(String message) throws IOException {
        return null;
    }

    public UserWrapper getUserWrapper(String message) throws IOException {
        UserWrapper userWrapper;
        try {
            userWrapper = (UserWrapper) jsonClientProtocol.getResponseByKey(message, SharedConstants.USER);
        } catch (ParseException | ClassCastException e) {
            e.printStackTrace();
            throw new IOException();
        }
        return userWrapper;
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
}
