package org.poianitibaldizhou.sagrada.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BufferManager {

    private class ReadThread extends Thread {

        private BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        boolean exit = true;

        @Override
        public void run(){
            while(exit){
                pausePoint();
                try {
                    String read = console.readLine();
                } catch (IOException e) {
                    //...
                }

            }
        }

        synchronized void pausePoint() {
            while (needToPause) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private boolean needToPause;

    public void consolePrint(String message, Level level) {
        if (level.name().equals(Level.STANDARD.name()))
            if (!message.equals("") && message.substring(0, 1).equals("\n"))
                System.out.print("\b\b\b" + message + ">> ");
            else
                System.out.print("\r" + message + ">> ");
        else
        if (!message.equals("") && message.substring(0, 1).equals("\n"))
            System.out.print("\b\b\b" + level.name() + ": " + message + ">> ");
        else
            System.out.print("\r" + level.name() + ": " + message + ">> ");
    }

    public void consoleRead(String[] response){

    }

    public void stopConsoleRead() { needToPause = true; }

    public synchronized void restartConsoleRead() {
        needToPause = false;
        this.notifyAll();
    }
}
