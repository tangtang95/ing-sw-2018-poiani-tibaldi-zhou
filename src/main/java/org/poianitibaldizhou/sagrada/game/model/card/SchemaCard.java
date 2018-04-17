package org.poianitibaldizhou.sagrada.game.model.card;

public class SchemaCard {
    private String name;
    private int difficulty;
    private Tile[][] tileMatrix;

    public static final int NUMBER_OF_COLUMNN = 5;
    public static final int NUMBER_OF_ROWS = 4;

    public SchemaCard(String name, int difficulty, Tile[][] tileMatrix){
        this.name = name;
        this.difficulty = difficulty;
        for (int i = 0; i < NUMBER_OF_ROWS ; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNN; j++) {
                //tileMatrix
            }
        }
    }
}
