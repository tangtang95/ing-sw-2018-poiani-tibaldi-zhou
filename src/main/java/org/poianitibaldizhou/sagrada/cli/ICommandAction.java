package org.poianitibaldizhou.sagrada.cli;

import java.io.Serializable;
import java.rmi.RemoteException;

public interface ICommandAction {

    void executeCommand() throws RemoteException;

}
