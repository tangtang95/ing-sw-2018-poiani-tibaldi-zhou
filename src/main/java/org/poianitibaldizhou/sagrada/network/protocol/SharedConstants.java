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
    public static final String OLD_DICE = "oldDice";
    public static final String NEW_DICE = "newDice";
    public static final String ROUND_USER = "roundUser";
    public static final String TURN_USER = "turnUser";
    public static final String BOOLEAN = "boolean";
    public static final String VICTORY_POINT_MAP = "victoryPointMap";
    public static final String COLOR_LIST_KEY = "colorListKey";
    public static final String DICE_VALUE = "diceValue";
    public static final String FRONT_BACK_SCHEMA_CARD = "frontBackSchemaCard";

    /**
     * Key protocol communication
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

    public static final String GET_ERROR = "Can't get because you're not logged or you are not part of the " +
            "specified game or the specified does not exist or the requested object does not exist";
    public static final String GET_ERROR_KEY = "error";
}
