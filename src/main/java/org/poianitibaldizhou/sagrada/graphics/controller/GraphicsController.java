package org.poianitibaldizhou.sagrada.graphics.controller;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.util.Duration;
import org.poianitibaldizhou.sagrada.graphics.utils.SceneManager;

/**
 * OVERVIEW: Represents the standard controller that regroups the basic operations needed to various controller
 * implemented
 */
public abstract class GraphicsController {

    SceneManager sceneManager;

    /**
     * Set the scene manager used to pass from a scene to another one
     *
     * @param sceneManager scene manager that will be set
     */
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

