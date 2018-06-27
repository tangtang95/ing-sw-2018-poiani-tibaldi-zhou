package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import org.jetbrains.annotations.Contract;

/**
 * OVERVIEW: Represents an user associated with his score
 */
public final class PlayerScoreWrapper extends RecursiveTreeObject<PlayerScoreWrapper> {

    private final String username;
    private final int totalScore;

    /**
     * Constructor.
     * Creates a wrapper for the player score
     *
     * @param username   user name of the player
     * @param totalScore player's score
     */
    public PlayerScoreWrapper(String username, int totalScore) {
        this.username = username;
        this.totalScore = totalScore;
    }

    /**
     * Returns the score of the player
     *
     * @return return the score of the player
     */
    @Contract(pure = true)
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * Returns the user name of the player
     *
     * @return player's username
     */
    @Contract(pure = true)
    public String getUsername() {
        return username;
    }
}
