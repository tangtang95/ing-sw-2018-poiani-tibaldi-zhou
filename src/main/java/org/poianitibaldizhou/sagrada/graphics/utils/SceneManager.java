package org.poianitibaldizhou.sagrada.graphics.utils;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayDeque;
import java.util.EmptyStackException;

public class SceneManager {

    private StackPane stackPane;
    private ArrayDeque<Parent> sceneStack;

    private WindowSize windowSize;

    /**
     * Constructor.
     * Create a SceneManager to managing all the scene inside the GUI application
     *
     * @param stackPane the stackPane from where to insert all the scenes
     * @param size      the window size of the GUI application
     */
    public SceneManager(StackPane stackPane, WindowSize size) {
        this.sceneStack = new ArrayDeque<>();
        this.stackPane = stackPane;
        this.windowSize = size;
    }

    /**
     * Push a new scene inside the stackPane and the sceneStack without removing anything
     *
     * @param newScene the new scene to add (the new scene goes in front)
     */
    public void pushScene(Parent newScene) {
        sceneStack.push(newScene);
        stackPane.getChildren().forEach(node -> node.setVisible(false));
        stackPane.getChildren().add(newScene);
        newScene.toFront();
    }

    /**
     * Remove the scene at the top of the sceneStack
     *
     * @return the scene removed
     */
    public Parent popScene() {
        if (sceneStack.isEmpty())
            throw new EmptyStackException();
        Parent oldScene = sceneStack.pop();
        stackPane.getChildren().remove(oldScene);
        stackPane.getChildren().forEach(node -> node.setVisible(false));
        sceneStack.peek().toFront();
        sceneStack.peek().setVisible(true);
        sceneStack.peek().setOpacity(1);
        return oldScene;
    }

    /**
     * Replace the top scene with a new scene passed through parameters
     *
     * @param newScene the new scene to replace the current one
     */
    public void replaceScene(Parent newScene) {
        popScene();
        pushScene(newScene);
    }

    public Stage getPrimaryStage() {
        return (Stage) stackPane.getScene().getWindow();
    }

    public double getSceneWidth() {
        return windowSize.getWidth();
    }

    public double getSceneHeight() {
        return windowSize.getHeight();
    }

    public Node getCurrentScene() {
        return sceneStack.peek();
    }
}
