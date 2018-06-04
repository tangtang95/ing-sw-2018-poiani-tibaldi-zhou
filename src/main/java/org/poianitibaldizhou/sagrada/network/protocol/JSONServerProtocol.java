package org.poianitibaldizhou.sagrada.network.protocol;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.Tile;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Class to convert to string the object for communicating with client.
 */
public class JSONServerProtocol extends JSONProtocol{

    private static final Map<String, Class> classMap = new HashMap<>();

    /**
     * constructor.
     * Set up the classMap with the loadable class of the game.
     */
    public JSONServerProtocol() {
        super(classMap);
        classMap.put(SharedConstants.COLLECTION, Collection.class);
        classMap.put(SharedConstants.DICE, Dice.class);
        classMap.put(SharedConstants.DRAFT_POOL, DraftPool.class);
        classMap.put(SharedConstants.INTEGER, Integer.class);
        classMap.put(SharedConstants.MAP, Map.class);
        classMap.put(SharedConstants.TILE, Tile.class);
        classMap.put(SharedConstants.POSITION, Position.class);
        classMap.put(SharedConstants.PRIVATE_OBJECTIVE_CARD, PrivateObjectiveCard.class);
        classMap.put(SharedConstants.PUBLIC_OBJECTIVE_CARD, PublicObjectiveCard.class);
        classMap.put(SharedConstants.ROUND_TRACK, RoundTrack.class);
        classMap.put(SharedConstants.STRING, String.class);
        classMap.put(SharedConstants.TOOL_CARD, ToolCard.class);
        classMap.put(SharedConstants.USER, User.class);
        classMap.put(SharedConstants.SCHEMA_CARD, SchemaCard.class);
        classMap.put(SharedConstants.COLOR, Color.class);
        classMap.put(SharedConstants.COMMAND_FLOW, CommandFlow.class);
    }

}













