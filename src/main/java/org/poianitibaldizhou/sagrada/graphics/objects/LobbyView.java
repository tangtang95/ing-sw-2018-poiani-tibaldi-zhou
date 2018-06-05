package org.poianitibaldizhou.sagrada.graphics.objects;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
    private transient List<VBox> userViews;
    private int numberOfUsers;

    private static final int COLUMNS = 2;

    public LobbyView(LobbyController controller, GridPane usersPane) throws RemoteException {
        this.controller = controller;
        this.usersPane = usersPane;
        this.userViews = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            VBox container = new VBox(20);
            container.setAlignment(Pos.CENTER);
            this.userViews.add(container);
            this.usersPane.add(container, i/COLUMNS, i%COLUMNS);
        }

        this.numberOfUsers = 0;

    }

    public void addUser(String username) {
        VBox container = userViews.get(numberOfUsers);
        container.setAlignment(Pos.CENTER);
        Image userImage = new Image(getClass().getClassLoader().getResourceAsStream("images/user.png"));
        ImageView userView = new ImageView(userImage);
        userView.setFitHeight(userImage.getHeight()*0.3);
        userView.setFitWidth(userImage.getWidth()*0.3);

        Label usernameLabel = new Label(username);
        usernameLabel.setStyle("-fx-font-size: 200%");

        DropShadow dropShadowEffect = new DropShadow();
        dropShadowEffect.setOffsetY(3.0);
        dropShadowEffect.setOffsetX(3.0);
        dropShadowEffect.setColor(Color.GRAY);
        Label readyLabel = new Label("READY!");
        readyLabel.setStyle("-fx-font-size: 200%; -fx-font-weight: bold");
        readyLabel.setEffect(dropShadowEffect);
        container.getChildren().addAll(usernameLabel, userView, readyLabel);

        numberOfUsers++;
    }

    private void clearGrid(){
        userViews.forEach(vBox -> vBox.getChildren().clear());
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
