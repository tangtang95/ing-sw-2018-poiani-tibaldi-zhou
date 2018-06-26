package org.poianitibaldizhou.sagrada.game.model.cards;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.exception.RuleViolationType;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.dice.DiceRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement.PlacementRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.constraint.IConstraint;
import org.poianitibaldizhou.sagrada.network.protocol.JSONable;
import org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces.ISchemaCardFakeObserver;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

import java.util.*;

/**
 * OVERVIEW: Represents a schema card of Sagrada
 */
public class SchemaCard implements JSONable {
    private final String name;
    private final int difficulty;
    private final Tile[][] tileMatrix;
    private final Map<String, ISchemaCardFakeObserver> observerMap;

    public static final int NUMBER_OF_COLUMNS = 5;
    public static final int NUMBER_OF_ROWS = 4;

    /**
     * SchemaCard param for network protocol.
     */
    private static final String JSON_DIFFICULTY = "difficulty";
    private static final String JSON_MATRIX = "matrix";

    /**
     * Constructor: creates a SchemaCard without any dice on it
     *
     * @param name        the name of the schema card
     * @param difficulty  the difficulty of the schema card
     * @param constraints a matrix of constraints of the tile (NUMBER_OF_ROWS x NUMBER_OF_COLUMNS)
     */
    public SchemaCard(String name, int difficulty, IConstraint[][] constraints) {
        this.name = name;
        this.difficulty = difficulty;
        this.tileMatrix = new Tile[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];
        this.observerMap = new HashMap<>();
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                this.tileMatrix[i][j] = new Tile(constraints[i][j]);
            }
        }
    }

    /**
     * Constructor: creates a SchemaCard with dice on it
     *
     * @param name        the name of the schema card
     * @param difficulty  the difficulty of the schema card
     * @param tileMatrix a matrix of tile with or without dice (NUMBER_OF_ROWS x NUMBER_OF_COLUMNS)
     */
    public SchemaCard(String name, int difficulty, Tile[][] tileMatrix) {
        this.name = name;
        this.difficulty = difficulty;
        this.tileMatrix = tileMatrix;
        this.observerMap = new HashMap<>();
    }

    /**
     * Copy-constructor (Note: shallow copy of the observer)
     *
     * @param schemaCard the schemaCard to copy
     */
    private SchemaCard(SchemaCard schemaCard) {
        this.name = schemaCard.name;
        this.difficulty = schemaCard.difficulty;
        this.tileMatrix = new Tile[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                this.tileMatrix[i][j] = Tile.newInstance(schemaCard.tileMatrix[i][j]);
            }
        }
        this.observerMap = new HashMap<>(schemaCard.getObserverMap());
    }

    //GETTER
    @Contract(pure = true)
    public String getName() {
        return name;
    }


    /**
     * Returns a list the list of the observers.
     * This method creates a new instance of the list, so it is safe to modify the list, but not safe to modify
     * the observers since is not a deep copy.
     *
     * @return list of observers
     */
    public Map<String, ISchemaCardFakeObserver> getObserverMap() {
        return new HashMap<>(observerMap);
    }

    @Contract(pure = true)
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Returns the dice on the tile designated by row and column
     *
     * @param position the position from where to get the dice
     * @return the dice positioned on the point (if there is no dice on the tile, then it returns null)
     */
    @Contract(pure = true)
    public Dice getDice(Position position) {
        return tileMatrix[position.getRow()][position.getColumn()].getDice();
    }

    /**
     * Return the DEEP COPY of the tile requested
     *
     * @return the tile requested by position
     */
    @Contract(pure = true)
    private Tile getTile(Position position) {
        return Tile.newInstance(tileMatrix[position.getRow()][position.getColumn()]);
    }

    /**
     * Check if the dice can be placed on the tile designated by row and column based on constraint given
     *
     * @param dice            the dice to check if positionable
     * @param position        the position of the tile
     * @param restriction     the constraint to check on the tile
     * @param diceRestriction the constraint to check on the placement of dice
     * @return true if the dice can be placed on the point
     */
    @Contract(pure = true)
    public boolean isDicePositionable(Dice dice, Position position, PlacementRestrictionType restriction,
                                      DiceRestrictionType diceRestriction) {
        if (isEmpty()) {
            return isBorderPosition(position) &&
                    tileMatrix[position.getRow()][position.getColumn()].isDicePositionable(dice, restriction);
        } else {
            return tileMatrix[position.getRow()][position.getColumn()].isDicePositionable(dice, restriction) &&
                    !hasOrthogonalDicesSimilar(dice, position) &&
                    diceRestriction.getDiceRestriction().isCorrectNumberOfAdjacentDices(getNumberOfAdjacentDices(position));
        }
    }

    /**
     * Check if the dice can be placed on the tile designated by row and column based on constraint given
     *
     * @param dice            the dice to check if positionable
     * @param row             the row of the tile position
     * @param column          the column of the tile position
     * @param restriction     the constraint to check on the tile
     * @param diceRestriction the constraint to check on the placement of dice
     * @return true if the dice can be placed on the point
     */
    @Contract(pure = true)
    public boolean isDicePositionable(Dice dice, int row, int column, PlacementRestrictionType restriction,
                                      DiceRestrictionType diceRestriction) {
        return isDicePositionable(dice, new Position(row, column), restriction, diceRestriction);
    }

    /**
     * Check if the dice can be placed on the tile designated by row and column based on the standard constraint
     * (PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.NORMAL)
     *
     * @param dice     the dice to check if positionable
     * @param position the position of the tile
     * @return true if the dice can be placed on the point
     */
    @Contract(pure = true)
    public boolean isDicePositionable(Dice dice, Position position) {
        return isDicePositionable(dice, position, PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.NORMAL);
    }

    /**
     * Check if the dice can be placed on the tile designated by row and column based on the standard constraint
     * (PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.NORMAL)
     *
     * @param dice   the dice to check if positionable
     * @param row    the row of the tile position
     * @param column the column of the tile position
     * @return true if the dice can be placed on the point
     */
    @Contract(pure = true)
    public boolean isDicePositionable(Dice dice, int row, int column) {
        return isDicePositionable(dice, new Position(row, column));
    }

    /**
     * Returns true if the dice is positionable following the given restrictions somewhere on the
     * the schemaCard, false otherwise
     *
     * @param dice            dice that needs to be placed
     * @param restriction     placement restrictions to respect
     * @param diceRestriction dice restrictions to respect
     * @return true if the dice is positionable, false otherwise
     */
    @Contract(pure = true)
    public boolean isDicePositionable(Dice dice, PlacementRestrictionType restriction,
                                      DiceRestrictionType diceRestriction) {
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                if (isDicePositionable(dice, new Position(i, j), restriction, diceRestriction))
                    return true;
            }
        }

        return false;
    }

    /**
     * Return the number of empty spaces inside the tileMatrix
     *
     * @return the number of empty spaces inside the tileMatrix
     */
    @Contract(pure = true)
    public int getNumberOfEmptySpaces() {
        int numberOfEmptySpaces = 0;
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                if (getDice(new Position(i, j)) == null)
                    numberOfEmptySpaces++;
            }
        }
        return numberOfEmptySpaces;
    }

    /**
     * Checks if the schemaCard has a dice of a certain color placed on it.
     *
     * @param color specified color
     * @return true if a dice of color is present on the board, false otherwise
     */
    @Contract(pure = true)
    public boolean hasDiceOfColor(Color color) {
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                if (getDice(new Position(i, j)) != null && getDice(new Position(i, j)).getColor().equals(color))
                    return true;
            }
        }
        return false;
    }

    /**
     * @return true if the schemaCard has no dice placed in it
     */
    @Contract(pure = true)
    public boolean isEmpty() {
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                if (getDice(new Position(i, j)) != null)
                    return false;
            }
        }
        return true;
    }


    //MODIFIER

    /**
     * Set the dice on a tile indicated by row and column based on a standard constraint
     * (PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.NORMAL)
     * It also notifies the observers that a dice has been placed in a certain position.
     *
     * @param dice     the dice to place on the schemaCard
     * @param position the row where to place the dice
     * @throws RuleViolationException if getNeededDice(row, column) != null ||
     *                                hasOrthogonalDicesSimilar() ||
     *                                this.isEmpty() && !this.isOutOfBounds(row,column) ||
     *                                !this.isEmpty() && getNumberOfAdjacentDices() == 0
     */
    public void setDice(Dice dice, Position position) throws RuleViolationException{
        setDice(dice, position, PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.NORMAL);
    }

    /**
     * Set the dice on a tile indicated by row and column based on a standard constraint
     * (PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.NORMAL)
     * It also notifies the observers that a dice has been placed in a certain position.
     *
     * @param dice   the dice to place on the schemaCard
     * @param row    the row where to place the dice
     * @param column the column where to place the dice
     * @throws RuleViolationException if getNeededDice(row, column) != null ||
     *                                hasOrthogonalDicesSimilar() ||
     *                                this.isEmpty() && !this.isOutOfBounds(row,column) ||
     *                                !this.isEmpty() && getNumberOfAdjacentDices() == 0
     */
    public void setDice(Dice dice, int row, int column) throws RuleViolationException{
        setDice(dice, new Position(row, column));
    }

    /**
     * Set the dice on a tile indicated by row and column based on the two type of constraint given
     * It also notifies the observers that a dice has been placed in a certain position.
     *
     * @param dice            the dice to place on the schemaCard
     * @param position        the position where to place the dice
     * @param restriction     the constraint to check on the tile
     * @param diceRestriction the constraint to check on the placement of dice
     * @throws RuleViolationException if getNeededDice(row, column) != null ||
     *                                hasOrthogonalDicesSimilar() ||
     *                                (this.isEmpty() && !this.isOutOfBounds(row,column)) ||
     *                                (diceRestriction == NORMAL && !this.isEmpty() &&
     *                                getNumberOfAdjacentDices() == 0 ||
     *                                (diceRestriction == ISOLATED &&
     *                                getNumberOfAdjacentDices() > 0
     */
    public void setDice(Dice dice, Position position, PlacementRestrictionType restriction,
                        DiceRestrictionType diceRestriction) throws RuleViolationException {
        if (isEmpty()) {
            if (!isBorderPosition(position)) {
                throw new RuleViolationException(RuleViolationType.NOT_BORDER_TILE);
            }
            if (!tileMatrix[position.getRow()][position.getColumn()].isDicePositionable(dice, restriction)) {
                throw new RuleViolationException(RuleViolationType.TILE_UNMATCHED);
            }
        } else {
            if (!tileMatrix[position.getRow()][position.getColumn()].isDicePositionable(dice, restriction)) {
                throw new RuleViolationException(RuleViolationType.TILE_UNMATCHED);
            }
            if (hasOrthogonalDicesSimilar(dice, position)) {
                throw new RuleViolationException(RuleViolationType.SIMILAR_DICE_NEAR);
            }
            int numberOfAdjacentDices = getNumberOfAdjacentDices(position);
            if (!diceRestriction.getDiceRestriction().isCorrectNumberOfAdjacentDices(numberOfAdjacentDices)) {
                if (diceRestriction == DiceRestrictionType.NORMAL) {
                    throw new RuleViolationException(RuleViolationType.NO_DICE_NEAR);
                }
                else {
                    throw new RuleViolationException(RuleViolationType.HAS_DICE_NEAR);
                }
            }
        }
        tileMatrix[position.getRow()][position.getColumn()].setDice(dice, restriction);

        observerMap.forEach((key, value) -> value.onPlaceDice(dice, position));
    }

    /**
     * Set the dice on a tile indicated by row and column based on a standard constraint
     * (PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.NORMAL).
     * It also notifies the observers that a dice has been placed in a certain position.
     *
     * @param dice            the dice to place on the schemaCard
     * @param row             the row where to place the dice
     * @param column          the column where to place the dice
     * @param restriction     the constraint to check on the tile
     * @param diceRestriction the constraint to check on the placement of dice
     * @throws RuleViolationException if getNeededDice(row, column) != null ||
     *                                hasOrthogonalDicesSimilar() ||
     *                                this.isEmpty() && !this.isOutOfBounds(row,column) ||
     *                                !this.isEmpty() && getNumberOfAdjacentDices() == 0
     */
    public void setDice(Dice dice, int row, int column, PlacementRestrictionType restriction,
                        DiceRestrictionType diceRestriction) throws RuleViolationException{
        setDice(dice, new Position(row, column), restriction, diceRestriction);
    }


    /**
     * Remove the dice from the tile designated by row and column
     * It also notifies to the observers that the dice's been removed.
     *
     * @param position the position from where to remove the dice
     * @return the dice removed from the point position (if there is no dice it returns null)
     */
    public Dice removeDice(Position position) {
        Dice removedDice = tileMatrix[position.getRow()][position.getColumn()].removeDice();
        observerMap.forEach((key, value) -> value.onDiceRemove(removedDice, position));
        return removedDice;
    }

    public void attachObserver(String token, ISchemaCardFakeObserver observer) {
        observerMap.put(token, observer);
    }

    public void detachObserver(String token){
        observerMap.remove(token);
    }

    /**
     * Override of the equals method
     *
     * @param obj the other object to compare
     * @return true if *hasSameTiles(same constraint and same dice placed on it)* &&
     * this.getName().equals(obj.getName()) &&
     * this.getDifficultyValue() == obj.getDifficultyValue();
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof SchemaCard))
            return false;
        SchemaCard other = (SchemaCard) obj;
        boolean hasSameTiles = true;
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                if (!getTile(new Position(i, j)).equals(other.getTile(new Position(i, j))))
                    hasSameTiles = false;
            }
        }
        return hasSameTiles && this.getName().equals(other.getName()) && this.getDifficulty() == other.getDifficulty();
    }

    @Override
    public int hashCode() {
        List<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            tiles.addAll(Arrays.asList(tileMatrix[i]).subList(0, NUMBER_OF_COLUMNS));
        }
        return Objects.hash(SchemaCard.class, name, tiles, difficulty);
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
     * This creates a new instance of the schema card. Observers are not inserted in the new instance.
     *
     * @param schemaCard schema card that needs to be copied
     * @return schemaCard copy
     */
    public static SchemaCard newInstance(@NotNull SchemaCard schemaCard) {
        return new SchemaCard(schemaCard);
    }

    /**
     * Given row and column, returns if the position indicated is on the border or not
     *
     * @param position the position
     * @return true if the position is on the border of the schemaCard
     */
    @Contract(pure = true)
    private boolean isBorderPosition(Position position) {
        return position.getRow() == 0 || position.getColumn() == 0 ||
                position.getColumn() == NUMBER_OF_COLUMNS - 1 || position.getRow() == NUMBER_OF_ROWS - 1;
    }

    /**
     * Check if the orthogonal dices around the tile(row, column) are similar to the dice given
     *
     * @param dice     the dice to compare with the orthogonal dices
     * @param position the position of the tile
     * @return true if there is at least one orthogonal dice similar to the dice given
     */
    @Contract(pure = true)
    private boolean hasOrthogonalDicesSimilar(Dice dice, Position position) {
        for (int delta = -1; delta <= 1; delta++) {
            if (delta != 0) {
                if (!isOutOfBounds(position.getRow() + delta, position.getColumn())
                        && getDice(position.add(delta, 0)) != null
                        && dice.isSimilar(getDice(position.add(delta, 0)))) {
                    return true;
                }
                if (!isOutOfBounds(position.getRow(), position.getColumn() + delta)
                        && getDice(position.add(0, delta)) != null
                        && dice.isSimilar(getDice(position.add(0, delta)))) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Given a position designated by row and column, return the number of adjacent(orthogonal and diagonal) dices
     *
     * @param position the position
     * @return the number of adjacent dices near the position given
     */
    @Contract(pure = true)
    private int getNumberOfAdjacentDices(Position position) {
        int numberOfAdjacentDice = 0;
        for (int deltaRow = -1; deltaRow <= 1; deltaRow++)
            for (int deltaColumn = -1; deltaColumn <= 1; deltaColumn++)
                if (!(deltaRow == 0 && deltaColumn == 0) &&
                        !isOutOfBounds(position.getRow() + deltaRow, position.getColumn() + deltaColumn) &&
                        getDice(position.add(deltaRow, deltaColumn)) != null)
                    numberOfAdjacentDice++;
        return numberOfAdjacentDice;
    }

    /**
     * Convert a schemaCard in a JSONObject.
     *
     * @return a JSONObject.
     */
    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject main = new JSONObject();
        JSONArray matrix = new JSONArray();
        JSONObject schemaCardJSON = new JSONObject();

        schemaCardJSON.put(Card.JSON_NAME, this.getName());
        schemaCardJSON.put(JSON_DIFFICULTY, this.getDifficulty());

        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            JSONArray row = new JSONArray();
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++)
                row.add(this.getTile(new Position(i,j)).toJSON());
            matrix.add(row);
        }
        schemaCardJSON.put(JSON_MATRIX, matrix);
        main.put(SharedConstants.TYPE, SharedConstants.SCHEMA_CARD);
        main.put(SharedConstants.BODY,schemaCardJSON);
        return main;
    }

    /**
     * Convert a json string in a SchemaCard object.
     *
     * @param jsonObject a JSONObject that contains a schema card.
     * @return a SchemaCard object.
     */
    public static SchemaCard toObject(JSONObject jsonObject) {
        Tile[][] tiles = new Tile[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];
        JSONArray matrix = (JSONArray) jsonObject.get(JSON_MATRIX);
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            JSONArray row = (JSONArray) matrix.get(i);
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                tiles[i][j] = Tile.toObject((JSONObject) ((JSONObject) row.get(j)).get(SharedConstants.BODY));
            }
        }
        return new SchemaCard(
                jsonObject.get(Card.JSON_NAME).toString(),
                Integer.parseInt(jsonObject.get(JSON_DIFFICULTY).toString()),
                tiles
        );
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("  -----   -----   -----   -----   -----  \n");
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                stringBuilder.append("|  ").append(Objects.requireNonNull(tileMatrix)[i][j].toString()).append("  ");
            }
            stringBuilder.append("|\n");
            stringBuilder.append("  -----   -----   -----   -----   -----  \n");
        }
        return stringBuilder.toString();
    }
}
