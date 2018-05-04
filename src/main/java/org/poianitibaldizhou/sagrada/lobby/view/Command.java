package org.poianitibaldizhou.sagrada.lobby.view;

import java.io.Serializable;
import java.rmi.RemoteException;

public class Command implements ICommandAction{
    private String commandText;
    private String helpText;
    private ICommandAction commandAction;

    public Command(String commandText, String helpText){
        this.commandText = commandText;
        this.helpText = helpText;
    }

    public String getCommandText() {
        return commandText;
    }

    public String getHelpText(){
        return helpText;
    }

    public void setCommandAction(ICommandAction commandAction){
        this.commandAction = commandAction;
    }

    @Override
    public void executeCommand() throws RemoteException {
        commandAction.executeCommand();
    }
}
