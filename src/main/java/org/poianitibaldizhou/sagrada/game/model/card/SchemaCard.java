package org.poianitibaldizhou.sagrada.game.model.card;

import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.exception.TileFilledException;
import org.poianitibaldizhou.sagrada.game.model.Dice;

public class SchemaCard {
    private String name;
    private int difficulty;
    private Tile[][] tileMatrix;

    public static final int NUMBER_OF_COLUMNS = 5;
    public static final int NUMBER_OF_ROWS = 4;

    public SchemaCard(String name, int difficulty, Tile[][] tileMatrix) {
        this.name = name;
        this.difficulty = difficulty;
        this.tileMatrix = new Tile[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
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

    public void setDice(Dice dice, int row, int column) throws TileFilledException, RuleViolationException {
        if (isDicePositionable(dice, row, column))
            tileMatrix[row][column].setDice(dice);
    }

    public Dice getDice(int row, int column) {
        return tileMatrix[row][column].getDice();
    }

    public Dice removeDice(int row, int column) {
        return tileMatrix[row][column].removeDice();
    }

    public boolean isDicePositionable(Dice dice, int row, int column) throws RuleViolationException{
        if (isEmpty()) {
            if (row == NUMBER_OF_ROWS - 1 || column == NUMBER_OF_COLUMNS - 1) {
                return tileMatrix[row][column].isDicePositionable(dice);
            }
            else
                throw new RuleViolationException("The schema card is empty so you should start from the outside");
        } else
            return tileMatrix[row][column].isDicePositionable(dice) && isAdjacentDicesRulesValid(dice, row, column);
    }

    private boolean isAdjacentDicesRulesValid(Dice dice, int row, int column) throws RuleViolationException {
        int numberOfAdjacentDice = 0;
        for (int deltaX = -1; deltaX <= 1; deltaX++) {
            for (int deltaY = -1; deltaY <= 1; deltaY++) {
                if (deltaX == 0 && deltaY == 0) continue;
                Dice tileDice = tileMatrix[row + deltaX][column + deltaY].getDice();
                if (tileDice != null) {
                    numberOfAdjacentDice++;
                    if (Math.abs(deltaX) + Math.abs(deltaY) != 2) {
                        //Ortogonal direction
                        if (tileDice.getColorConstraint().matches(dice.getColorConstraint()))
                            throw new RuleViolationException("There is already an adjacent dice with the same color");
                        if (tileDice.getNumberConstraint().matches(dice.getNumberConstraint()))
                            throw new RuleViolationException("There is already an adjacent dice with the same number");
                    }
                }
            }
        }
        return (numberOfAdjacentDice > 0);
    }

    private boolean isEmpty() {
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                if (tileMatrix[i][j].getDice() != null)
                    return false;
            }
        }
        return true;
    }

    public Tile[][] getTileMatrix(){
        Tile[][] cloneMatrix = new Tile[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                cloneMatrix[i][j] = new Tile(tileMatrix[i][j]);
            }
        }
        return cloneMatrix;
    }

}
