package org.poianitibaldizhou.sagrada.network.protocol;

import org.apache.velocity.runtime.directive.Parse;
import org.json.simple.parser.ParseException;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;

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
}
