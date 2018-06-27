package org.poianitibaldizhou.sagrada.utilities;

/**
 * Class contains all server message.
 */
public final class ServerMessage {

    /**
     * private constructor.
     */
    private ServerMessage(){}

    /**
     * gameView.err
     */
    public static final String THE_GAME_NOT_EXIST = "The game doesn't exist or you are not signaled as an entering player.";
    public static final String YOU_ARE_NOT_IN = "You are not playing in this game or it is impossible to join. Try with a reconnect.";
    public static final String SCHEMA_NOT_VALID = "The schema card selected is not valid.";
    public static final String NOT_TOOL_CARD_EXIST = "You are trying to listening on a non existing toolCard.";
    public static final String YOU_CANNOT_TAKE_ACTION = "You cannot take any action right now.";
    public static final String INVALID_PRIVATE_OBJECTIVE_CARD = "The private objective card chosen is invalid.";
    public static final String FIRE_EVENT_ERROR = "Can't fire an event now";
    public static final String INVALID_ACTION_ERR = "You can't perform this action now";
    public static final String YOU_NEED_TO_RECONNECT = "You need to reconnect";
    public static final String GAME_TERMINATED = "The game has terminated";
    public static final String NO_TOKEN_EXIST = "No user with such token exists.";
    public static final String PLAYER_ALREADY_EXIST = "A player with this name is already connected to the game.";
    public static final String OBSERVER_ARE_WRONG = "Observers are wrong.";

    /**
     * lobbyView.err
     */
    public static final String USER_ALREADY_EXIST = "An user with this username already exists.";

    /**
     * gameView.ack
     */
    public static final String YUO_ARE_READY = "You are now ready to play.";
    public static final String CORRECT_SCHEMA_CARD_SELECTION = "You have correctly selected the schema card: ";
    public static final String RECONNECT_SUCCESSFUL = "Reconnected successful.";
    public static final String HAS_RECONNECT = " has reconnected.";

    /**
     * lobbyView.ack
     */
    public static final String YUO_ARE_LOGGED = "You are now logged as: ";
    public static final String LOBBY_LEFT = "Lobby left.";
    public static final String YUO_ARE_IN_LOBBY = "You're now in the lobby.";

    /**
     * Game error message.
     */
    public static final String INVALID_DICE_NUMBER = "Invalid number: number must be in range [%d,%d].";
    public static final String DRAFT_POOL_DICE_NOT_FOUND = "DraftPool.useDice() failed due to non existence of the dice in the pool.";
    public static final String ROUND_TRACK_ILLEGAL_ARGUMENT = "Round must be in [%d,%d]. Round specified: ";
    public static final String CONSTRAINT_ARE_DIFFERENT = "constraints has different type than PlacementRestrictionType given.";
    public static final String TOOL_CARD_EXECUTOR_ILLEGAL_STATE = "SEVERE ERROR: Need to set the commands before starting the thread.";
    public static final String TOOL_CARD_EXECUTOR_INVALID_COMMAND = "Invocation of commands interrupted.";
    public static final String TURN_ILLEGAL_ARGUMENT = "Turn has to be 1 or 2.";
    public static final String LANGUAGE_PARSER_ILLEGAL_ARGUMENT1 = "This is not a command ";
    public static final String LANGUAGE_PARSER_ILLEGAL_ARGUMENT2 = "Command not recognized ";
    public static final String POSITION_ILLEGAL_ARGUMENT = "Row and column is out of bounds.";
    public static final String NO_CONSTRAINT_ILLEGAL_STATE = "SEVERE ERROR: Cannot be implemented.";
    public static final String MULTI_PLAYER_ILLEGAL_ARGUMENT = "SEVERE ERROR: player is not a MultiPlayer, do not call this method from MultiPlayer.";
    public static final String SINGLE_PLAYER_ILLEGAL_ARGUMENT = "SEVERE ERROR: player is not a single player, do not call this method directly.";
    public static final String PLAYER_ILLEGAL_ARGUMENT = "PrivateObjectiveCard doesn't exist in the player.";
    public static final String ILLEGAL_STATE_EXCEPTION = "SEVERE ERROR: method not implemented in this state.";
    public static final String EMPTY_COLLECTION_ERROR = "SEVERE ERROR: Error for empty collection.";
    public static final String CANNOT_FIND_TOKEN_ERROR = "SEVERE ERROR: cannot find token.";
    public static final String PARSE_EXCEPTION = "SEVERE ERROR: Parse exception.";
    public static final String NO_WINNER_ERROR = "SEVERE ERROR: No winners founded.";

    /**
     * Lobby error message.
     */
    public static final String AUTHORIZATION_FAILED = "Authorization failed";
    public static final String TIMEOUT_INTERRUPT = "TimeoutThread interrupted.";
    public static final String USER_HAS_ALREADY_JOIN = "User has already joined the lobby.";
    public static final String NEED_TO_LOGIN = "Need to login.";
    public static final String CAN_NOT_LEAVE = "Can't leave because user is not in the lobby.";
    public static final String USER_HAS_ALREADY_LOGGED = "User has already logged: ";
    public static final String NO_LOBBY_ACTIVE = "No lobby active.";

    /**
     * Network error message.
     */
    public static final String INVALID_ACTION = "Impossible because have to happen in this way.";
    public static final String INVOCATION_ERROR = "Invocation failed, cannot find method from all the target's method.";
    public static final String CLIENT_HANDLER_ILLEGAL_ARGUMENT = "The object passed is not a Response or a NotifyMessage.";
    public static final String NOT_FIND_RMI_REGISTRY = "Cannot find RMI registry.";
    public static final String NOT_FIND_RMI_CONTROLLER = "Cannot find RMI controller.";
    public static final String NETWORK_UNDEFINED = "Network type undefined.";
    public static final String SOCKET_CLOSE = "Socket closed";

}
