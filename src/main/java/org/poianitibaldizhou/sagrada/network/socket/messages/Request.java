package org.poianitibaldizhou.sagrada.network.socket.messages;

import org.poianitibaldizhou.sagrada.utilities.ServerMessage;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Request implements Serializable {
    private final String methodName;
    private final Object[] methodParameters;

    /**
     * Constructor.
     * Encapsulate the values necessary to invoke the method of the other side
     *
     * @param methodName       name of the method to call
     * @param methodParameters an array of Object needed by the method
     */
    public Request(String methodName, Object... methodParameters) {
        this.methodName = methodName;
        this.methodParameters = methodParameters;
    }

    /**
     * Constructor.
     * Encapsulate the values necessary to invoke the method of the other side
     *
     * @param request the request to copy from
     */
    public Request(Request request) {
        this.methodName = request.methodName;
        this.methodParameters = request.getMethodParameters().toArray();
    }

    public List<Object> getMethodParameters() {
        List<Object> parameters = new ArrayList<>();
        if(methodParameters != null)
            parameters.addAll(Arrays.asList(methodParameters));
        return parameters;
    }

    /**
     * Invoke the method of the target object using methodName and methodParameters
     *
     * @param target the invoker of the method
     * @return the object returned by the invocation (null if the method is void)
     */
    public Object invokeMethod(Object target) throws IOException {
        Method[] methods = target.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                try {
                    return method.invoke(target, methodParameters);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    Logger.getAnonymousLogger().log(Level.SEVERE, e.getMessage());
                }
            }
        }
        throw new IOException(ServerMessage.INVOCATION_ERROR);
    }

    /**
     * Replace a parameter of the methodParameters (used when replacing the client observer to the proxy view)
     *
     * @param parameter the new Object parameter
     * @param index     the index of the old Object to be replaced
     */
    public void replaceParameter(Object parameter, int index) {
        methodParameters[index] = parameter;
    }
}
