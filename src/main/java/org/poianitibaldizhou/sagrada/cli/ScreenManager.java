package org.poianitibaldizhou.sagrada.cli;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EmptyStackException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * OVERVIEW: Manages the various screen as a stack of screen. So the screen that will be active
 * is the last one that has been pushed.
 */
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

    /**
     * Return the current thread active on the CLI client
     *
     * @return current thread active on the CLI client
     */
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
    public synchronized IScreen popScreen() {
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
        if (screens.isEmpty())
            throw new EmptyStackException();
        stopCurrentThread();
        screens.pop();
    }

    /**
     * Replace the current screen with another one; first of all pop the screen from the stack
     * and then push the new screen.
     *
     * @param screen the new screen to replace the top screen
     * @throws EmptyStackException if the stack of screen is empty
     */
    public synchronized void replaceScreen(IScreen screen) {
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
    private synchronized IScreen topScreen() {
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

    /**
     * If there is at least a screen pushed, is start the thread associated with it.
     */
    private void startThread() {
        try {
            if (topScreen() != null) {
                currentThread = new ScreenThread(topScreen());
                currentThread.start();
            }
        } catch (EmptyStackException e) {
            Logger.getAnonymousLogger().log(Level.INFO, ClientMessage.NO_SCREEN_AVAILABLE);
        }
    }

    /**
     * Stop the current thread that is being executed
     */
    private void stopCurrentThread() {
        if (currentThread != null && currentThread.isAlive())
            currentThread.interrupt();
        currentThread = null;
    }

}
