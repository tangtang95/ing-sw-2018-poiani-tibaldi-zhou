package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.jetbrains.annotations.Contract;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.network.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

import java.util.Arrays;
import java.util.Objects;

/**
 * Copy class of SchemaCard in the game model.
 */
@Immutable
public final class SchemaCardWrapper implements JSONable{

    /**
     * Card's name.
     */
    private final String name;

    /**
     * Card's difficulty.
     */
    private final int difficulty;

    /**
     * Schema card matrix with constraint and diceWrapper.
     */
    private final TileWrapper[][] tileMatrix;

    /**
     * SchemaCardWrapper param for network protocol.
     */
    private static final String JSON_DIFFICULTY = "difficulty";
    private static final String JSON_MATRIX = "matrix";

    /**
     * SchemaCardWrapper Parameter.
     */
    public static final int NUMBER_OF_COLUMNS = 5;
    public static final int NUMBER_OF_ROWS = 4;

    /**
     * Constructor.
     *
     * @param name card name.
     * @param difficulty card difficulty.
     * @param tileMatrix card matrix.
     */
    public SchemaCardWrapper(String name, int difficulty, TileWrapper[][] tileMatrix) {
        this.name = name;
        this.difficulty = difficulty;
        this.tileMatrix = new TileWrapper[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                this.tileMatrix[i][j] = TileWrapper.newInstance(tileMatrix[i][j]);
            }
        }
    }

    /**
     * @return the card name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return the card difficulty.
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Return the tileWrapper requested
     *
     * @return the tileWrapper requested by position
     */
    @Contract(pure = true)
    public TileWrapper getTile(PositionWrapper position) {
        return Objects.requireNonNull(tileMatrix)[position.getRow()][position.getColumn()];
    }

    /**
     * Convert a schemaCardWrapper in a JSONObject.
     *
     * @return a JSONObject.
     */
    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject main = new JSONObject();
        JSONArray matrix = new JSONArray();
        JSONObject schemaCardJSON = new JSONObject();

        schemaCardJSON.put(CardWrapper.JSON_NAME, this.getName());
        schemaCardJSON.put(JSON_DIFFICULTY, this.getDifficulty());

        for (int i = 0; i < SchemaCardWrapper.NUMBER_OF_ROWS; i++) {
            JSONArray row = new JSONArray();
            for (int j = 0; j < SchemaCardWrapper.NUMBER_OF_COLUMNS; j++)
                row.add(this.getTile(new PositionWrapper(i,j)).toJSON());
            matrix.add(row);
        }
        schemaCardJSON.put(JSON_MATRIX, matrix);
        main.put(SharedConstants.TYPE, SharedConstants.SCHEMA_CARD);
        main.put(SharedConstants.BODY,schemaCardJSON);
        return main;
    }

    /**
     * Convert a json string in a SchemaCardWrapper object.
     *
     * @param jsonObject a JSONObject that contains a schema card.
     * @return a SchemaCardWWrapper object.
     */
    public static SchemaCardWrapper toObject(JSONObject jsonObject) {
        TileWrapper[][] tiles = new TileWrapper[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];
        JSONArray matrix = (JSONArray) jsonObject.get(JSON_MATRIX);
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            JSONArray row = (JSONArray) matrix.get(i);
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                tiles[i][j] = TileWrapper.toObject((JSONObject) ((JSONObject) row.get(j)).get(SharedConstants.BODY));
            }
        }
        return new SchemaCardWrapper(
                jsonObject.get(CardWrapper.JSON_NAME).toString(),
                Integer.parseInt(jsonObject.get(JSON_DIFFICULTY).toString()),
                tiles
        );
    }

    /**
     * @return Schema card wrapper to string ->  "  -----   -----   -----   -----   -----  "
     *                                           "|       |   2   |   Y   |       |       |"
     *                                           "  -----   -----   -----   -----   -----  "
     *                                           "|       |       |       |   4   |       |"
     *                                           "  -----   -----   -----   -----   -----  "
     *                                           "|       |       |       |       |   R   |"
     *                                           "  -----   -----   -----   -----   -----  "
     *                                           "|       |  4/R  |       |       |       |"
     *                                           "  -----   -----   -----   -----   -----  "
     */
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

    /**
     * @param o the other object to compare.
     * @return true if the SchemaCardWrapper is the same object or if the matrix is the same, name is same and
     * difficulty is the same..
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SchemaCardWrapper)) return false;
        SchemaCardWrapper that = (SchemaCardWrapper) o;
        boolean hasSameTiles = true;
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                if (!getTile(new PositionWrapper(i, j)).equals(that.getTile(new PositionWrapper(i, j))))
                    hasSameTiles = false;
            }
        }
        return getDifficulty() == that.getDifficulty() &&
                Objects.equals(getName(), that.getName()) &&
                hasSameTiles;
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {

        int result = Objects.hash(getName(), getDifficulty());
        result = 31 * result + Arrays.hashCode(tileMatrix);
        return result;
    }
}
