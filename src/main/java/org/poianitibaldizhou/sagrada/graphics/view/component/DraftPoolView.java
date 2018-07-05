package org.poianitibaldizhou.sagrada.graphics.view.component;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.graphics.utils.GraphicsUtils;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DraftPoolWrapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * OVERVIEW: Represents the view for the draft pool
 */
public class DraftPoolView extends Pane {

    private List<DiceView> diceViewList;
    private final double scale;


    private static final String IMAGE_PATH = "src/test/images/board/draftpool.png";
    private static final int COLUMNS = 3;

    private static final double PADDING_X_PERCENT = 0.12;
    private static final double PADDING_Y_PERCENT = 0.12;

    private static final double PADDING_PERCENT = 0.06;
    private static final double DICE_SCALE = 1.44;

    /**
     * Constructor.
     * Create a draft pool view (pane) that contains the draft pool image and every dice inside the draft
     * pool
     *
     * @param scale the scale value
     */
    public DraftPoolView(double scale) {
        this.scale = scale;
        diceViewList = new ArrayList<>();
        ImageView draftPoolImage = GraphicsUtils.getSimpleImageView(IMAGE_PATH, scale);

        this.getChildren().add(draftPoolImage);
    }

    /**
     * Draw the draftPool using the draftPoolWrapper object
     *
     * @param draftPoolWrapper the draftPool model that contains the dice inside it
     */
    public void drawDraftPool(DraftPoolWrapper draftPoolWrapper) {
        clearDraftPool();
        for (int i = 0; i < draftPoolWrapper.size(); i++) {
            int row = i / COLUMNS;
            int column = i % COLUMNS;
            drawDice(draftPoolWrapper.getDice(i), row, column);
        }
    }

    /**
     * Draw a new dice into the draftPool
     *
     * @param diceWrapper the new dice to add
     */
    public void addDiceToDraftPool(DiceWrapper diceWrapper) {
        int row = (diceViewList.size()) / COLUMNS;
        int column = (diceViewList.size()) % COLUMNS;
        drawDice(diceWrapper, row, column);
    }

    /**
     * Remove a diceView from the draftPool
     *
     * @param diceWrapper the dice to remove
     */
    public void removeDiceFromDraftPool(DiceWrapper diceWrapper) {
        for (DiceView diceView : diceViewList) {
            if (diceView.getDiceWrapper().equals(diceWrapper)) {
                this.getChildren().remove(diceView);
                diceViewList.remove(diceView);
                return;
            }
        }
    }

    /**
     * Remove every diceView from the draftPool
     */
    public void clearDraftPool() {
        this.getChildren().removeAll(diceViewList);
        diceViewList.clear();
    }


    /**
     * Re-draw every dice on the draftPool by changing the dice number of each dice
     *
     * @param diceList the new list of dice of the draftPool
     */
    public void reRollDraftPool(List<DiceWrapper> diceList) {
        Set<Integer> diceChangedIndexes = new HashSet<>();
        for (DiceWrapper newDice : diceList) {
            for (int j = 0; j < diceViewList.size(); j++) {
                if (diceChangedIndexes.contains(j))
                    break;
                DiceWrapper diceWrapper = diceViewList.get(j).getDiceWrapper();
                if (diceWrapper.getColor() == newDice.getColor()) {
                    diceChangedIndexes.add(j);
                    diceViewList.get(j).changeDiceNumber(newDice.getNumber());
                }
            }
        }
    }

    /**
     * Draw a dice
     *
     * @param diceWrapper dice that will be drawn
     * @param row         dice will be drawn on this row
     * @param column      dice will be drawn on this column
     */
    private void drawDice(DiceWrapper diceWrapper, int row, int column) {
        DiceView diceView = new DiceView(diceWrapper, scale * DICE_SCALE);
        diceViewList.add(diceView);

        DoubleBinding offset = this.widthProperty().multiply(PADDING_PERCENT);
        DoubleBinding paddingX = this.widthProperty().multiply(PADDING_X_PERCENT);
        DoubleBinding paddingY = this.widthProperty().multiply(PADDING_Y_PERCENT);

        DoubleBinding x = paddingX.add(diceView.widthProperty().multiply(column)).add(offset.multiply(column));
        DoubleBinding y = paddingY.add(diceView.heightProperty().multiply(row)).add(offset.multiply(row));

        diceView.translateXProperty().bind(x);
        diceView.translateYProperty().bind(y);
        this.getChildren().add(diceView);
    }
}
