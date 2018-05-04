package org.poianitibaldizhou.sagrada;

import org.poianitibaldizhou.sagrada.lobby.view.IScreen;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Singleton pattern without using global variables, just limiting the number of instances to 1
 */
public class ScreenManager {

    private static boolean isInstantiated = false;
    private Stack<IScreen> screens;

    public ScreenManager(){
        if(isInstantiated)
            throw new IllegalArgumentException("cannot instantiate two screen manager");
        isInstantiated = true;
        screens = new Stack<>();
    }

    public void pushScreen(IScreen screen){
        screens.push(screen);
        screen.run();
    }

    public IScreen popScreen(){
        if(screens.isEmpty())
            throw new EmptyStackException();
        IScreen screen = screens.pop();
        screens.peek().run();
        return screen;
    }

    public IScreen topScreen(){
        if(screens.isEmpty())
            throw new EmptyStackException();
        return screens.peek();
    }
}
