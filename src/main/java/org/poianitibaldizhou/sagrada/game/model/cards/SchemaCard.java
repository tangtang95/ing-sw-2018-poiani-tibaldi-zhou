package org.poianitibaldizhou.sagrada.game.model.cards;

import org.poianitibaldizhou.sagrada.exception.ConstraintTypeException;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.exception.SchemaCardPointOutOfBoundsException;
import org.poianitibaldizhou.sagrada.exception.TileFilledException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.IConstraint;

public class SchemaCard {
    private final String name;
    private final int difficulty;
    private Tile[][] tileMatrix;

    public static final int NUMBER_OF_COLUMNS = 5;
    public static final int NUMBER_OF_ROWS = 4;

    public SchemaCard(String name, int difficulty, IConstraint[][] constraints) {
        this.name = name;
        this.difficulty = difficulty;
        this.tileMatrix = new Tile[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                this.tileMatrix[i][j] = new Tile(constraints[i][j]);
            }
        }
    }

    public String getName() {
        return name;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDice(Dice dice, SchemaCardPoint point) throws ConstraintTypeException, RuleViolationException, TileFilledException {
        setDice(dice, point, ConstraintType.NUMBERCOLOR);
    }

    public void setDice(Dice dice, SchemaCardPoint point, ConstraintType type) throws TileFilledException, RuleViolationException, ConstraintTypeException {
        if (isDicePositionable(dice, point, type))
            tileMatrix[point.row][point.column].setDice(dice, type);
    }

    public Dice getDice(SchemaCardPoint point) {
        return tileMatrix[point.row][point.column].getDice();
    }

    public Dice removeDice(SchemaCardPoint point) {
        return tileMatrix[point.row][point.column].removeDice();
    }

    public boolean isDicePositionable(Dice dice, SchemaCardPoint point) throws RuleViolationException, ConstraintTypeException {
        return isDicePositionable(dice, point, ConstraintType.NUMBERCOLOR);
    }

    public boolean isDicePositionable(Dice dice, SchemaCardPoint point, ConstraintType type) throws RuleViolationException, ConstraintTypeException {
        if (isEmpty()) {
            if (isPointOutsider(point)) {
                if(tileMatrix[point.row][point.column].isDicePositionable(dice, type)){
                    return true;
                }
                else throw new RuleViolationException("Dice not positionable for constraint");
            }
            else
                throw new RuleViolationException("The schema card is empty so you should start from the outside");
        } else {
            if(tileMatrix[point.row][point.column].isDicePositionable(dice, type)) {
                return isAdjacentDicesRulesValid(dice, point);
            }
            else throw new RuleViolationException("Dice not positionable for constraint");
        }
    }

    private boolean isPointOutsider(SchemaCardPoint point){
        return point.row == 0 || point.column == 0 || point.column == NUMBER_OF_COLUMNS - 1 || point.row == NUMBER_OF_ROWS - 1;
    }

    private boolean isAdjacentDicesRulesValid(Dice dice, SchemaCardPoint point) throws RuleViolationException {
        int numberOfAdjacentDice = 0;
        for (int deltaRow = -1; deltaRow <= 1; deltaRow++) {
            for (int deltaColumn = -1; deltaColumn <= 1; deltaColumn++) {
                if (deltaRow == 0 && deltaColumn == 0) continue;
                try {
                    SchemaCardPoint newPoint = new SchemaCardPoint(point.row + deltaRow, point.column + deltaColumn);
                    Dice tileDice = tileMatrix[newPoint.row][newPoint.column].getDice();
                    if (tileDice != null) {
                        numberOfAdjacentDice++;
                        if (Math.abs(deltaRow) + Math.abs(deltaColumn) != 2) {
                            //Ortogonal direction
                            if (tileDice.getColorConstraint().matches(dice.getColorConstraint()))
                                throw new RuleViolationException("There is already an adjacent dice with the same color");
                            if (tileDice.getNumberConstraint().matches(dice.getNumberConstraint()))
                                throw new RuleViolationException("There is already an adjacent dice with the same number");
                        }
                    }
                } catch (SchemaCardPointOutOfBoundsException ignored) { }
            }
        }
        if (numberOfAdjacentDice > 0)
            return true;
        else
            throw new RuleViolationException("There is no adjacent dices");
    }

    public boolean isEmpty() {
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                try {
                    if (getDice(new SchemaCardPoint(i,j)) != null)
                        return false;
                } catch (SchemaCardPointOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public Tile getTile(SchemaCardPoint point){
        return new Tile(tileMatrix[point.row][point.column]);
    }

}
