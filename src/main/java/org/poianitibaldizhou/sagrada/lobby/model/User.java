package org.poianitibaldizhou.sagrada.lobby.model;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.jetbrains.annotations.Contract;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

import java.io.Serializable;
import java.util.Objects;

@Immutable
public class User implements Serializable, JSONable {
    private String name;
    private transient String token;

    /**
     * User param for network protocol.
     */
    private static final String JSON_USER_NAME = "userName";

    /**
     * Constructor.
     * Creates a new User with a certain name and token.
     *
     * @param name User's name
     * @param token User's token
     */
    public User(String name, String token) {
        this.name = name;
        this.token = token;
    }

    /**
     * Constructor.
     * Creates a new User with a certain name and token.
     *
     * @param name User's name
     */
    public User(String name) {
        this.name = name;
    }

    @Contract(pure = true)
    public String getToken() {
        return token;
    }

    @Contract(pure = true)
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof User))
            return false;
        User u = (User)object;
        return u.getName().equals(this.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, token);
    }

    @Override
    public String toString() {
        return "Username: " + this.name;
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
        main.put(SharedConstants.BODY,jsonObject);
        return main;
    }

    /**
     * Convert a json string in a User object.
     *
     * @param jsonObject a JSONObject that contains a User.
     * @return a User object.
     */
    @Override
    public Object toObject(JSONObject jsonObject) {
        return new User(jsonObject.get(JSON_USER_NAME).toString());
    }

    /**
     * Fake constructor.
     */
    @SuppressWarnings("unused")
    private User(){}
}
