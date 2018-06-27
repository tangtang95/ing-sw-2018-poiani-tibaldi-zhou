package org.poianitibaldizhou.sagrada.cli;

public class ScreenThread extends Thread {

    private IScreen screen;

    ScreenThread(IScreen screen) {
        this.screen = screen;
    }

    @Override
    public void run() {
        try {
            screen.startCLI();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}