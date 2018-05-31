package org.poianitibaldizhou.sagrada.lobby.model;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.jetbrains.annotations.Contract;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;

import java.io.Serializable;
import java.util.Objects;

@Immutable
public class User implements Serializable, JSONable {
    private String name;
    private transient String token;

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

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putIfAbsent("user", this.getName());
        jsonObject.putIfAbsent("token", this.getToken());
        return jsonObject;
    }

    @Override
    public Object toObject(JSONObject jsonObject) {
        return null;
    }
}
