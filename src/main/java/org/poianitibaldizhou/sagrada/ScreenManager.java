package org.poianitibaldizhou.sagrada;

import org.poianitibaldizhou.sagrada.lobby.view.IScreen;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EmptyStackException;

/**
 * Singleton pattern without using global variables, just limiting the number of instances to 1
 */
public class ScreenManager {

    private Deque<IScreen> screens;

    public ScreenManager(){
        screens = new ArrayDeque<>();
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
