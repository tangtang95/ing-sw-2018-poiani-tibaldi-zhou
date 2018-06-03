package org.poianitibaldizhou.sagrada.graphics.objects;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class AnimationView extends Transition {
    private ImageView imageView;
    private int count;
    private int columns;
    private int offsetX;
    private int offsetY;
    private int width;
    private int height;

    private int lastIndex;

    public AnimationView(ImageView imageView, Duration duration, int width, int height, int offsetX, int offsetY, int columns, int count){
        this.imageView = imageView;
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.columns = columns;
        this.count = count;
        this.lastIndex = 0;
        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
    }


    @Override
    protected void interpolate(double frac) {
        final int index = Math.min((int) Math.floor(frac * count), count - 1);
        if (index != lastIndex) {
            final int x = (index % columns) * width  + offsetX;
            final int y = (index / columns) * height + offsetY;
            imageView.setViewport(new Rectangle2D(x, y, width, height));
            lastIndex = index;
        }
    }
}
