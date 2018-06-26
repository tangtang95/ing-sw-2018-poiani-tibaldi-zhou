package org.poianitibaldizhou.sagrada.game.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TerminationGameManagerTest {

    @Mock
    private GameManager gameManager;

    @Before
    public void setUp() {
        //MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testTerminateCall() {/*
        TerminationGameManager terminationGameManager = new TerminationGameManager(anyString(), gameManager);

        terminationGameManager.terminateGame();

        verify(gameManager, times(1)).terminateGame(anyString());*/
    }

}
