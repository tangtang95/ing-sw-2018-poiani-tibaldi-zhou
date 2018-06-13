package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.network.protocol.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

import java.util.Objects;

/**
 * Copy class of User in the lobby model.
 */
@Immutable
public final class UserWrapper implements JSONable{

    /**
     * Username of player in game.
     */
    private final String username;

    /**
     * User param for network protocol.
     */
    private static final String JSON_USER_NAME = "userName";

    /**
     * Constructor.
     *
     * @param username name of player.
     */
    public UserWrapper(String username) {
        this.username = username;
    }

    /**
     * @return the userWrapper username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Convert a UserWrapper in a JSONObject.
     *
     * @return a JSONObject.
     */
    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject main = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        jsonObject.putIfAbsent(JSON_USER_NAME, this.getUsername());
        main.put(SharedConstants.TYPE, SharedConstants.USER);
        main.put(SharedConstants.BODY,jsonObject);
        return main;
    }

    /**
     * Convert a json string in a UserWrapper object.
     *
     * @param jsonObject a JSONObject that contains a UserWrapper.
     * @return a UserWrapper object.
     */
    public static UserWrapper toObject(JSONObject jsonObject) {
        return new UserWrapper(jsonObject.get(JSON_USER_NAME).toString());
    }

    /**
     * @param o the other object to compare.
     * @return true if the UserWrapper is the same object or the username is the same.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserWrapper)) return false;
        UserWrapper that = (UserWrapper) o;
        return getUsername().equals(that.getUsername());
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }

    /**
     * @return UserWrapper toString -> username
     */
    @Override
    public String toString() {
        return username;
    }
}
