package org.poianitibaldizhou.sagrada.cli;

import org.jetbrains.annotations.Contract;

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
     */
    public ScreenManager() {
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
    public synchronized void pushScreen(IScreen screen) {
        stopCurrentThread();
        screens.push(screen);
        startThread();
    }

    /**
     * Stop the currentThread and pop a screen from the stack, then start the thread at the top of the stack
     * and return the screen popped out
     *
     * @return the screen at the top of the stack
     * @throws EmptyStackException if the stack of screen is empty
     */
    public synchronized IScreen popScreen(){
        if (screens.isEmpty())
            throw new EmptyStackException();
        stopCurrentThread();
        IScreen screen = screens.pop();
        startThread();
        return screen;
    }

    /**
     * Stop the currentThread and pop a screen from the stack without starting it.
     *
     * @throws EmptyStackException if the stack of screen is empty
     */
    public synchronized void popWithoutStartInScreen() {
        if(screens.isEmpty())
            throw new EmptyStackException();
        stopCurrentThread();
    }

    /**
     * Replace the current screen with another one; first of all pop the screen from the stack
     * and then push the new screen.
     *
     * @param screen the new screen to replace the top screen
     * @throws EmptyStackException if the stack of screen is empty
     */
    public synchronized void replaceScreen(IScreen screen){
        if (screens.isEmpty())
            throw new EmptyStackException();
        stopCurrentThread();
        screens.pop();
        pushScreen(screen);
    }

    /**
     * return the top screen of the stack without removing anything
     *
     * @return the screen at the top of the stack (null if the stack is empty)
     */
    @Contract(pure = true)
    public synchronized IScreen topScreen() {
        if (screens.isEmpty())
            return null;
        return screens.peek();
    }

    /**
     * @return the size of the stack of screen
     */
    public int getNumberOfScreen() {
        return screens.size();
    }

    private void startThread() {
        try {
            if (topScreen() != null) {
                currentThread = new ScreenThread(topScreen());
                currentThread.start();
            }
        } catch (EmptyStackException e) {
            Logger.getAnonymousLogger().log(Level.INFO, "No screen available");
        }
    }

    private void stopCurrentThread() {
        if (currentThread != null && currentThread.isAlive())
            currentThread.interrupt();
        currentThread = null;
    }

}
