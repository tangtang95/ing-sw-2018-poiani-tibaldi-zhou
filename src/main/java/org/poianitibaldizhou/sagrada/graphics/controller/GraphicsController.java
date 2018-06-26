package org.poianitibaldizhou.sagrada.graphics.controller;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.util.Duration;
import org.poianitibaldizhou.sagrada.graphics.utils.SceneManager;

public abstract class GraphicsController {

    protected SceneManager sceneManager;

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    /**
     * Standard scene transition
     *
     * @param node the node target of the transition
     * @param eventHandler the event to do after the transition end
     */
    public void playSceneTransition(Node node, EventHandler<ActionEvent> eventHandler){
        FadeTransition transition = new FadeTransition();
        transition.setDuration(Duration.millis(400));
        transition.setNode(node);
        transition.setToValue(0);
        transition.setByValue(1);
        transition.play();
        transition.setOnFinished(eventHandler);
    }

}

