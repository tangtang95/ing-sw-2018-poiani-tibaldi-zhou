package org.poianitibaldizhou.sagrada.lobby.model;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.jetbrains.annotations.Contract;

import java.io.Serializable;
import java.util.Objects;

@Immutable
public class User implements Serializable {
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
}
