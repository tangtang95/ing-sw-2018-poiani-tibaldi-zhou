package org.poianitibaldizhou.sagrada.game.model.cards;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.exception.RuleViolationType;
import org.poianitibaldizhou.sagrada.exception.SchemaCardPointOutOfBoundsException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.IConstraint;

public class SchemaCard {
    private final String name;
    private final int difficulty;
    private Tile[][] tileMatrix;

    public static final int NUMBER_OF_COLUMNS = 5;
    public static final int NUMBER_OF_ROWS = 4;

    /**
     * Constructor: create a SchemaCard without any dice on it
     * @param name the name of the schema card
     * @param difficulty the difficulty of the schema card
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

    /**
     *
     * @return the name of the schema card
     */
    @Contract(pure = true)
    public String getName() {
        return name;
    }

    /**
     *
     * @return the difficulty of the schema card
     */
    @Contract(pure = true)
    public int getDifficulty() {
        return difficulty;
    }

    /**
     *
     * @param dice the dice to place on the schemaCard
     * @param point the position to place the dice (row, column)
     * @throws RuleViolationException if there are some violation of the rules
     */
    public void setDice(Dice dice, SchemaCardPoint point) throws RuleViolationException{
        setDice(dice, point, new ConstraintType(TileConstraintType.NUMBER_COLOR, DiceConstraintType.NORMAL));
    }

    /**
     *
     * @param dice the dice to place on the schemaCard
     * @param point the position to place the dice (row, column)
     * @param type the constraint (containing tile constraint and dice constraint) applied when setting the dice
     * @throws RuleViolationException if there are some violation of the rules
     */
    public void setDice(Dice dice, SchemaCardPoint point, ConstraintType type) throws RuleViolationException{
        if (isDicePositionable(dice, point, type))
            tileMatrix[point.row][point.column].setDice(dice, type.getTileConstraint());
    }

    /**
     *
     * @param point the position from where to get the dice (row, column)
     * @return the dice positioned on the point
     */
    @Contract(pure = true)
    public Dice getDice(SchemaCardPoint point) {
        return tileMatrix[point.row][point.column].getDice();
    }

    /**
     *
     * @param point the position from where to remove the dice
     * @return the dice removed from the point position
     */
    public Dice removeDice(SchemaCardPoint point) {
        return tileMatrix[point.row][point.column].removeDice();
    }

    /**
     *
     * @param dice the dice to check if positionable
     * @param point the position to try to place
     * @return true if the dice can be placed on the point
     * @throws RuleViolationException if there are some rule violation
     */
    @Contract(pure = true)
    public boolean isDicePositionable(Dice dice, SchemaCardPoint point) throws RuleViolationException{
        return isDicePositionable(dice, point, new ConstraintType(TileConstraintType.NUMBER_COLOR, DiceConstraintType.NORMAL));
    }

    /**
     *
     * @param dice the dice to check if positionable
     * @param point the position to try to place
     * @param type the constraint (containing tile constraint and dice constraint) applied
     * @return true if the dice can be placed on the point
     * @throws RuleViolationException if there are some rule violation
     */
    @Contract(pure = true)
    public boolean isDicePositionable(Dice dice, SchemaCardPoint point, ConstraintType type) throws RuleViolationException{
        if (isEmpty()) {
            if (!isBorderPoint(point)) {
                throw new RuleViolationException(RuleViolationType.NOT_BORDER_TILE);
            }
            if(!tileMatrix[point.row][point.column].isDicePositionable(dice, type.getTileConstraint())) {
                throw new RuleViolationException(RuleViolationType.TILE_UNMATCHED);
            }
            return true;
        } else {
            if(!tileMatrix[point.row][point.column].isDicePositionable(dice, type.getTileConstraint())) {
                throw new RuleViolationException(RuleViolationType.TILE_UNMATCHED);
            }
            if(!isAdjacentDicesRulesValid(dice, point)) {
                if (type.getDiceConstraint() == DiceConstraintType.NORMAL)
                    throw new RuleViolationException(RuleViolationType.NO_DICE_NEAR);
                else if(type.getDiceConstraint() == DiceConstraintType.ISOLATED)
                    return true;
            }
            return true;
        }
    }

    /**
     *
     * @param point the position to check
     * @return true if the point is on the border of the schemaCard
     */
    @Contract(pure = true)
    private boolean isBorderPoint(SchemaCardPoint point){
        return point.row == 0 || point.column == 0 || point.column == NUMBER_OF_COLUMNS - 1 || point.row == NUMBER_OF_ROWS - 1;
    }

    /**
     *
     * @param dice
     * @param point
     * @return true if there is at least one adjacent dice
     * @throws RuleViolationException if there are rule violation for a similar orthogonal adjacent dice (same color or same number)
     */
    @Contract(pure = true)
    private boolean isAdjacentDicesRulesValid(Dice dice, SchemaCardPoint point) throws RuleViolationException {
        int numberOfAdjacentDice = 0;
        for (int deltaRow = -1; deltaRow <= 1; deltaRow++) {
            for (int deltaColumn = -1; deltaColumn <= 1; deltaColumn++) {
                if (deltaRow == 0 && deltaColumn == 0) continue;
                if (!SchemaCardPoint.isOutOfBounds(point.row + deltaRow, point.column + deltaColumn)) {
                    try {
                        Dice tileDice = getDice(new SchemaCardPoint(point.row + deltaRow, point.column + deltaColumn));
                        if (tileDice != null) {
                            numberOfAdjacentDice++;
                            if (Math.abs(deltaRow) + Math.abs(deltaColumn) == 1) {
                                //Orthogonal direction
                                
                                if (tileDice.getColorConstraint().matches(dice.getColorConstraint()) ||
                                        tileDice.getNumberConstraint().matches(dice.getNumberConstraint()))
                                    throw new RuleViolationException(RuleViolationType.SIMILAR_DICE_NEAR);
                            }
                        }
                    } catch (SchemaCardPointOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return numberOfAdjacentDice > 0;
    }

    /**
     *
     * @return true if the schemaCard has no dice placed in it
     */
    @Contract(pure = true)
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

    /**
     *
     * @param point the position from where to get the tile
     * @return the tile of that specific point passed
     */
    public Tile getTile(SchemaCardPoint point){
        return new Tile(tileMatrix[point.row][point.column]);
    }

}
