package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.jetbrains.annotations.Contract;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.JSONClientProtocol;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

import java.util.Objects;

@Immutable
public class SchemaCardWrapper implements JSONable{

    private final String name;
    private final int difficulty;
    private final TileWrapper[][] tileMatrix;

    /**
     * SchemaCardWrapper param for network protocol.
     */
    private static final String JSON_DIFFICULTY = "difficulty";
    private static final String JSON_MATRIX = "matrix";

    public static final int NUMBER_OF_COLUMNS = 5;
    public static final int NUMBER_OF_ROWS = 4;


    public SchemaCardWrapper(String name, int difficulty, TileWrapper[][] tileMatrix) {
        this.name = name;
        this.difficulty = difficulty;
        this.tileMatrix = tileMatrix;
    }

    public String getName() {
        return name;
    }

    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Return the tileWrapper requested
     *
     * @return the tileWrapper requested by position
     */
    @Contract(pure = true)
    private TileWrapper getTile(PositionWrapper position) {
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
    @Override
    public Object toObject(JSONObject jsonObject) {
        JSONClientProtocol protocol = new JSONClientProtocol();
        TileWrapper[][] tiles = new TileWrapper[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];
        JSONArray matrix = (JSONArray) jsonObject.get(JSON_MATRIX);
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            JSONArray row = (JSONArray) matrix.get(i);
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                tiles[i][j] = (TileWrapper) protocol.convertToObject((JSONObject) row.get(j));
            }
        }
        return new SchemaCardWrapper(
                jsonObject.get(CardWrapper.JSON_NAME).toString(),
                Integer.parseInt(jsonObject.get(JSON_DIFFICULTY).toString()),
                tiles
        );
    }

    /**
     * Fake constructor.
     */
    @SuppressWarnings("unused")
    private SchemaCardWrapper() {
        this.tileMatrix = null;
        this.difficulty = 0;
        this.name = null;
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
