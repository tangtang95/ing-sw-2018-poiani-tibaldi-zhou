package org.poianitibaldizhou.sagrada.graphics.view;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import org.poianitibaldizhou.sagrada.IView;
import org.poianitibaldizhou.sagrada.graphics.controller.LobbyGraphicsController;
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


    private transient LobbyGraphicsController controller;
    private transient Pane corePane;

    private transient List<Pane> userViews;
    private transient int numberOfUsers;

    private static final int COLUMNS = 4;

    private static final double RETRO_IMAGE_SCALE = 1;

    /**
     * Constructor.
     * Create a lobbyView that contains the user views and the timeout
     *
     * @param controller the lobby controller of the GUI
     * @param corePane the core view of the lobby
     * @throws RemoteException network error
     */
    public LobbyView(LobbyGraphicsController controller, Pane corePane) throws RemoteException {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void ack(String ack) throws IOException {
        Logger.getAnonymousLogger().log(Level.FINEST, ack);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUserJoin(String message) throws IOException {
        Platform.runLater(this::updateUserViews);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUserExit(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        UserWrapper user = parser.getUserWrapper(message);
        if(!user.getUsername().equals(controller.getMyUsername())) {
            Platform.runLater(this::updateUserViews);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onGameStart(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        final String gameName = parser.getGameName(message);

        Platform.runLater(() -> controller.gameStart(gameName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPing() throws IOException {
        Logger.getAnonymousLogger().log(Level.FINEST, "onPing");
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof LobbyView;
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }

    private void addUser(UserWrapper user) {
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

    private void updateUserViews(){
        clearGrid();
        List<UserWrapper> users = controller.getUsers();
        users.forEach(this::addUser);
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
            controller.onTimeoutSet(serverTimeout*1000 - delayTime);
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
        }
    }

    private void hideTimeoutLabel() {
        controller.hideTimeoutLabel();
    }

    private DoubleBinding getWidth() {
        return corePane.widthProperty().divide(1);
    }

    private DoubleBinding getHeight() {
        return corePane.heightProperty().divide(1);
    }

    private DoubleBinding getCenterX() {
        return corePane.widthProperty().divide(2);
    }

    private DoubleBinding getCenterY() {
        return corePane.heightProperty().divide(2);
    }

    private DoubleBinding getPivotX(DoubleBinding x, DoubleBinding width, double pivotX) {
        return x.subtract(width.multiply(1 - pivotX));
    }

    private DoubleBinding getPivotX(DoubleBinding x, ReadOnlyDoubleProperty width, double pivotX) {
        return x.subtract(width.multiply(1 - pivotX));
    }

    private DoubleBinding getPivotY(DoubleBinding y, DoubleBinding height, double pivotY) {
        return y.subtract(height.multiply(1 - pivotY));
    }

    private DoubleBinding getPivotY(DoubleBinding y, ReadOnlyDoubleProperty height, double pivotY) {
        return y.subtract(height.multiply(1 - pivotY));
    }

}
