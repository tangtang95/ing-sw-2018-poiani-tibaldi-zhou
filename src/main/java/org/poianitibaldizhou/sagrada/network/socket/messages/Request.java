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

/**
 * OVERVIEW: Represents the general request of executing a certain method
 */
public class Request implements Serializable {
    private final String methodName;
    private final Object[] methodParameters;

    private static final String INVOCATION_ERROR = "Invocation failed, cannot find method from all the target's method.";

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

    /**
     * Returns the list of the parameters necessary for the method invocation
     *
     * @return list of parameters necessary for the method invocation
     */
    public List<Object> getMethodParameters() {
        List<Object> parameters = new ArrayList<>();
        if (methodParameters != null)
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
                } catch (IllegalAccessException e) {
                    Logger.getAnonymousLogger().log(Level.SEVERE, e.getMessage());
                } catch (InvocationTargetException e) {
                    Logger.getAnonymousLogger().log(Level.SEVERE, e.getMessage());
                    if(e.getTargetException() instanceof IOException){
                        throw (IOException) e.getTargetException();
                    }
                }
            }
        }
        throw new IllegalStateException(INVOCATION_ERROR);
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
