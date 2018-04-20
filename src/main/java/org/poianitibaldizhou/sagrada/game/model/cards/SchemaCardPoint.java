package org.poianitibaldizhou.sagrada.game.model.cards;

import org.poianitibaldizhou.sagrada.exception.SchemaCardPointOutOfBoundsException;

public final class SchemaCardPoint {
    public final int row;
    public final int column;

    public SchemaCardPoint(int row, int column) throws SchemaCardPointOutOfBoundsException {
        if(row < 0 || row > SchemaCard.NUMBER_OF_ROWS -1 || column < 0 || column > SchemaCard.NUMBER_OF_COLUMNS -1)
            throw new SchemaCardPointOutOfBoundsException("schema card point out of bounds");
        this.row = row;
        this.column = column;
    }

    public int getRow(){
        return row;
    }

    public int getColumn(){
        return column;
    }
}
