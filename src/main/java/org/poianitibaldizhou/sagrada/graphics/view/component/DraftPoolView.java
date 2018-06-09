package org.poianitibaldizhou.sagrada.graphics.view.component;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.graphics.utils.TextureUtils;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DraftPoolWrapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DraftPoolView extends Pane{

    private final ImageView draftPoolImage;
    private List<DiceView> diceViewList;
    private final double scale;


    private static final String IMAGE_PATH = "images/board/draftpool.png";
    private static final int COLUMNS = 3;

    private static final double PADDING_X_PERCENT = 0.12;
    private static final double PADDING_Y_PERCENT = 0.12;

    private static final double PADDING_PERCENT = 0.1;
    private static final double DICE_SCALE = 0.6;

    public DraftPoolView(double scale){
        this.scale = scale;
        diceViewList = new ArrayList<>();
        draftPoolImage = TextureUtils.getSimpleImageView(IMAGE_PATH, scale);

        this.getChildren().add(draftPoolImage);
    }

    public List<DiceWrapper> getDiceWrapperList(){
        return diceViewList.stream().map(DiceView::getDiceWrapper).collect(Collectors.toList());
    }

    public void drawDraftPool(DraftPoolWrapper draftPoolWrapper) {
        clearDraftPool();
        for (int i = 0; i < draftPoolWrapper.size(); i++) {
            int row = i/COLUMNS;
            int column = i%COLUMNS;
            drawDice(draftPoolWrapper.getDice(i), row, column);
        }
    }

    public void addDiceToDraftPool(DiceWrapper diceWrapper){
        int row = (diceViewList.size())/COLUMNS;
        int column = (diceViewList.size())%COLUMNS;
        drawDice(diceWrapper, row, column);
    }

    public void removeDiceFromDraftPool(DiceWrapper diceWrapper){
        for (DiceView diceView: diceViewList) {
            if(diceView.getDiceWrapper().equals(diceWrapper)){
                this.getChildren().remove(diceView);
                diceViewList.remove(diceView);
                return;
            }
        }
    }

    public void clearDraftPool() {
        this.getChildren().removeAll(diceViewList);
        diceViewList.clear();
    }


    public void reRollDraftPool(List<DiceWrapper> diceList) {
        Set<Integer> diceChangedIndexes = new HashSet<>();
        for (int i = 0; i < diceList.size(); i++) {
            for (int j = 0; j < diceViewList.size(); j++) {
                if(diceChangedIndexes.contains(j))
                    break;
                DiceWrapper diceWrapper = diceViewList.get(j).getDiceWrapper();
                if(diceWrapper.getColor() == diceList.get(i).getColor()){
                    diceChangedIndexes.add(j);
                    diceViewList.get(j).reRoll(diceList.get(i).getNumber());
                }
            }
        }
    }

    private void drawDice(DiceWrapper diceWrapper, int row, int column){
        DiceView diceView = new DiceView(diceWrapper, scale*DICE_SCALE);
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
