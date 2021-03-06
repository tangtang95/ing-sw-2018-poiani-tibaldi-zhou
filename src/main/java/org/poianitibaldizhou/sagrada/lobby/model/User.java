package org.poianitibaldizhou.sagrada.lobby.model;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.network.protocol.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;
import org.poianitibaldizhou.sagrada.utilities.NetworkUtility;

import java.util.Objects;

/**
 * OVERVIEW: represents an user with an username and a token.
 * Username and token can't be null
 */
@Immutable
public class User implements JSONable {
    private String name;
    private String token;

    /**
     * User param for network protocol.
     */
    private static final String JSON_USER_NAME = "userName";

    /**
     * Constructor.
     * Creates a new User with a certain name and token.
     *
     * @param name  User's name
     * @param token User's token
     */
    public User(@NotNull String name, @NotNull String token) {
        this.name = name;
        this.token = token;
    }

    /**
     * @return user's token
     */
    @Contract(pure = true)
    public String getToken() {
        return token;
    }

    /**
     * @return user's name
     */
    @Contract(pure = true)
    public String getName() {
        return name;
    }

    /**
     * Two users are equals if they have same user name and token
     *
     * @param object object that need to be compared
     * @return true if this is equals object, false otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof User))
            return false;
        User u = (User) object;
        return u.getName().equals(this.name) && u.getToken().equals(this.getToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, token);
    }

    @Override
    public String toString() {
        return "Username: " + this.name + " ;" + this.token;
    }

    /**
     * Convert a User in a JSONObject.
     *
     * @return a JSONObject.
     */
    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject main = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        jsonObject.putIfAbsent(JSON_USER_NAME, this.getName());
        main.put(SharedConstants.TYPE, SharedConstants.USER);
        main.put(SharedConstants.BODY, jsonObject);
        return main;
    }

    /**
     * Convert a json string in a User object.
     *
     * @param jsonObject a JSONObject that contains a User.
     * @return a User object.
     */
    public static User toObject(JSONObject jsonObject) {
        return new User(jsonObject.get(JSON_USER_NAME).toString(), NetworkUtility.encryptUsername(jsonObject.get(JSON_USER_NAME).toString()));
    }
}
