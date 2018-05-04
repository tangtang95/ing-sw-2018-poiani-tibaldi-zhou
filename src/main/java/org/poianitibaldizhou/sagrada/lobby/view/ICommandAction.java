package org.poianitibaldizhou.sagrada.lobby.view;

import java.io.Serializable;
import java.rmi.RemoteException;

public interface ICommandAction {

    void executeCommand() throws RemoteException;

}
