package org.poianitibaldizhou.sagrada.exception;

public class RuleViolationException extends Exception {

    private final RuleViolationType violationType;

    public RuleViolationException(RuleViolationType violationType){
        this.violationType = violationType;
    }

    public RuleViolationException(String message, RuleViolationType violationType){
        super(message);
        this.violationType = violationType;
    }

    public RuleViolationType getViolationType() {
        return violationType;
    }
}
