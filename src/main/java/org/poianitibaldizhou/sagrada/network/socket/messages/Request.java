package org.poianitibaldizhou.sagrada.network.socket.messages;

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
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals(methodName)) {
                try {
                    return methods[i].invoke(target, methodParameters);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    Logger.getAnonymousLogger().log(Level.SEVERE, e.getMessage());
                }
            }
        }
        throw new IOException("invocation failed, cannot find method from all the target's method");
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
