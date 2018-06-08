package org.poianitibaldizhou.sagrada.network.protocol;

/**
 * This class is used to define all constants needed for communication protocol.
 */
public class SharedConstants {

    private SharedConstants() { }

    /**
     * JSONObject attributes.
     */
    public static final String TYPE = "type";
    public static final String BODY = "body";

    /**
     * JSONObject loadable.
     */
    public static final String COLLECTION = "collection";
    public static final String MAP = "map";
    public static final String STRING = "string";
    public static final String INTEGER = "integer";
    public static final String COLOR = "color";
    public static final String TILE = "tile";
    public static final String DICE = "dice";
    public static final String TOOL_CARD = "toolCard";
    public static final String PUBLIC_OBJECTIVE_CARD = "publicObjectiveCard";
    public static final String PRIVATE_OBJECTIVE_CARD = "privateObjectiveCard";
    public static final String SCHEMA_CARD = "schemaCard";
    public static final String DRAFT_POOL = "draftPool";
    public static final String ROUND_TRACK = "roundTrack";
    public static final String POSITION = "position";
    public static final String USER = "user";
    public static final String COMMAND_FLOW = "commandFlow";
    public static final String ELEM = "elem";
    public static final String OUTCOME = "outcome";
    public static final String BOOLEAN = "boolean";
    public static final String FRONT_BACK_SCHEMA_CARD = "frontBackSchemaCard";
    public static final String END_TURN_ACTION = "endTurnAction";
    public static final String PLACE_DICE_ACTION = "placeDiceAction";
    public static final String USE_TOOL_CARD_ACTION = "useToolCardAction";


    /**
     * Key protocol communication.
     */
    public static final String GAME_NAME_KEY = "gameNameKey";
    public static final String TOKEN_KEY = "tokenKey";
    public static final String DICE_LIST_KEY = "diceListKey";
    public static final String ELEM_LIST_KEY = "elemListKey";
    public static final String USER_LIST_KEY = "userListKey";
    public static final String PUBLIC_OBJECTIVE_CARD_LIST_KEY = "publicObjectiveCardListKey";
    public static final String TOOL_CARD_LIST_KEY = "toolCardList";
    public static final String PRIVATE_OBJECTIVE_CARD_LIST_KEY = "privateObjectiveCardListKey";
    public static final String FRONT_CARD_LIST_KEY = "frontBackSchemaCardListKey";
    public static final String USER_NAME_STRING = "usernameKey";
    public static final String TIMEOUT = "timeout";
    public static final String MAP_SCHEMA_CARD_KEY = "mapSchemaCardKey";
    public static final String ACTION_KEY = "action";
    public static final String TURN_VALUE_KEY = "turnValue";
    public static final String MAP_PLAYERS_COINS_KEY = "mapPlayersCoinsKey";
    public static final String COLOR_LIST_KEY = "colorListKey";
    public static final String DICE_VALUE_KEY = "diceValueKey";
    public static final String VICTORY_POINT_MAP_KEY = "victoryPointMapKey";
    public static final String OLD_DICE_KEY = "oldDiceKey";
    public static final String NEW_DICE_KEY = "newDiceKey";
    public static final String ROUND_USER_KEY = "roundUserKey";
    public static final String TURN_USER_KEY = "turnUserKey";

    /**
     * Error message.
     */
    public static final String GET_ERROR = "Can't get because you're not logged or you are not part of the " +
            "specified game or the specified does not exist or the requested object does not exist";
    public static final String GET_ERROR_KEY = "error";
    public static final String RECONNECT_ERROR = "Error reconnecting";
}
