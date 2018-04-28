package org.poianitibaldizhou.sagrada.lobby.socket.messages;

import org.poianitibaldizhou.sagrada.lobby.socket.InvokerUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Request implements Serializable {
    private final String methodName;
    private final List<Object> methodParameters;

    /**
     * Constructor.
     * Encapsulate the values necessary to invoke the method of the other side
     *
     * @param methodName       name of the method to call
     * @param methodParameters a list of Object needed by the method parameters
     */
    public Request(String methodName, List<Object> methodParameters) {
        this.methodName = methodName;
        this.methodParameters = new ArrayList<>(methodParameters);
    }

    public Request(Request request) {
        this.methodName = request.methodName;
        this.methodParameters = new ArrayList<>(request.methodParameters);
    }

    public List<Object> getMethodParameters() {
        return new ArrayList<>(methodParameters);
    }

    /**
     * Invoke the method of the target object using methodName and methodParameters
     *
     * @param target the invoker of the method
     * @return the object returned by the invocation (null if the method is void)
     */
    public Object invokeMethod(Object target) {
        Method[] methods = target.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals(methodName)) {
                return InvokerUtils.invoke(methods[i], target, methodParameters.toArray());
            }
        }
        throw new RuntimeException("invocation failed, cannot find method from all the target's method");
    }

    /**
     * Replace a parameter of the methodParameters (used when replacing the client observer to the proxy view)
     *
     * @param parameter the new Object parameter
     * @param index     the index of the old Object to be replaced
     */
    public void replaceParameter(Object parameter, int index) {
        methodParameters.set(index, parameter);
    }
}
