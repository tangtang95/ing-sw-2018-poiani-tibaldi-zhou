package org.poianitibaldizhou.sagrada.graphics.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.poianitibaldizhou.sagrada.exception.NetworkException;
import org.poianitibaldizhou.sagrada.graphics.model.LobbyModel;
import org.poianitibaldizhou.sagrada.graphics.utils.AlertBox;
import org.poianitibaldizhou.sagrada.graphics.view.LobbyView;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * OVERVIEW: Graphic controller for handling the lobby phase of Sagrada
 */
public class LobbyGraphicsController extends GraphicsController implements Initializable {

    @FXML
    public Pane corePane;
    @FXML
    public Label labelTimeout;

    private Timeline timeoutAnimation;

    private LobbyModel lobbyModel;
    private LobbyView lobbyView;

    private static final String TITLE_BOX = "Errore di connessione";

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelTimeout.getParent().setVisible(false);
        try {
            lobbyView = new LobbyView(this, corePane);
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Init method for instantiation of Lobby
     *
     * @param username          the username chosen
     * @param connectionManager the manager of the connection
     * @throws NetworkException if cannot connect to server or the username is already in use
     */
    public void initLobbyModel(String username, ConnectionManager connectionManager) throws NetworkException {
        lobbyModel = new LobbyModel(username, connectionManager);
        sceneManager.getPrimaryStage().setOnCloseRequest((event -> connectionManager.close()));
        try {
            lobbyModel.login(lobbyView, lobbyView);
        } catch (IOException e) {
            throw new NetworkException(e);
        }
    }

    /**
     * @return the list of user inside the lobby
     */
    public List<UserWrapper> getUsers() {
        List<UserWrapper> users = null;
        try {
            users = lobbyModel.getUsers();
        } catch (IOException e) {
            AlertBox.displayBox(TITLE_BOX, ClientMessage.OPERATION_ERROR);
        }
        return users;
    }

    /**
     * Get the user name of the user who is playing with this client
     *
     * @return user name of the user who is playing with this client
     */
    public String getMyUsername() {
        return lobbyModel.getUsername();
    }

    /**
     * Change the scene to the GameScene
     *
     * @param gameName the name of the game
     */
    public void gameStart(String gameName) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("src/test/fxml/game.fxml"));

        try {
            Parent root = loader.load();
            GameGraphicsController controller = loader.getController();
            controller.setSceneManager(sceneManager);
            controller.initMultiPlayerGame(lobbyModel.getToken(), lobbyModel.getUsername(), gameName, lobbyModel.getConnectionManager());
            playSceneTransition(sceneManager.getCurrentScene(), event -> sceneManager.replaceScene(root));
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, ClientMessage.LOAD_FXML_ERROR);
        } catch (NetworkException e) {
            AlertBox.displayBox(TITLE_BOX, ClientMessage.CONNECTION_ERROR);
        }
    }

    /**
     * Initialize the timeout
     *
     * @param millisTime the remaining time until the game start
     */
    public void onTimeoutSet(final long millisTime) {
        labelTimeout.setText(String.valueOf(Math.round(millisTime / 1000.0)));
        long startTime = System.currentTimeMillis();
        if (timeoutAnimation != null)
            timeoutAnimation.stop();

        timeoutAnimation = new Timeline(new KeyFrame(
                Duration.millis(500),
                event -> {
                    long deltaTime = millisTime - (System.currentTimeMillis() - startTime);
                    if (deltaTime < 0) {
                        labelTimeout.setText("00");
                    } else {
                        labelTimeout.setText(String.format("%02d", Math.round(deltaTime / 1000.0)));
                    }

                }
        ));
        timeoutAnimation.setCycleCount(Animation.INDEFINITE);
        timeoutAnimation.play();
        labelTimeout.getParent().setVisible(true);
    }

    /**
     * Action associated to the leave button. Leaves the lobby and pop the scene
     *
     * @param actionEvent not used
     */
    @FXML
    public void onLeaveButtonAction(ActionEvent actionEvent) {
        try {
            lobbyModel.leave();
        } catch (IOException e) {
            AlertBox.displayBox(TITLE_BOX, ClientMessage.OPERATION_ERROR);
        }
        playSceneTransition(corePane, event -> sceneManager.popScene());
    }

    /**
     * Get timeout in millis
     *
     * @return timeout in millis
     * @throws IOException network communication error
     */
    public int getTimeout() throws IOException {
        return lobbyModel.getTimeout();
    }

    /**
     * Hide the label of the timeout
     */
    public void hideTimeoutLabel() {
        labelTimeout.getParent().setVisible(false);
    }
}
