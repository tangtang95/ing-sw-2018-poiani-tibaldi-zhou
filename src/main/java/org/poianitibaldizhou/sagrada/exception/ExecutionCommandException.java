package org.poianitibaldizhou.sagrada.exception;

import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;

public class ExecutionCommandException extends Exception {

    private Exception innerException;
    private CommandFlow error;

    public ExecutionCommandException(CommandFlow error){
        this.error = error;
    }

    public CommandFlow getError() {
        return error;
    }
}
