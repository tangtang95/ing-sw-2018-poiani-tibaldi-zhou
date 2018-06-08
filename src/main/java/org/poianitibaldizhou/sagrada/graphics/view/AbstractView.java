package org.poianitibaldizhou.sagrada.graphics.view;

import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.graphics.controller.MultiPlayerController;

public abstract class AbstractView {

    protected final MultiPlayerController controller;
    protected final Pane corePane;
    protected final Pane notifyPane;

    protected AbstractView(MultiPlayerController controller, Pane corePane, Pane notifyPane) {
        this.controller = controller;
        this.corePane = corePane;
        this.notifyPane = notifyPane;
    }
}
