package org.poianitibaldizhou.sagrada.graphics.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.graphics.model.LobbyModel;
import org.poianitibaldizhou.sagrada.graphics.view.LobbyView;
import org.poianitibaldizhou.sagrada.graphics.utils.AlertBox;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LobbyController extends Controller implements Initializable {

    @FXML
    public Pane corePane;

    private LobbyModel lobbyModel;
    private LobbyView lobbyView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            lobbyView = new LobbyView(this, corePane);
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
        }
    }

    public void setConnectionManager(String username, ConnectionManager connectionManager) {
        lobbyModel = new LobbyModel(username, connectionManager);
        stage.setOnCloseRequest((event -> {
            connectionManager.close();
        }));
        try {
            lobbyModel.login(lobbyView, lobbyView);
        } catch (IOException e) {
            AlertBox.displayBox("Errore di connessione", "L'operazione è fallita per problemi di connessione");
        }
    }

    public List<UserWrapper> getUsers() {
        List<UserWrapper> users = null;
        try {
            users =  lobbyModel.requestGetUsers();
        } catch (IOException e) {
            AlertBox.displayBox("Errore di connessione", "L'operazione è fallita per problemi di connessione");
        }
        return users;
    }

    @FXML
    public void onLeaveLobbyButton(ActionEvent actionEvent) {
        try {
            lobbyModel.leave();
        } catch (IOException e) {
            AlertBox.displayBox("Errore di connessione", "L'operazione è fallita per problemi di connessione");
        }
        playSceneTransition(corePane, event -> sceneManager.popScene());
    }

    public String getMyUsername() {
        return lobbyModel.getUsername();
    }

    public void gameStart(String gameName) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/game.fxml"));

        try {
            Parent root = loader.load();
            MultiPlayerController controller = loader.getController();
            controller.setStage(stage);
            controller.setSceneManager(sceneManager);
            controller.setMultiPlayerModel(lobbyModel.getToken(), lobbyModel.getUsername(), gameName, lobbyModel.getConnectionManager());
            playSceneTransition(sceneManager.getCurrentScene(), (event) -> sceneManager.pushScene(root));
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Cannot load FXML loader");
        }
    }
}
