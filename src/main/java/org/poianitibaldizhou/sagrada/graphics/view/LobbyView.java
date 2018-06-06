package org.poianitibaldizhou.sagrada.graphics.view;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import org.poianitibaldizhou.sagrada.IView;
import org.poianitibaldizhou.sagrada.graphics.controller.LobbyController;
import org.poianitibaldizhou.sagrada.lobby.model.observers.ILobbyObserver;
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
    private transient GridPane usersPane;
    private transient List<Canvas> userViews;
    private int numberOfUsers;

    private static final int COLUMNS = 4;

    public LobbyView(LobbyController controller, GridPane usersPane) throws RemoteException {
        this.controller = controller;
        this.usersPane = usersPane;
        this.userViews = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Canvas container = getUserContainer(i + 1);
            this.userViews.add(container);
            this.usersPane.add(container, i%COLUMNS, i/COLUMNS);
            this.usersPane.getColumnConstraints().forEach(columnConstraints -> columnConstraints.setHalignment(HPos.CENTER));
            this.usersPane.getRowConstraints().forEach(rowConstraints -> rowConstraints.setValignment(VPos.CENTER));
        }

        this.numberOfUsers = 0;
    }

    public void addUser(String username) {
        Canvas canvas = userViews.get(numberOfUsers);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        RotateTransition rotator = new RotateTransition(Duration.millis(500), canvas);
        rotator.setAxis(Rotate.Y_AXIS);
        rotator.setByAngle(90);
        rotator.setInterpolator(Interpolator.LINEAR);
        rotator.setCycleCount(1);
        rotator.play();
        rotator.setOnFinished(event -> {
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);

            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.setFill(Color.WHITE);
            gc.fillRect(0,0 , canvas.getWidth(), canvas.getHeight());
            Image userImage = new Image(getClass().getClassLoader().getResourceAsStream("images/user.png"));
            gc.drawImage(userImage, canvas.getWidth()/2, canvas.getHeight()/2,
                    userImage.getWidth()*0.3, userImage.getHeight()*0.3);
            gc.setFill(Color.BLACK);
            gc.setFont(Font.font(40));

            gc.fillText(username, canvas.getWidth()/2, canvas.getHeight()/3);
            gc.fillText("READY!", canvas.getWidth()/2, canvas.getHeight()*2/3);


            DropShadow dropShadowEffect = new DropShadow();
            dropShadowEffect.setOffsetY(3.0);
            dropShadowEffect.setOffsetX(3.0);
            dropShadowEffect.setColor(Color.GRAY);
            gc.applyEffect(dropShadowEffect);

            RotateTransition secondRotator = new RotateTransition(Duration.millis(500), canvas);
            secondRotator.setAxis(Rotate.Y_AXIS);
            secondRotator.setFromAngle(270);
            secondRotator.setToAngle(360);
            secondRotator.setInterpolator(Interpolator.LINEAR);
            secondRotator.setCycleCount(1);
            secondRotator.play();
        });

        numberOfUsers++;
    }

    private Canvas getUserContainer(int numberOfUser){
        Image retroImage = new Image(getClass().getClassLoader().getResourceAsStream("images/userRetro.png"));
        // TODO FIX 0.7
        double width = retroImage.getWidth()*0.7;
        double height = retroImage.getHeight()*0.7;

        Canvas canvas = new Canvas( width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();


        gc.drawImage(retroImage, 0, 0, width, height);
        gc.setFont(Font.font(80));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(String.valueOf(numberOfUser), width/2, height/2);

        return canvas;
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

    @Override
    public void onUserJoin(String message) throws IOException {
        Platform.runLater(() -> {
            clearGrid();
            List<UserWrapper> users = controller.getUsers();
            users.stream().map(UserWrapper::getUsername).forEach(this::addUser);
        });

    }

    @Override
    public void onUserExit(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        UserWrapper user = parser.getUserWrapper(message);
        if(!user.getUsername().equals(controller.getMyUsername())) {
            Platform.runLater(() -> {
                clearGrid();
                List<UserWrapper> users = controller.getUsers();
                users.stream().map(UserWrapper::getUsername).forEach(this::addUser);
            });
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

    @Override
    public boolean equals(Object obj) {
        return obj instanceof LobbyView;
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }
}
