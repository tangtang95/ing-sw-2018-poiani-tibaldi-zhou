package org.poianitibaldizhou.sagrada.cli;

import java.rmi.RemoteException;

/**
 * OVERVIEW: Represents a command, identified by a certain text, an help text and a concrete
 * command that can be executed
 */
public class Command implements ICommandAction {

    private String commandText;
    private String helpText;
    private ICommandAction commandAction;

    /**
     * Constructor.
     * Creates a command with a text and an help text, that can be used by the client in order
     * to understand its purpose
     *
     * @param commandText command's name
     * @param helpText    help text that is necessary for allowing to understand the purpose of
     *                    the command
     */
    public Command(String commandText, String helpText) {
        this.commandText = commandText;
        this.helpText = helpText;
    }

    /**
     * @return command's name
     */
    public String getCommandText() {
        return commandText;
    }

    /**
     * @return command's description
     */
    public String getHelpText() {
        return helpText;
    }

    /**
     * Set the action that this command will execute
     *
     * @param commandAction action that this command will execute, when executeCommand() will
     *                      be called
     */
    public void setCommandAction(ICommandAction commandAction) {
        this.commandAction = commandAction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeCommand() throws RemoteException {
        commandAction.executeCommand();
    }
}
