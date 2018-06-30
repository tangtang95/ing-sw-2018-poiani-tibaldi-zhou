package org.poianitibaldizhou.sagrada.exception;

/**
 * If a dice needed is not found
 */
public class DiceNotFoundException extends Exception{

    /**
     * Not found dice
     *
     * @param message error message
     */
    public DiceNotFoundException(String message){
        super(message);
    }
}
