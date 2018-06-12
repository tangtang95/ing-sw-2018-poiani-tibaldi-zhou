package org.poianitibaldizhou.sagrada.exception;

import java.io.IOException;

public class NetworkException extends Exception{

    private Exception innerException;

    public NetworkException(IOException e) {
        innerException = e;
    }

    public Exception getInnerException() {
        return innerException;
    }
}
