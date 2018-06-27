package org.poianitibaldizhou.sagrada.exception;

public class InvalidActionException extends Exception {

    private final Exception innerException;

    public InvalidActionException(){
        innerException = new Exception("Simple Invalid Action because it's not the player turn");
    }

    public InvalidActionException(Exception e) {
        innerException = e;
    }

    public Exception getException(){
        return innerException;
    }

}
