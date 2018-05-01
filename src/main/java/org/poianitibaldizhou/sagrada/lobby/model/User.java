package org.poianitibaldizhou.sagrada.lobby.model;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String token;

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

    public String getToken() {
        return token;
    }

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
}
