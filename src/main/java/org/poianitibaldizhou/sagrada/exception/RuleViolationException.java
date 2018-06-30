package org.poianitibaldizhou.sagrada.exception;

/**
 * Rule violation exception
 */
public class RuleViolationException extends Exception {

    private final RuleViolationType violationType;

    /**
     * Creates a rule violation exception that happened due to a violation of a certain
     * rule of Sagrada.
     *
     * @param violationType rule violated
     */
    public RuleViolationException(RuleViolationType violationType){
        this.violationType = violationType;
    }

    /**
     * @return rule violated
     */
    public RuleViolationType getViolationType() {
        return violationType;
    }
}
