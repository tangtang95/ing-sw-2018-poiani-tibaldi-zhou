package org.poianitibaldizhou.sagrada.graphics.utils;

import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayDeque;
import java.util.EmptyStackException;

public class SceneManager {

    private StackPane stackPane;
    private ArrayDeque<Parent> sceneStack;

    private WindowSize windowSize;

    public SceneManager(StackPane stackPane, WindowSize size){
        this.sceneStack = new ArrayDeque<>();
        this.stackPane = stackPane;
        this.windowSize = size;
    }

    public void pushScene(Parent newScene){
        sceneStack.push(newScene);
        stackPane.getChildren().forEach(node -> node.setVisible(false));
        stackPane.getChildren().add(newScene);
        newScene.toFront();
    }

    public Parent popScene(){
        if(sceneStack.isEmpty())
            throw new EmptyStackException();
        Parent oldScene = sceneStack.pop();
        stackPane.getChildren().remove(oldScene);
        stackPane.getChildren().forEach(node-> node.setVisible(false));
        sceneStack.peek().toFront();
        sceneStack.peek().setVisible(true);
        return oldScene;
    }

    public void replaceScene(Parent newScene){
        popScene();
        pushScene(newScene);
    }

    public Stage getPrimaryStage(){
        return (Stage) stackPane.getScene().getWindow();
    }

    public int getSceneWidth(){
        return windowSize.getWidth();
    }

    public int getSceneHeight(){
        return windowSize.getHeight();
    }

}
