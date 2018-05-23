package org.poianitibaldizhou.sagrada.exception;

public class InvalidActionException extends Exception {

    private final Exception ruleViolationException;

    public InvalidActionException(){
        ruleViolationException = new Exception("Simple Invalid Action because it's not the player turn");
    }

    public InvalidActionException(Exception e) {
        ruleViolationException = e;
    }

    public Exception getException(){
        return ruleViolationException;
    }

}
