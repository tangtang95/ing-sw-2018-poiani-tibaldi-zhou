package org.poianitibaldizhou.sagrada.graphics.view;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import org.poianitibaldizhou.sagrada.IView;
import org.poianitibaldizhou.sagrada.graphics.controller.LobbyController;
import org.poianitibaldizhou.sagrada.graphics.utils.GraphicsUtils;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.ILobbyObserver;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LobbyView extends UnicastRemoteObject implements IView, ILobbyObserver{


    private transient LobbyController controller;
    private transient Pane corePane;

    private transient List<Pane> userViews;
    private transient int numberOfUsers;

    private static final int COLUMNS = 4;

    private static final double RETRO_IMAGE_SCALE = 0.6;

    public LobbyView(LobbyController controller, Pane corePane) throws RemoteException {
        this.controller = controller;
        this.corePane = corePane;
        this.userViews = new ArrayList<>();
        for (int i = 0; i < COLUMNS; i++) {
            UserView userView = new UserView(RETRO_IMAGE_SCALE);
            this.userViews.add(userView);
        }
        GraphicsUtils.drawCenteredPanes(corePane, userViews, "on-board-pane", getCenterX(),
                getPivotY(getCenterY(), userViews.get(0).heightProperty(), 0.5));

        this.numberOfUsers = 0;
    }

    public void addUser(UserWrapper user) {
        UserView userView = (UserView) userViews.get(numberOfUsers);
        userView.drawRetro();
        RotateTransition rotator = new RotateTransition(Duration.millis(500), userView);
        rotator.setAxis(Rotate.Y_AXIS);
        rotator.setFromAngle(0);
        rotator.setToAngle(90);
        rotator.setInterpolator(Interpolator.LINEAR);
        rotator.setCycleCount(1);
        rotator.play();
        rotator.setOnFinished(event -> {
            userView.drawUserCanvas(user);
            RotateTransition secondRotator = new RotateTransition(Duration.millis(500), userView);
            secondRotator.setAxis(Rotate.Y_AXIS);
            secondRotator.setFromAngle(270);
            secondRotator.setToAngle(360);
            secondRotator.setInterpolator(Interpolator.LINEAR);
            secondRotator.setCycleCount(1);
            secondRotator.play();
        });

        numberOfUsers++;
    }

    private void clearGrid(){
        numberOfUsers = 0;
    }

    @Override
    public void ack(String ack) throws IOException {
        Logger.getAnonymousLogger().log(Level.FINEST, ack);
    }

    @Override
    public void err(String err) throws IOException {
        Logger.getAnonymousLogger().log(Level.FINEST, err);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ping() throws IOException {
        // DO NOTHING
    }

    @Override
    public void onUserJoin(String message) throws IOException {
        Platform.runLater(this::updateUserViews);

    }

    @Override
    public void onUserExit(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        UserWrapper user = parser.getUserWrapper(message);
        if(!user.getUsername().equals(controller.getMyUsername())) {
            Platform.runLater(this::updateUserViews);
        }
    }

    @Override
    public void onGameStart(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        final String gameName = parser.getGameName(message);

        Platform.runLater(() -> {
            controller.gameStart(gameName);
        });
    }

    @Override
    public void onPing() throws IOException {
        Logger.getAnonymousLogger().log(Level.FINEST, "onPing");
    }

    private void updateUserViews(){
        clearGrid();
        List<UserWrapper> users = controller.getUsers();
        users.stream().forEach(this::addUser);
        if(numberOfUsers > 1){
            setTimeoutLabel();
        }
        else if(numberOfUsers == 0){
            hideTimeoutLabel();
        }
    }

    private void setTimeoutLabel(){
        try {
            long currentTime = System.currentTimeMillis();
            int serverTimeout = controller.getTimeout();
            long delayTime = System.currentTimeMillis() - currentTime;
            System.out.println(delayTime);
            System.out.println(serverTimeout);
            System.out.println(serverTimeout*1000 - delayTime);
            controller.onTimeoutSet(serverTimeout*1000 - delayTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void hideTimeoutLabel() {
        controller.hideTimeoutLabel();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof LobbyView;
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }

    protected DoubleBinding getWidth() {
        return corePane.widthProperty().divide(1);
    }

    protected DoubleBinding getHeight() {
        return corePane.heightProperty().divide(1);
    }

    protected DoubleBinding getCenterX() {
        return corePane.widthProperty().divide(2);
    }

    protected DoubleBinding getCenterY() {
        return corePane.heightProperty().divide(2);
    }

    protected DoubleBinding getPivotX(DoubleBinding x, DoubleBinding width, double pivotX) {
        return x.subtract(width.multiply(1 - pivotX));
    }

    protected DoubleBinding getPivotX(DoubleBinding x, ReadOnlyDoubleProperty width, double pivotX) {
        return x.subtract(width.multiply(1 - pivotX));
    }

    protected DoubleBinding getPivotY(DoubleBinding y, DoubleBinding height, double pivotY) {
        return y.subtract(height.multiply(1 - pivotY));
    }

    protected DoubleBinding getPivotY(DoubleBinding y, ReadOnlyDoubleProperty height, double pivotY) {
        return y.subtract(height.multiply(1 - pivotY));
    }

}
