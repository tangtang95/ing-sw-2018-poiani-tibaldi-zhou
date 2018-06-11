package org.poianitibaldizhou.sagrada.game.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TerminationGameManagerTest {

    @Mock
    private GameManager gameManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testTerminateCall() {
        TerminationGameManager terminationGameManager = new TerminationGameManager("gameNameTest", gameManager);

        terminationGameManager.terminateGame();

        verify(gameManager, times(1)).terminateGame("gameNameTest");
    }

    @Test
    public void testNoTerminateCall() {
        TerminationGameManager terminationGameManager = new TerminationGameManager("gameNameTest", gameManager);

        verify(gameManager, times(0)).terminateGame("gameNameTest");
    }

    @Test
    public void testWithTerminationWithAnotherName() {
        TerminationGameManager terminationGameManager = new TerminationGameManager("gameNameTest", gameManager);

        verify(gameManager, times(0)).terminateGame("gameNameTestDifferetn");
    }
}
