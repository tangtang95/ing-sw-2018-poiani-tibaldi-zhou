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
    public static final String PLAYER = "player";
    public static final String USER = "user";
    public static final String COMMAND_FLOW = "commandFlow";
    public static final String ELEM = "elem";
    public static final String OUTCOME = "outcome";
    public static final String OLD_DICE = "oldDice";
    public static final String NEW_DICE = "newDice";
    public static final String ROUND_USER = "rounduser";
    public static final String TURN_USER = "turnuser";
    public static final String BOOLEAN = "boolean";
    public static final String VICTORY_POINT_MAP = "victorypointmap";
    public static final String COLOR_LIST_KEY = "ColorListKey";
    public static final String DICE_VALUE = "Dice value";

    /**
     * Key protocol communication
     */
    public static final String GAME_NAME_KEY = "GameNameKey";
    public static final String TOKEN_KEY = "TokenKey";
    public static final String DICE_LIST_KEY = "DiceListKey";
    public static final String ELEM_LIST_KEY = "ElemListKey";
    public static final String USER_LIST_KEY = "UserListKey";
    public static final String PUBLIC_OBJECTIVE_CARD_LIST_KEY = "PublicObjectiveCardListKey";
    public static final String TOOL_CARD_LIST_KEY = "ToolCardList";
    public static final String PRIVATE_OBJECTIVE_CARD_LIST_KEY = "PrivateObjectiveCardListKey";
    public static final String FRONT_CARD_LIST_KEY = "FrontBackSchemaCardListKey";

    public static final transient String GET_ERROR = "Can't get because you're not logged or you are not part of the " +
            "specified game or the specified does not exist or the requested object does not exist";
    public static final transient String GET_ERROR_KEY = "error";
}
