package org.poianitibaldizhou.sagrada.graphics.view.component;

import com.jfoenix.controls.JFXSpinner;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * OVERVIEW: Represents the view for the timeout
 */
public class TimeoutView extends StackPane {

    private Label timeoutLabel;
    private Timeline timeoutAnimation;
    private long delayTime;
    private long startTime;

    /**
     * Constructor.
     * Create a TimeoutView (pane) that shows a count down
     */
    public TimeoutView(){
        timeoutLabel = new Label();
        timeoutLabel.getStyleClass().add("big-title");
        JFXSpinner spinner = new JFXSpinner(ProgressIndicator.INDETERMINATE_PROGRESS);
        spinner.setRadius(40);

        timeoutAnimation = new Timeline(new KeyFrame(
                Duration.millis(500),
                event -> {
                    long deltaTime = delayTime - (System.currentTimeMillis() - startTime);
                    if(deltaTime < 0){
                        startTimeout(0);
                    }
                    else{
                        startTimeout(deltaTime);
                    }

                }
        ));
        timeoutAnimation.setCycleCount(Animation.INDEFINITE);

        this.setMinHeight(80);
        this.setMinWidth(80);
        this.getChildren().addAll(timeoutLabel, spinner);
    }

    /**
     * Start the count down from the time value passed
     * @param millisTime the time value in millis
     */
    public void startTimeout(long millisTime){
        this.setVisible(true);
        delayTime = millisTime;
        timeoutLabel.setText(String.format("%02d", Math.round(millisTime/1000.0)));
        startTime = System.currentTimeMillis();
        timeoutAnimation.play();
    }

    /**
     * Stop the count down and hide the timeoutView
     */
    public void stopTimeout(){
        timeoutAnimation.stop();
        this.setVisible(false);
    }
}
