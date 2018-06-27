package org.poianitibaldizhou.sagrada.cli;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.ColorWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BuildGraphicTest {

    @DataPoint
    public static BuildGraphic buildGraphic;

    @DataPoint
    public static List<DiceWrapper> diceWrappers;

    @Before
    public void setUp() throws Exception {
        buildGraphic = new BuildGraphic();
        diceWrappers = new ArrayList<>();
        diceWrappers.add(new DiceWrapper(ColorWrapper.YELLOW,4));
        diceWrappers.add(new DiceWrapper(ColorWrapper.YELLOW,4));
        diceWrappers.add(new DiceWrapper(ColorWrapper.YELLOW,4));
        diceWrappers.add(new DiceWrapper(ColorWrapper.YELLOW,4));
        diceWrappers.add(new DiceWrapper(ColorWrapper.YELLOW,4));
        diceWrappers.add(new DiceWrapper(ColorWrapper.YELLOW,4));
        diceWrappers.add(new DiceWrapper(ColorWrapper.YELLOW,4));
        diceWrappers.add(new DiceWrapper(ColorWrapper.YELLOW,4));
        diceWrappers.add(new DiceWrapper(ColorWrapper.YELLOW,4));
        diceWrappers.add(new DiceWrapper(ColorWrapper.YELLOW,4));
    }

    @Test
    public void buildGraphicDices() {
        String test = "  [1]      [2]      [3]      [4]      [5]    \n" +
                " -----    -----    -----    -----    -----   \n" +
                "| 4/Y |  | 4/Y |  | 4/Y |  | 4/Y |  | 4/Y |  \n" +
                " -----    -----    -----    -----    -----   \n" +
                "  [6]      [7]      [8]      [9]      [10]    \n" +
                " -----    -----    -----    -----    -----   \n" +
                "| 4/Y |  | 4/Y |  | 4/Y |  | 4/Y |  | 4/Y |  \n" +
                " -----    -----    -----    -----    -----   \n";
        assertEquals(test,buildGraphic.buildGraphicDices(diceWrappers).toString());
    }


}
