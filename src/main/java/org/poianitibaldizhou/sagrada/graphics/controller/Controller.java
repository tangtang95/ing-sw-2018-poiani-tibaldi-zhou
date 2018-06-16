package org.poianitibaldizhou.sagrada.graphics.controller;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.poianitibaldizhou.sagrada.graphics.utils.SceneManager;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;

import java.util.Map;

public abstract class Controller {

    protected Stage stage;
    protected SceneManager sceneManager;

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public void playSceneTransition(Node node, EventHandler<ActionEvent> eventHandler){
        FadeTransition transition = new FadeTransition();
        transition.setDuration(Duration.millis(400));
        transition.setNode(node);
        transition.setToValue(0);
        transition.setByValue(1);
        transition.play();
        transition.setOnFinished(eventHandler);
    }

    public void pushScene(Parent node){
        sceneManager.pushScene(node);
    }

    public void popScene() {
        sceneManager.popScene();
    }

}

