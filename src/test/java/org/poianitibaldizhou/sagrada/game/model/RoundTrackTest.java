package org.poianitibaldizhou.sagrada.game.model;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NumberConstraint;

import java.util.ArrayList;
import java.util.List;

public class RoundTrackTest {
    @DataPoint
    public static Game game;

    @DataPoint
    public static List<String> tokens;

    @DataPoint
    public static List<Dice> dices;

    @BeforeClass
    public static void setUpClass() {
        tokens = new ArrayList<>();
        dices = new ArrayList<>();
        tokens.add("ABC");
        tokens.add("DEF");
        game = new Game(tokens, "1Game");
        dices.add(new Dice(new NumberConstraint(1),new ColorConstraint(Color.BLUE)));
        dices.add(new Dice(new NumberConstraint(2),new ColorConstraint(Color.GREEN)));
        dices.add(new Dice(new NumberConstraint(3),new ColorConstraint(Color.YELLOW)));
        dices.add(new Dice(new NumberConstraint(4),new ColorConstraint(Color.RED)));
        dices.add(new Dice(new NumberConstraint(5),new ColorConstraint(Color.PURPLE)));
        dices.add(new Dice(new NumberConstraint(6),new ColorConstraint(Color.GREEN)));

    }

    @AfterClass
    public static void tearDownClass(){

    }

    @Before
    public void setUp(){

    }

    @After
    public void tearDown() {
    }

    @Test
    public void test(){
        for (int i = 1; i < 10; i++) {
            game.getRoundTrack().addDicesToCurrentRound(dices);
            game.getRoundTrack().nextRound();
        }
        int j = 0;
        for (int i = 1; i < 10; i++) {
            for(Dice d: game.getRoundTrack().getDices(i)) {
                assert (d.equals(dices.get(j)));
                j++;
            }
            j = 0;
        }
    }
}
