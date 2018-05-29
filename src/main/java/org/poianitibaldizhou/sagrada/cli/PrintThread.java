package org.poianitibaldizhou.sagrada.cli;

import java.util.Deque;

@Deprecated
public class PrintThread extends Thread {
    private final Deque<String> lowMessage;
    private final Deque<String> highMessage;

    private static final long BUFFER_TIME = 180;

    PrintThread(Deque<String> lowMessage, Deque<String> highMessage) {
        this.lowMessage = lowMessage;
        this.highMessage = highMessage;
    }

    @Override
    public void run() {
        String message;

        while (!lowMessage.isEmpty() || !highMessage.isEmpty()) {
            if ((message = popMessage(true)) != null) {
                if (!message.equals("") && message.substring(0, 1).equals("\n"))
                    System.out.print("\b\b\b" + message + ">> ");
                else
                    System.out.print("\r" + message + ">> ");
            }
        }
    }

    private String popMessage(boolean checkLoad) {

        if (highMessage.isEmpty()) {
            if (lowMessage.isEmpty())
                return null;
            else {
                if (lowMessage.size() < 2 && checkLoad) {
                    loadBuffer();
                    return popMessage(false);
                }
                return lowMessage.removeLast();
            }
        } else
            return highMessage.removeLast();
    }

    private void loadBuffer() {
        try {
            Thread.sleep(BUFFER_TIME);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
