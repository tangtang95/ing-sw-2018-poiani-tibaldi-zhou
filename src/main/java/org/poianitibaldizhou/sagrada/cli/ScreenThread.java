package org.poianitibaldizhou.sagrada.cli;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ScreenThread extends Thread {

    private IScreen screen;

    ScreenThread(IScreen screen) {
        this.screen = screen;
    }

    @Override
    public void run() {
        try {
            screen.run();
        } catch (InterruptedException e) {
            Logger.getAnonymousLogger().log(Level.INFO, "ScreenThread interrupted");
            Thread.currentThread().interrupt();
        }
    }
}