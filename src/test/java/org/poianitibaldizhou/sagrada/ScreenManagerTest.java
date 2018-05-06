package org.poianitibaldizhou.sagrada;

import org.junit.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.cli.IScreen;
import org.poianitibaldizhou.sagrada.cli.ScreenManager;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ScreenManagerTest {

    private IScreen screen1;
    private IScreen screen2;

    private ScreenManager screenManager;

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {

    }

    @Before
    public void setUp() {
        screen1 = () -> {
                while (true);
        };
        screen2 = () -> {
                while (true);
        };
        screenManager = new ScreenManager();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testPushScreen() {
        screenManager.pushScreen(screen1);
        assertEquals("Didn't push the screen1", screen1, screenManager.topScreen());
        Thread thread1 = screenManager.getCurrentThread();
        assertTrue(thread1.isAlive());
        screenManager.pushScreen(screen2);
        assertEquals("Didn't push the screen2", screen2, screenManager.topScreen());
        assertNotEquals("thread are the same", thread1, screenManager.getCurrentThread());
        assertTrue(!thread1.isAlive());
        assertTrue(screenManager.getCurrentThread().isAlive());
    }

}
