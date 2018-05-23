package org.poianitibaldizhou.sagrada.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadThread extends Thread {
    private final BufferedReader console;
    private String[] response;

    ReadThread(String[] response) {
        this.console = new BufferedReader(new InputStreamReader(System.in));
        this.response = response;
    }

    @Override
    public void run() {
        try {
            this.response[0] = console.readLine();
        } catch (IOException e) {
            this.response[0] = "";
        }
    }

}
