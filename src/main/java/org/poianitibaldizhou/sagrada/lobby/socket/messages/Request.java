package org.poianitibaldizhou.sagrada.lobby.socket.messages;

import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyServerController;
import org.poianitibaldizhou.sagrada.lobby.socket.InvokerUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Request implements Serializable{
    private final String methodName;
    private final List<Object> methodParameters;

    public Request(String methodName, List<Object> methodParameters){
        this.methodName = methodName;
        this.methodParameters = new ArrayList<>(methodParameters);
    }

    public Object invokeMethod(Object target) {
        Method[] methods = target.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equalsIgnoreCase(methodName)) {
                return InvokerUtils.invoke(methods[i], target, methodParameters.toArray());
            }
        }
        throw new IllegalArgumentException("invoke failed");
    }

    public List<Object> getMethodParameters() {
        return new ArrayList<>(methodParameters);
    }

    public void replaceParameter(Object parameter, int index){
        methodParameters.set(index, parameter);
    }
}
