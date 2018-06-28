package org.poianitibaldizhou.sagrada.cli;

/**
 * OVERVIEW: Thread related to a screen.
 */
public class ScreenThread extends Thread {

    private IScreen screen;

    /**
     * Creates a screen thread for a certain screen
     *
     * @param screen a thread for this screen will be created
     */
    ScreenThread(IScreen screen) {
        this.screen = screen;
    }

    /**
     * Start the screen
     */
    @Override
    public void run() {
        try {
            screen.startCLI();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}