package org.poianitibaldizhou.sagrada.cli;

import org.junit.*;
import org.poianitibaldizhou.sagrada.cli.ScreenManager;
import org.poianitibaldizhou.sagrada.cli.IScreen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.EmptyStackException;

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
                    if(Thread.interrupted())
                        throw new InterruptedException();
                }
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
                    if(Thread.interrupted())
                        throw new InterruptedException();
                }
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
        if(screenManager.getCurrentThread() != null) {
            Thread thread = screenManager.getCurrentThread();
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                fail("thread didn't end");
            }
        }
        scanner1 = null;
        scanner2 = null;
        screenManager = null;
    }

    @Test
    public void testPushScreen() throws Exception{
        screenManager.pushScreen(screen1);
        assertEquals("Didn't push the screen1", screen1, screenManager.topScreen());
        Thread thread1 = screenManager.getCurrentThread();
        assertTrue(thread1.isAlive());
        screenManager.pushScreen(screen2);
        assertEquals("Didn't push the screen2", screen2, screenManager.topScreen());
        assertNotEquals("threads are the same", thread1, screenManager.getCurrentThread());
        thread1.join();
        assertFalse(thread1.isAlive());
        assertTrue(screenManager.getCurrentThread().isAlive());
    }

    @Test
    public void testPopScreen() throws Exception{
        try {
            screenManager.popScreen();
            fail("exception expected");
        }catch(EmptyStackException e){
            assertEquals("numberOfScreen unmatched", 0, screenManager.getNumberOfScreen());
        }
        screenManager.pushScreen(screen1);
        Thread thread1 = screenManager.getCurrentThread();
        assertTrue(thread1.isAlive());
        screenManager.pushScreen(screen2);
        thread1.join();
        assertFalse(thread1.isAlive());
        Thread thread2 = screenManager.getCurrentThread();
        assertTrue(thread2.isAlive());
        IScreen poppedScreen = screenManager.popScreen();
        assertEquals("screens aren't the same", screen2, poppedScreen);
        thread2.join();
        assertFalse(thread2.isAlive());
        poppedScreen = screenManager.popScreen();
        assertEquals("screens aren't the same", screen1, poppedScreen);
        assertEquals("currentThread isn't null", null, screenManager.getCurrentThread());
    }

    @Test
    public void testReplaceScreen() throws Exception{
        screenManager.pushScreen(screen1);
        Thread thread1 = screenManager.getCurrentThread();
        assertTrue(thread1.isAlive());
        screenManager.replaceScreen(screen2);
        Thread thread2 = screenManager.getCurrentThread();
        thread1.join(2000);
        assertFalse(thread1.isAlive());
        assertTrue(thread2.isAlive());
    }


}
