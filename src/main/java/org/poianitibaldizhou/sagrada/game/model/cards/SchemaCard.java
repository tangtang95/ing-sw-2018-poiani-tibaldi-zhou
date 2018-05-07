package org.poianitibaldizhou.sagrada.game.model.cards;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.exception.RuleViolationType;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.constraint.IConstraint;

import java.util.Objects;

public class SchemaCard {
    private final String name;
    private final int difficulty;
    private Tile[][] tileMatrix;

    public static final int NUMBER_OF_COLUMNS = 5;
    public static final int NUMBER_OF_ROWS = 4;

    /**
     * Constructor: creates a SchemaCard without any dice on it
     *
     * @param name        the name of the schema card
     * @param difficulty  the difficulty of the schema card
     * @param constraints a matrix of the constraints of the tile (NUMBER_OF_ROWS x NUMBER_OF_COLUMNS)
     */
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


    @Contract(pure = true)
    public String getName() {
        return name;
    }


    @Contract(pure = true)
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Set the dice on a tile indicated by row and column based on a standard constraint
     * (TileConstraintType.NUMBER_COLOR, DiceConstraintType.NORMAL)
     *
     * @param dice   the dice to place on the schemaCard
     * @param row    the row where to place the dice
     * @param column the column where to place the dice
     * @throws RuleViolationException if getNeededDice(row, column) != null ||
     *                                hasOrthogonalDicesSimilar() ||
     *                                this.isEmpty() && !this.isOutOfBounds(row,column) ||
     *                                !this.isEmpty() && getNumberOfAdjacentDices() == 0
     */
    public void setDice(Dice dice, int row, int column) throws RuleViolationException {
        setDice(dice, row, column, TileConstraintType.NUMBER_COLOR, DiceConstraintType.NORMAL);
    }

    /**
     * Set the dice on a tile indicated by row and column based on the two type of constraint given
     *
     * @param dice           the dice to place on the schemaCard
     * @param row            the row where to place the dice
     * @param column         the column where to place the dice
     * @param diceConstraint the constraint to check on the placement of dice
     * @param tileConstraint the constraint to check on the tile
     * @throws RuleViolationException if getNeededDice(row, column) != null ||
     *                                hasOrthogonalDicesSimilar() ||
     *                                (this.isEmpty() && !this.isOutOfBounds(row,column)) ||
     *                                (diceConstraint == NORMAL && !this.isEmpty() &&
     *                                getNumberOfAdjacentDices() == 0 ||
     *                                (diceConstraint == ISOLATED &&
     *                                getNumberOfAdjacentDices() > 0
     */
    public void setDice(Dice dice, int row, int column, TileConstraintType tileConstraint,
                        DiceConstraintType diceConstraint) throws RuleViolationException {
        if (isEmpty()) {
            if (!isBorderPosition(row, column)) {
                throw new RuleViolationException(RuleViolationType.NOT_BORDER_TILE);
            }
            if (!tileMatrix[row][column].isDicePositionable(dice, tileConstraint)) {
                throw new RuleViolationException(RuleViolationType.TILE_UNMATCHED);
            }
        } else {
            if (!tileMatrix[row][column].isDicePositionable(dice, tileConstraint)) {
                throw new RuleViolationException(RuleViolationType.TILE_UNMATCHED);
            }
            if (hasOrthogonalDicesSimilar(dice, row, column)) {
                throw new RuleViolationException(RuleViolationType.SIMILAR_DICE_NEAR);
            }
            int numberOfAdjacentDices = getNumberOfAdjacentDices(row, column);
            if (numberOfAdjacentDices == 0 && diceConstraint == DiceConstraintType.NORMAL) {
                throw new RuleViolationException(RuleViolationType.NO_DICE_NEAR);
            }
            if (numberOfAdjacentDices > 0 && diceConstraint == DiceConstraintType.ISOLATED) {
                throw new RuleViolationException(RuleViolationType.HAS_DICE_NEAR);
            }
        }
        tileMatrix[row][column].setDice(dice, tileConstraint);
    }

    /**
     * Returns the dice on the tile designated by row and column
     *
     * @param row    the row from where to get the dice
     * @param column the column from where to get the dice
     * @return the dice positioned on the point (if there is no dice on the tile, then it returns null)
     */
    @Contract(pure = true)
    public Dice getDice(int row, int column) {
        return tileMatrix[row][column].getDice();
    }

    /**
     * Remove the dice from the tile designated by row and column
     *
     * @param row    the row from where to remove the dice
     * @param column the column from where to remove the dice
     * @return the dice removed from the point position (if there is no dice it returns null)
     */
    public Dice removeDice(int row, int column) {
        return tileMatrix[row][column].removeDice();
    }

    /**
     * Check if the dice can be placed on the tile designated by row and column based on the standard constraint
     * (TileConstraintType.NUMBER_COLOR, DiceConstraintType.NORMAL)
     *
     * @param dice   the dice to check if positionable
     * @param row    the row of the tile
     * @param column the column of the tile
     * @return true if the dice can be placed on the point
     */
    @Contract(pure = true)
    public boolean isDicePositionable(Dice dice, int row, int column) {
        return isDicePositionable(dice, row, column, TileConstraintType.NUMBER_COLOR, DiceConstraintType.NORMAL);
    }

