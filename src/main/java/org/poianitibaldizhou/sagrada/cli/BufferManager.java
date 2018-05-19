package org.poianitibaldizhou.sagrada.cli;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;

public class BufferManager {
    private final BufferedReader console;
    private final Deque<String> lowMessage;
    private final Deque<String> highMessage;

    private Thread manager;

    public BufferManager() {
        this.console = new BufferedReader(new InputStreamReader(System.in));
        this.highMessage = new ArrayDeque<>();
        this.lowMessage = new ArrayDeque<>();
        this.manager = null;
    }

    public void formatPrint(String message, Level level) {

        if (level == Level.HIGH) {
            highMessage.push(message);
        }
        else {
            lowMessage.push(message);
        }

        if (manager == null || !manager.isAlive()) {
            manager = new BufferThread(lowMessage, highMessage);
            manager.start();
        }
    }

    public BufferedReader getConsole() {
        return console;
    }

}
