package org.poianitibaldizhou.sagrada.graphics.objects;

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
import org.poianitibaldizhou.sagrada.lobby.model.ILobbyObserver;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class LobbyView extends UnicastRemoteObject implements IView, ILobbyObserver{

    private LobbyController controller;

    private GridPane usersPane;

    private int numberOfUsersOnGrid;

    private static final int COLUMNS = 2;

    public LobbyView(LobbyController controller, GridPane usersPane) throws RemoteException {
        this.controller = controller;
        this.usersPane = usersPane;
        this.numberOfUsersOnGrid = 0;
    }

    public void addUser(String username) {
        int row = numberOfUsersOnGrid%COLUMNS;
        int column = numberOfUsersOnGrid/COLUMNS;

        VBox container = new VBox(20);
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

        usersPane.add(container, row, column);
        numberOfUsersOnGrid++;
    }

    private void clearGrid(){
        usersPane.getChildren().clear();
        numberOfUsersOnGrid = 0;
    }

    @Override
    public void ack(String ack) throws IOException {

    }

    @Override
    public void err(String err) throws IOException {

    }

    @Override
    public void onUserJoin(User user) throws IOException {
        clearGrid();
        List<String> users = controller.getUsers();
        users.forEach(this::addUser);
    }

    @Override
    public void onUserExit(User user) throws IOException {
        if(!user.getName().equals(controller.getMyUsername())) {
            clearGrid();
            List<String> users = controller.getUsers();
            users.forEach(this::addUser);
        }
    }

    @Override
    public void onGameStart(String gameName) throws IOException {

    }

    @Override
    public void onPing() throws IOException {

    }
}