    /**
     * Check if the dice can be placed on the tile designated by row and column based on constraint given
     *
     * @param dice           the dice to check if positionable
     * @param row            the row of the tile
     * @param column         the column of the tile
     * @param diceConstraint the constraint to check on the placement of dice
     * @param tileConstraint the constraint to check on the tile
     * @return true if the dice can be placed on the point
     */
    @Contract(pure = true)
    public boolean isDicePositionable(Dice dice, int row, int column, TileConstraintType tileConstraint,
                                      DiceConstraintType diceConstraint) {
        if (isEmpty()) {
            return isBorderPosition(row, column) && tileMatrix[row][column].isDicePositionable(dice, tileConstraint);
        } else {
            switch (diceConstraint) {
                case NORMAL:
                    return tileMatrix[row][column].isDicePositionable(dice, tileConstraint) &&
                            !hasOrthogonalDicesSimilar(dice, row, column) &&
                            getNumberOfAdjacentDices(row, column) > 0;
                case ISOLATED:
                    return tileMatrix[row][column].isDicePositionable(dice, tileConstraint) &&
                            !hasOrthogonalDicesSimilar(dice, row, column) &&
                            getNumberOfAdjacentDices(row, column) == 0;
                default:
                    throw new IllegalArgumentException("impossible case");
            }
        }
    }

    /**
     * @return true if the schemaCard has no dice placed in it
     */
    @Contract(pure = true)
    public boolean isEmpty() {
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                if (getDice(i, j) != null)
                    return false;

            }
        }
        return true;
    }

    /**
     * Returns the deep copy of the tile requested
     *
     * @param row    the row of the tile
     * @param column the column of the tile
     * @return the tile requested by row and column
     */
    public Tile getTile(int row, int column) {
        return Tile.newInstance(tileMatrix[row][column]);
    }

    /**
     * Override of the equals method
     *
     * @param obj the other object to compare
     * @return true if *hasSameTiles(same constraint and same dice placed on it)* &&
     * this.getName().equals(obj.getName()) &&
     * this.getDifficulty() == obj.getDifficulty();
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;
        if (!(obj instanceof SchemaCard))
            return false;
        SchemaCard other = (SchemaCard) obj;
        boolean hasSameTiles = true;
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                if (!getTile(i, j).equals(other.getTile(i, j)))
                    hasSameTiles = false;
            }
        }
        return hasSameTiles && this.getName().equals(other.getName()) && this.getDifficulty() == other.getDifficulty();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), tileMatrix, getDifficulty());
    }

    /**
     * Given row and column, returns if the position indicated is on the border or not
     *
     * @param row    the row
     * @param column the column
     * @return true if the position is on the border of the schemaCard
     */
    @Contract(pure = true)
    private boolean isBorderPosition(int row, int column) {
        return row == 0 || column == 0 || column == NUMBER_OF_COLUMNS - 1 || row == NUMBER_OF_ROWS - 1;
    }

    /**
     * Check if the orthogonal dices around the tile(row, column) are similar to the dice given
     *
     * @param dice   the dice to compare with the orthogonal dices
     * @param row    the row of the tile
     * @param column the column of the tile
     * @return true if there is at least one orthogonal dice similar to the dice given
     */
    @Contract(pure = true)
    private boolean hasOrthogonalDicesSimilar(Dice dice, int row, int column) {
        for (int delta = -1; delta <= 1; delta++) {
            if (delta != 0) {
                if (!isOutOfBounds(row + delta, column) && getDice(row + delta, column) != null
                        && dice.isSimilar(getDice(row + delta, column))) {
                    return true;
                }
                if (!isOutOfBounds(row, column + delta) && getDice(row, column + delta) != null
                        && dice.isSimilar(getDice(row, column + delta))) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Given a position designated by row and column, return the number of adjacent(orthogonal and diagonal) dices
     *
     * @param row    the row of the position
     * @param column the column of the position
     * @return the number of adjacent dices near the position given
     */
    @Contract(pure = true)
    private int getNumberOfAdjacentDices(int row, int column) {
        int numberOfAdjacentDice = 0;
        for (int deltaRow = -1; deltaRow <= 1; deltaRow++)
            for (int deltaColumn = -1; deltaColumn <= 1; deltaColumn++)
                if (!(deltaRow == 0 && deltaColumn == 0) &&
                        !isOutOfBounds(row + deltaRow, column + deltaColumn) &&
                        getDice(row + deltaRow, column + deltaColumn) != null)
                    numberOfAdjacentDice++;
        return numberOfAdjacentDice;
    }


    /**
     * Given row and column, return whether or not the position is out of bounds according
     * to the matrix tile
     *
     * @param row    the row of the position
     * @param column the column of the position
     * @return true if out of bounds, false otherwise
     */
    @Contract(pure = true)
    public static boolean isOutOfBounds(int row, int column) {
        return row < 0 || row > SchemaCard.NUMBER_OF_ROWS - 1 || column < 0 || column > SchemaCard.NUMBER_OF_COLUMNS - 1;
    }


    /**
     * @return the number of empty spaces inside the tileMatrix
     */
    public int getNumberOfEmptySpaces() {
        int numberOfEmptySpaces = 0;
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                if (getDice(i, j) == null)
                    numberOfEmptySpaces++;
            }
        }
        return numberOfEmptySpaces;
    }


    public static SchemaCard newInstance(SchemaCard schema) {
        //TODO
        return schema;
    }
}
