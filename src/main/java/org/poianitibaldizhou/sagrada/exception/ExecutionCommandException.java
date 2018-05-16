package org.poianitibaldizhou.sagrada.exception;

public class ExecutionCommandException extends Exception {

    private Exception innerException;

    public ExecutionCommandException(){

    }

    public ExecutionCommandException(RuleViolationException e) {
        innerException = e;
    }
}
