package org.poianitibaldizhou.sagrada.exception;

import java.io.IOException;

public class NetworkException extends Exception{

    private final Exception innerException;

    public NetworkException(IOException e) {
        innerException = e;
    }

    public NetworkException(String errorMessage) {
        innerException = new IOException(errorMessage);
    }

    public Exception getInnerException() {
        return innerException;
    }
}
