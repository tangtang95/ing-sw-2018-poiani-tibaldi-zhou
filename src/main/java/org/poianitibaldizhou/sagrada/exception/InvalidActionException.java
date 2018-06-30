package org.poianitibaldizhou.sagrada.exception;

/**
 * When a client does an invalid action (e.g. player1 tries to place a dice when player2 is playing)
 */
public class InvalidActionException extends Exception {

    private final Exception innerException;

    /**
     * Constructor.
     * Creates an invalid action exception with a default inner exception explaining that it's not athe player turn for making
     * an action.
     */
    public InvalidActionException() {
        innerException = new Exception("Simple Invalid Action because it's not the player turn");
    }

    /**
     * Constructor.
     * Creates an invalid action exception with an inner exception
     *
     * @param e inner exception
     */
    public InvalidActionException(Exception e) {
        innerException = e;
    }

    /**
     * @return inner exception
     */
    public Exception getException() {
        return innerException;
    }

}
