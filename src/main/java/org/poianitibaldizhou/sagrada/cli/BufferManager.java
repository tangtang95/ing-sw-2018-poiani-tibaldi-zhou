package org.poianitibaldizhou.sagrada.cli;

import java.util.ArrayDeque;
import java.util.Deque;

public class BufferManager {

    private final Deque<String> lowMessage;
    private final Deque<String> highMessage;

    private Thread printManager;
    private Thread readManager;

    public BufferManager() {

        this.highMessage = new ArrayDeque<>();
        this.lowMessage = new ArrayDeque<>();

        this.printManager = null;
        this.readManager = null;
    }

    public void consolePrint(String message, Level level) {

        if (level == Level.HIGH) {
            highMessage.push(message);
        }
        else {
            lowMessage.push(message);
        }

        if (printManager == null || !printManager.isAlive()) {
            printManager = new PrintThread(lowMessage, highMessage);
            printManager.start();
        }
    }

    public void consoleRead(String[] response){
        if (readManager != null && (readManager.isAlive() || readManager.isInterrupted())) {
            stopConsoleRead();
        }
        response[0] = null;
        readManager = new ReadThread(response);
        readManager.start();

        while (response[0] == null && readManager != null) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (response[0] == null || response[0].equals("")) {
            response[0] = null;
            throw new NullPointerException();
        }
    }

    public void stopConsoleRead() {
        if (readManager != null && readManager.isAlive())
            readManager.interrupt();
        readManager = null;
    }
}
