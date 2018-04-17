package org.poianitibaldizhou.sagrada.game.model.card;

import org.poianitibaldizhou.sagrada.exception.TileFilledException;
import org.poianitibaldizhou.sagrada.game.model.Dice;

public class SchemaCard {
    private String name;
    private int difficulty;
    private Tile[][] tileMatrix;

    public static final int NUMBER_OF_COLUMNS = 5;
    public static final int NUMBER_OF_ROWS = 4;

    public SchemaCard(String name, int difficulty, Tile[][] tileMatrix){
        this.name = name;
        this.difficulty = difficulty;
        this.tileMatrix = new Tile[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];
        for (int i = 0; i < NUMBER_OF_ROWS ; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                this.tileMatrix[i][j] = new Tile(tileMatrix[i][j].getConstraint());
            }
        }
    }

    public String getName() {
        return name;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDice(Dice dice, int row, int column) throws TileFilledException {
        tileMatrix[row][column].setDice(dice);
    }

    public Dice getDice(int row, int column){
        return tileMatrix[row][column].getDice();
    }

    public Dice removeDice(int row, int column){
        return tileMatrix[row][column].removeDice();
    }

    public boolean checkDice(Dice dice, int row, int column){
        return true;
    }

}
