package org.poianitibaldizhou.sagrada.cli;

import java.rmi.RemoteException;

/**
 * OVERVIEW: Represents a command that the user can execute in the CLI to interact with the game,
 * the menus and the lobby
 */
public interface ICommandAction {

    /**
     * Execute a command that allows the user to interact with Sagrada client.
     *
     * @throws RemoteException network communication error
     */
    void executeCommand() throws RemoteException;
}
