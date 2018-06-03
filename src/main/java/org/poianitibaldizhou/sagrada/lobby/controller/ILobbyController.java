package org.poianitibaldizhou.sagrada.lobby.controller;

import org.poianitibaldizhou.sagrada.lobby.model.observers.ILobbyObserver;
import org.poianitibaldizhou.sagrada.IView;

import java.io.IOException;
import java.rmi.Remote;

public interface ILobbyController extends Remote {
    /**
     * Implements the login of an User with an username and a view.
     * If an User is already logged with username, returns an empty token (login is thus failed).
     *
     * @param message user's name
     * @param view     user's view
     * @return login's token
     * @throws IOException network communication error
     */
    String login(String  message, IView view) throws IOException;

    /**
     * An user identified with token and username leaves the lobby.
     *
     * @param message message containing username and user's token
     * @throws IOException if authorization fails or if there are problems in the communication
     *                     architecture
     */
    void leave(String message) throws IOException;

    /**
     * An user identified by token and username join the lobby. It also carries an lobbyObserver
     * in order to receive notification of what happens.
     * If the user has already joined, signals an error message to the user's view.
     *
     * @param message   message containing the user's token and the user's name
     * @param lobbyObserver observer of the lobby for the client
     * @throws IOException network communication error
     */
    void join(String message, ILobbyObserver lobbyObserver) throws IOException;

    /**
     * Return a list of user present in the lobby
     *
     * @throws IOException network communication error
     */
    String getUsersInLobby() throws IOException;

    /**
     * Returns the timeout
     *
     * @throws IOException network communication error
     */
    String getTimeout() throws IOException;
}
