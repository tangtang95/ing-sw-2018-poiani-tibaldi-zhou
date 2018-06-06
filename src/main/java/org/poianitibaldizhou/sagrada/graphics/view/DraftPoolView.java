package org.poianitibaldizhou.sagrada.graphics.view;

import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IDraftPoolObserver;

import java.io.IOException;

public class DraftPoolView extends Pane implements IDraftPoolObserver{


    public DraftPoolView(){

    }

    @Override
    public void onDiceAdd(String dice) throws IOException {

    }

    @Override
    public void onDiceRemove(String dice) throws IOException {

    }

    @Override
    public void onDicesAdd(String dices) throws IOException {

    }

    @Override
    public void onDraftPoolReroll(String dices) throws IOException {

    }

    @Override
    public void onDraftPoolClear() throws IOException {

    }
}
