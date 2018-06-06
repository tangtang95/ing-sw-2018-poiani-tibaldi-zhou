package org.poianitibaldizhou.sagrada.network.protocol;


import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to convert to string the object or communicating with server.
 */
public class JSONClientProtocol extends JSONProtocol {

    private static final Map<String, Class> classMap = new HashMap<>();

    /**
     * constructor.
     * Set up the classMap with the loadable class of the game.
     */
    public JSONClientProtocol() {
        super(classMap);
        classMap.put(SharedConstants.COLLECTION, Collection.class);
        classMap.put(SharedConstants.DICE, DiceWrapper.class);
        classMap.put(SharedConstants.DRAFT_POOL, DraftPoolWrapper.class);
        classMap.put(SharedConstants.INTEGER, Integer.class);
        classMap.put(SharedConstants.MAP, Map.class);
        classMap.put(SharedConstants.TILE, TileWrapper.class);
        classMap.put(SharedConstants.POSITION, PositionWrapper.class);
        classMap.put(SharedConstants.PRIVATE_OBJECTIVE_CARD, PrivateObjectiveCardWrapper.class);
        classMap.put(SharedConstants.PUBLIC_OBJECTIVE_CARD, PublicObjectiveCardWrapper.class);
        classMap.put(SharedConstants.ROUND_TRACK, RoundTrackWrapper.class);
        classMap.put(SharedConstants.STRING, String.class);
        classMap.put(SharedConstants.TOOL_CARD, ToolCardWrapper.class);
        classMap.put(SharedConstants.USER, UserWrapper.class);
        classMap.put(SharedConstants.SCHEMA_CARD, SchemaCardWrapper.class);
        classMap.put(SharedConstants.COLOR, ColorWrapper.class);
        classMap.put(SharedConstants.END_TURN_ACTION, EndTurnStateWrapper.class);
        classMap.put(SharedConstants.PLACE_DICE_ACTION, PlaceDiceStateWrapper.class);
        classMap.put(SharedConstants.USE_TOOL_CARD_ACTION, UseToolCardStateWrapper.class);
        classMap.put(SharedConstants.FRONT_BACK_SCHEMA_CARD, FrontBackSchemaCardWrapper.class);
    }
}
