package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import org.jetbrains.annotations.Contract;

public final class PlayerScoreWrapper extends RecursiveTreeObject<PlayerScoreWrapper> {

    private final String username;
    private final int totalScore;

    public PlayerScoreWrapper(String username, int totalScore) {
        this.username = username;
        this.totalScore = totalScore;
    }

    @Contract(pure = true)
    public int getTotalScore() {
        return totalScore;
    }

    @Contract(pure = true)
    public String getUsername() {
        return username;
    }
}
