package org.poianitibaldizhou.sagrada;

import org.junit.*;
import org.poianitibaldizhou.sagrada.lobby.view.IScreen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.*;

public class ScreenManagerTest {

    private BufferedReader scanner1;
    private BufferedReader scanner2;

    private IScreen screen1;
    private IScreen screen2;

    private ScreenManager screenManager;

    @Before
    public void setUp() {
        scanner1 = new BufferedReader(new InputStreamReader(System.in));
        scanner2 = new BufferedReader(new InputStreamReader(System.in));
        screen1 = () -> {
            try {
                while (!scanner1.ready()) {
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {
                return;
            } catch (IOException e) {
                fail("exception not expected");
            }
            try {
                scanner1.readLine();
            } catch (IOException e) {
                fail("exception not expected");
            }
        };
        screen2 = () -> {
            try {
                while (!scanner2.ready()) {
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {
                return;
            } catch (IOException e) {
                fail("exception not expected");
            }
            try {
                scanner2.readLine();
            } catch (IOException e) {
                fail("exception not expected");
            }
        };
        screenManager = new ScreenManager();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testPushScreen() throws InterruptedException {
        screenManager.pushScreen(screen1);
        assertEquals("Didn't push the screen1", screen1, screenManager.topScreen());
        Thread thread1 = screenManager.getCurrentThread();
        assertTrue(thread1.isAlive());
        screenManager.pushScreen(screen2);
        assertEquals("Didn't push the screen2", screen2, screenManager.topScreen());
        assertNotEquals("thread are the same", thread1, screenManager.getCurrentThread());
        thread1.join();
        assertTrue(!thread1.isAlive());
        assertTrue(screenManager.getCurrentThread().isAlive());
    }

}
