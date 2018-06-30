package org.poianitibaldizhou.sagrada.exception;

import java.io.IOException;

/**
 * Network exception
 */
public class NetworkException extends Exception{

    private final Exception innerException;

    /**
     * Creates a network exception with a specified inner exception
     *
     * @param e inner exception
     */
    public NetworkException(IOException e) {
        innerException = e;
    }

    /**
     * Creates a network exception with an IOException as inner exception.
     * The IOException will be created with errorMessage as message
     *
     * @param errorMessage
     */
    public NetworkException(String errorMessage) {
        innerException = new IOException(errorMessage);
    }

    /**
     * @return inner exception
     */
    public Exception getInnerException() {
        return innerException;
    }
}
