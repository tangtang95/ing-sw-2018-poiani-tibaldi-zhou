package org.poianitibaldizhou.sagrada.game.model.cards;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.jetbrains.annotations.Contract;

@Immutable
public final class SchemaCardPoint {
    public final int row;
    public final int column;

    @Contract(pure = true)
    public static boolean isOutOfBounds(int row, int column){
        return row < 0 || row > SchemaCard.NUMBER_OF_ROWS -1 || column < 0 || column > SchemaCard.NUMBER_OF_COLUMNS -1;
    }

    public SchemaCardPoint(int row, int column) {
        if(isOutOfBounds(row,column))
            throw new IllegalArgumentException("schema card point out of bounds");
        this.row = row;
        this.column = column;
    }

    @Contract(pure = true)
    public int getRow(){
        return row;
    }

    @Contract(pure = true)
    public int getColumn(){
        return column;
    }
}
