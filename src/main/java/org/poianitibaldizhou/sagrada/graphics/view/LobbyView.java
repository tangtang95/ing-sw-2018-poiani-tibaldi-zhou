package org.poianitibaldizhou.sagrada.graphics.view;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import org.poianitibaldizhou.sagrada.graphics.controller.LobbyGraphicsController;
import org.poianitibaldizhou.sagrada.graphics.utils.GraphicsUtils;
import org.poianitibaldizhou.sagrada.graphics.view.component.UserView;
import org.poianitibaldizhou.sagrada.lobby.view.ILobbyView;
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

/**
 * Lobby gui.
 */
public class LobbyView extends UnicastRemoteObject implements ILobbyView, ILobbyObserver{


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

    /**
     * equals method
     *
     * @param obj object to compare.
     * @return true if the obj is equal at this.
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof LobbyView;
    }

    /**
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }

    /**
     * Add user at the lobby.
     *
     * @param user to add.
     */
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

    /**
     * Clear the lobby user.
     */
    private void clearGrid(){
        userViews.forEach(pane -> {
            UserView userView = (UserView) pane;
            userView.drawRetro();
        });
        numberOfUsers = 0;
    }

    /**
     * Update the user view.
     */
    private void updateUserViews(){
        clearGrid();
        List<UserWrapper> users = controller.getUsers();
        users.forEach(this::addUser);
        if(numberOfUsers > 1){
            setTimeoutLabel();
        }
        else if(numberOfUsers == 1){
            hideTimeoutLabel();
        }
    }

    /**
     * Set the lobby timeout.
     */
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

    /**
     * hide time out to lobby.
     */
    private void hideTimeoutLabel() {
        controller.hideTimeoutLabel();
    }

    /**
     * @return the pane width.
     */
    private DoubleBinding getWidth() {
        return corePane.widthProperty().divide(1);
    }

    /**
     * @return th pane height.
     */
    private DoubleBinding getHeight() {
        return corePane.heightProperty().divide(1);
    }

    /**
     * @return the pane center on variable X.
     */
    private DoubleBinding getCenterX() {
        return corePane.widthProperty().divide(2);
    }

    /**
     * @return the pane center on variable Y.
     */
    private DoubleBinding getCenterY() {
        return corePane.heightProperty().divide(2);
    }

    /**
     * return the correct position for fixing an object on the screen.
     *
     * @param x listener on x position.
     * @param width listener on width.
     * @param pivotX system reference.
     * @return the correct position.
     */
    private DoubleBinding getPivotX(DoubleBinding x, DoubleBinding width, double pivotX) {
        return x.subtract(width.multiply(1 - pivotX));
    }

    /**
     * return the correct position for fixing an object on the screen.
     *
     * @param x listener on x position.
     * @param width listener on width.
     * @param pivotX system reference.
     * @return the correct position.
     */
    private DoubleBinding getPivotX(DoubleBinding x, ReadOnlyDoubleProperty width, double pivotX) {
        return x.subtract(width.multiply(1 - pivotX));
    }

    /**
     * return the correct position for fixing an object on the screen.
     *
     * @param y listener on y position.
     * @param height listener on height.
     * @param pivotY system reference.
     * @return the correct position.
     */
    private DoubleBinding getPivotY(DoubleBinding y, DoubleBinding height, double pivotY) {
        return y.subtract(height.multiply(1 - pivotY));
    }

    /**
     * return the correct position for fixing an object on the screen.
     *
     * @param y listener on y position.
     * @param height listener on height.
     * @param pivotY system reference.
     * @return the correct position.
     */
    private DoubleBinding getPivotY(DoubleBinding y, ReadOnlyDoubleProperty height, double pivotY) {
        return y.subtract(height.multiply(1 - pivotY));
    }

}
