package org.poianitibaldizhou.sagrada.graphics.objects;

import javafx.scene.canvas.GraphicsContext;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IDraftPoolObserver;

import java.io.IOException;

public class DraftPoolView implements IDraftPoolObserver{

    private GraphicsContext gc;

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
