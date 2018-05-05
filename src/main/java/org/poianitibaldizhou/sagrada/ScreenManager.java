package org.poianitibaldizhou.sagrada;

import org.poianitibaldizhou.sagrada.lobby.view.IScreen;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EmptyStackException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ScreenManager {

    private final Deque<IScreen> screens;
    private Thread currentThread;

    /**
     * Constructor.
     * Create a manager class for the CLIs (Screen)
     *
     */
    public ScreenManager(){
        screens = new ArrayDeque<>();
        currentThread = null;
    }

    public Thread getCurrentThread() {
        return currentThread;
    }

    /**
     * Push and start the screen into the stack screens, but before that stop the current thread
     *
     * @param screen the new screen to be pushed
     */
    public synchronized void pushScreen(IScreen screen){
        if(currentThread != null && currentThread.isAlive())
            currentThread.interrupt();
        screens.push(screen);
        startThread();
    }

    /**
     *
     * @return
     */
    public synchronized IScreen popScreen(){
        if(screens.isEmpty())
            throw new EmptyStackException();
        if(currentThread != null && currentThread.isAlive())
            currentThread.interrupt();
        currentThread = null;
        IScreen screen = screens.pop();
        startThread();
        return screen;
    }

    /**
     *
     * @param screen
     */
    public synchronized void replaceScreen(IScreen screen){
        if(screens.isEmpty())
            throw new EmptyStackException();
        if(currentThread != null && currentThread.isAlive())
            currentThread.interrupt();
        currentThread = null;
        screens.pop();
        pushScreen(screen);
    }

    /**
     *
     * @return
     */
    public synchronized IScreen topScreen(){
        if(screens.isEmpty())
            throw new EmptyStackException();
        return screens.peek();
    }

    private void startThread(){
        currentThread = new Thread(() -> {
            try {
                screens.peek().run();
            }catch (Exception e){
                Logger.getAnonymousLogger().log(Level.INFO, "thread killed");
            }
        });
        currentThread.start();
    }
}
