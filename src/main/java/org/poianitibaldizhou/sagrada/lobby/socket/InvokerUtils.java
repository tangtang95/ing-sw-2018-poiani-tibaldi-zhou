package org.poianitibaldizhou.sagrada.lobby.socket;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InvokerUtils {

    private static final Logger LOGGER = Logger.getLogger(InvokerUtils.class.getName());

    public static Object invoke(Method method, Object invoker, Object[] parameters) {
        LOGGER.log(Level.INFO, "try invoking...");
        try {
            if (parameters.length == 0)
                return method.invoke(invoker);
            else if (parameters.length == 1)
                return method.invoke(invoker, parameters[0]);
            else if (parameters.length == 2)
                return method.invoke(invoker, parameters[0], parameters[1]);
            else if (parameters.length == 3)
                return method.invoke(invoker, parameters[0], parameters[1], parameters[2]);
            else if (parameters.length == 4)
                return method.invoke(invoker, parameters[0], parameters[1], parameters[2], parameters[3]);
            else if (parameters.length == 5)
                return method.invoke(invoker, parameters[0], parameters[1], parameters[2], parameters[3], parameters[4]);
            else
                throw new IllegalArgumentException("Cannot handle more than 5 parameters");
        } catch (IllegalAccessException e) {
            LOGGER.log(Level.SEVERE, "IllegalAccessException: " + e.toString());
        } catch (InvocationTargetException e) {
            LOGGER.log(Level.SEVERE, "InvocationTargetException: "+ e.getCause().getMessage());
        }
        return null;
    }

}
