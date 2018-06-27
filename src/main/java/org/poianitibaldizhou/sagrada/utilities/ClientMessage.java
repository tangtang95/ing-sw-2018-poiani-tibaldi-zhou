package org.poianitibaldizhou.sagrada.utilities;

/**
 * Class contains all client message.
 */
public final class ClientMessage {

    /**
     * private constructor.
     */
    private ClientMessage(){}

    /**
     * Graphic info massages.
     */
    public static final String THE_WINNER_IS = "Il vincitore è %s.";
    public static final String ONLY_NUMBER = "Solo numeri.";
    public static final String ONLY_CHARACTER = "Solo caratteri alfanumerici.";
    public static final String OBLIGATORY = "Obbligatorio.";
    public static final String COLOR_CHOSEN = "Hai scelto precedentemente il colore: %s.";
    public static final String DICE_CHOSEN = "Hai scelto precedentemente il dado: %s.";
    public static final String POSITION_CHOSEN = "Hai scelto precedentemente la posizione: %s.";
    public static final String ACTION_CHOSEN = "Hai scelto precedentemente di %s continuare.";
    public static final String VALUE_CHOSEN = "Hai scelto precedentemente il valore: %s.";
    public static final String TOOL_CARD_CHOSEN = "La Carta Utensile in uso è: %s, la cui descrizione: %s.";
    public static final String WAIT_FOR_USER = "In attesa degli altri giocatori.";
    public static final String CHOOSE_ACTION_ITA = "Tocca a te, scegli una delle seguenti azioni.";
    public static final String PLAYER_TURN = "%s turno del giocatore: %s.";
    public static final String PLAYER_SKIP_TURN = "E\' stato saltato il turno del giocatore: %s.";
    public static final String INSTRUCTION_MESSAGE1 = "Hai ricevuto due window pattern fronte e retro, per girarle premi sulle carte e trascinale.";
    public static final String CHOOSE_SCHEMA_CARD_ITA = "Devi scegliere una delle due Carte Schema fronte o retro.";
    public static final String INSTRUCTION_MESSAGE2 = "Prima del calcolo dei punti scegli una delle Carte Private che desideri.";
    public static final String CHOOSE_PRIVATE_OBJECTIVE_CARD_ITA = "Devi scegliere una delle Carte Private.";
    public static final String CHOOSE_TOOL_CARD_ITA = "Devi scegliere una Carta Utensile.";
    public static final String PLACE_DICE_FROM_DRAFT_POOL = "Piazza un dado della Riserva sulla tua Carta Schema rispettando le regoledi piazzamento.";
    public static final String CHOOSE_DICE_FROM_LIST = "Scegli uno dei dadi nella lista: ";
    public static final String CHOOSE_DICE_VALUE = "Scegli il nuovo valore del dado: ";
    public static final String CHOOSE_COLOR_FOR_CONTINUE = "Scegli uno dei colori indicati per la mossa successiva: ";
    public static final String CHOOSE_DICE_FROM_ROUND_TRACK = "Scegli un dado dal Tracciato dei round: ";
    public static final String REMOVE_DICE = "Rimuovi un dado dalla Carta Schema rispettando la Carta Utensile.";
    public static final String TOOL_CARD_PLACE_DICE = "Piazza il dado in una cella rispettando le restrizioni di piazzamento della Carta Utensile.";
    public static final String REMOVE_DICE_BY_COLOR = "Rimuovi un dado del color %s dalla Carta Schema rispettando la Carta Utensile.";
    public static final String WOULD_YOU_CONTINUE = "Vuoi continuare l'esecuzione della Carta Utensile?";
    public static final String TOOL_CARD_EXECUTION_SUCCESSFUL = "Carta Utensile eseguita con successo.";
    public static final String TOOL_CARD_END_TURN_ACTIVATION = "A fine turno la ToolCard si attiverà.";
    public static final String CHOOSE_DICE_ITA = "Devi scegliere un dado.";
    public static final String CHOOSE_COLOR_ITA = "Devi scegliere un colore.";
    public static final String CHOOSE_POSITION_ITA = "Devi scegliere una posizione.";
    public static final String CHOOSE_VALUE = "Devi scegliere un valore.";

    /**
     * Graphics error messages.
     */
    public static final String LOAD_FXML_ERROR = "SEVERE ERROR: Cannot load FXML loader.";
    public static final String NO_GAME_AVAILABLE = "No game available error.";
    public static final String GRAPHICS_UTILS_INSTANCE_ERROR = "Cannot instantiate GraphicsUtils.";
    public static final String FILE_NOT_FOUND = "SEVERE ERROR: File not founded.";
    public static final String PARSE_EXCEPTION = "SEVERE ERROR: Parse exception.";
    public static final String USER_ALREADY_EXIST = "Esiste già un utente con questo nome.";
    public static final String IP_ERROR = "Non è un indirizzo IP.";
    public static final String OPERATION_ERROR = "L'operazione è fallita per problemi di connessione";
    public static final String CONNECTION_ERROR = "Non è stato possibile connettersi al server.";
    public static final String SYNCHRONIZED_ERROR = "Errore di sincronismo.";
    public static final String TIMEOUT_ERROR = "E\' scaduto il tempo.";
    public static final String PLACE_DICE_ERROR = "Non puoi piazzare il dado su una cella piena.";
    public static final String NO_DICE_SELECTED = "Nessun dado selezionato.";
    public static final String COLOR_DICE_IS_WRONG = "Il dado selezionato è del colore sbagliato.";
    public static final String REPEAT_ACTION = "Hai sbagliato mossa, ripeti.";
    public static final String TOOL_CARD_INTERRUPT_EXCEPTION = "L'esecuzione della Carta Utensile è stata interrotta per errore: ";

    /**
     * CLI menu.
     */
    public static final String CHANGE_CONNECTION = "Change the connection mode";
    public static final String GO_TO_START_MENU = "Go to Start Game Menu";
    public static final String SELECT_CONNECTION_MENU = "----------------------Select Connection Menu-----------------------";
    public static final String CURRENT_CONNECTION_MODE = "Current connection mode: ";
    public static final String CHANGE_CONNECTION_OR_GO_BACK = "Change connection mode or go to Start Game Menu: ";
    public static final String QUIT_GAME = "Quit game";
    public static final String END_GAME = "-----------------------------END GAME------------------------------";
    public static final String CHOOSE_ACTION = "Choose action: ";
    public static final String VIEW_DRAFT_POOL = "View the Draft Pool";
    public static final String VIEW_ROUND_TRACK = "View the Round Track";
    public static final String VIEW_TOOL_CARDS = "View the list of Tool Card";
    public static final String VIEW_PUBLIC_OBJECTIVE_CARD = "View the public objective cards";
    public static final String VIEW_SCHEMA_CARDS = "View Schema Cards";
    public static final String VIEW_MY_SCHEMA = "View my schema Card";
    public static final String VIEW_PRIVATE_OBJECTIVE_CARD = "View the private objective cards";
    public static final String VIEW_MY_COINS = "View my coins";
    public static final String VIEW_PLAYERS_COINS = "View players coins";
    public static final String VIEW_TIME_TO_TIMEOUT = "View time to timeout";
    public static final String VIEW_DRAFT_POOL_HELP = "View the game Draft Pool";
    public static final String VIEW_ROUND_TRACK_HELP = "View the game Round Track";
    public static final String VIEW_TOOL_CARDS_HELP = "View the Tool Cards playable";
    public static final String VIEW_PUBLIC_OBJECTIVE_CARD_HELP = "View the Public Objective cards";
    public static final String VIEW_SCHEMA_CARDS_HELP = "View the Schema card of all players";
    public static final String VIEW_MY_SCHEMA_HELP = "View my Schema card";
    public static final String VIEW_PRIVATE_OBJECTIVE_CARD_HELP = "View the Private Objective cards";
    public static final String VIEW_MY_COINS_HELP = "View my expendable coins";
    public static final String VIEW_PLAYERS_COINS_HELP = "View the coins of all players";
    public static final String VIEW_TIME_TO_TIMEOUT_HELP = "View time to time out";
    public static final String QUIT_GAME_HELP = "Quit from current game";
    public static final String SINGLE_PLAYER = "Single player";
    public static final String MULTI_PLAYER = "Multi player";
    public static final String GO_BACK = "Go back";
    public static final String SINGLE_PLAYER_HELP = "Start in single player mode";
    public static final String MULTI_PLAYER_HELP = "Start in multi player mode";
    public static final String GO_BACK_HELP = "Go to Start Game Menu";
    public static final String SELECT_GAME_MODE = "------------------------Select Game Mode---------------------------";
    public static final String CHOOSE_GAME_MODE = "Choose the game mode or go to Start Game Menu: ";
    public static final String PLACE_DICE = "Place dice";
    public static final String PLAY_TOOL_CARD = "Play ToolCard";
    public static final String END_TURN = "End turn";
    public static final String PLACE_DICE_HELP = "Place a dice on Schema Card from Draft Pool";
    public static final String PLAY_TOOL_CARD_HELP = "Play a Tool Card";
    public static final String END_TURN_HELP = "End the turn";
    public static final String START_GAME = "Start game";
    public static final String RECONNECT = "Reconnect";
    public static final String CHANGE_CONNECTION_MODE_HELP = "Go to Change connection menu";
    public static final String START_GAME_HELP = "Go to Game mode menu";
    public static final String RECONNECT_HELP = "Reconnect to an on-going game";
    public static final String START_GAME_MENU = "-------------------------Start Game Menu---------------------------";
    public static final String CHANGE_CONNECTION_MODE = "Change connection mode";
    public static final String TURN_MENU = "----------------------------IS YOUR TURN---------------------------\n";
    public static final String END_TURN_MENU = "-------------------------YOUR TURN IS FINISH-----------------------\n";
    public static final String TABLE_OF_POINTS_MENU = "--------------------------TABLE OF POINTS--------------------------\n";
    public static final String LEAVE_COMMAND = "Leave";
    public static final String TIMEOUT_COMMAND = "Timeout";
    public static final String LOBBY_USER_COMMAND = "Show lobby users";
    public static final String LEAVE_COMMAND_HELP = "Leave the lobby";
    public static final String TIMEOUT_COMMAND_HELP = "Show time to reach timeout";
    public static final String LOBBY_USER_COMMAND_HELP = "Show users in lobby";
    public static final String LOBBY_MENU = "-----------------------Welcome to the Lobby------------------------\n";
    public static final String GAME_STARTED = "\"----------------------------GAME STARTED---------------------------\n";

    /**
     * CLI info massages.
     */
    public static final String HAS_RE_ROLL = " has re-rolled the draft pool.";
    public static final String PUBLIC_OBJECTIVE_CARD_VALID = "Public objective cards valid for this game: ";
    public static final String TOOL_CARD_VALID = "Tool cards valid for this game: ";
    public static final String CHOOSE_PRIVATE_OBJECTIVE_CARD = "Choose a private objective card:";
    public static final String PRIVATE_OBJECTIVE_CARD_VALID = "Private objective cards valid for this game: ";
    public static final String CHOOSE_SCHEMA_CARD = "Choose a schema card:";
    public static final String TIME_TO_TIMEOUT = "Time to timeout is: ";
    public static final String YOUR_COINS = "Your coins: ";
    public static final String SCHEMA_CARD_OF = "Schema of Player:";
    public static final String COINS_OF = "Coins of %s: %s.\n";
    public static final String TOKEN_SPENT = "%s has spent %d tokens.";
    public static final String YOUR_OUTCOME = "Your outcome is: ";
    public static final String RECONNECT_TO_YOUR_GAME = "Re-connecting to your game...\n";
    public static final String PROVIDE_AN_USERNAME = "Provide an username: \n";
    public static final String ADD_DICE_LIST_TO_ROUND = "%s added a list of dices to the round track at round %d.";
    public static final String ADD_DICE_TO_ROUND = "%s added a list of dices to the round track at round %d.";
    public static final String REMOVE_DICE_FROM_ROUND_TRACK = "%s  removed a dice from the round track at round %d.";
    public static final String SWAP_DICE = "%s  swap a with the round track at round %d.";
    public static final String OLD_DICE = "Old dice (no more present in round track) : ";
    public static final String NEW_DICE = "New dice (added to the round track) : ";
    public static final String PLACE_DICE_IN_POSITION = " has placed a dice in position ";
    public static final String REMOVE_DICE_IN_POSITION = " has removed a dice in position ";
    public static final String PROVIDE_DIFFICULTY = "Provide a difficulty ranging from 1 to 5: \n";
    public static final String YOU_LEFT_THE_GAME = "You have left the game.\n";
    public static final String GAME_SETUP = "Game setup.\n";
    public static final String PLAYER_SETUP = "Players setup.\n";
    public static final String ROUND_STARTED_WITH_PLAYER = "The round %d is started with player %s.\n";
    public static final String USER_PLAYING = "Is the round %d, %s is playing.\n";
    public static final String ROUND_END = "The round %d end.\n";
    public static final String SKIP_TURN = "The turn of player %s skip.\n";
    public static final String PLAYER_USE_TOOL_CARD = "The player %s use a toolCard.\n";
    public static final String TURN_END = "The turn of %s is ended.\n";
    public static final String WINNER_IS = "The winner is %s.\n";
    public static final String TIMEOUT = "You have spent all the time for your turn.\n";
    public static final String USER_HAS_TIMEOUT = "User %s has timeout.\n";
    public static final String CHOOSE_DICE = "Choose a dice: ";
    public static final String CHOOSE_NUMBER_BETWEEN_1_6 = "Choose a number between 1 and 6:\n";
    public static final String COLOR = "Colors: \n";
    public static final String CHOOSE_COLOR ="Choose a color: \n";
    public static final String CHOOSE_DELTA_NUMBER = "Choose the number %d or %d:\n";
    public static final String CHOOSE_THE_NUMBER = "Choose the number %d:\n";
    public static final String CHOOSE_POSITION = "Choose a position on your Schema Card.\n";
    public static final String CHOOSE_POSITION_WITH_COLOR = "Choose a position from your Schema Card with the color ";
    public static final String CONTINUE_MESSAGE = "Do you want to continue? (y/n)\n";
    public static final String RE_ROL_DICE = "The dice has been re-rolled: ";
    public static final String DICE_POURED_OVER = "The dice has been poured-over: ";
    public static final String CHOOSE_POSITION_ON_SCHEMA = "Choose a position on your Schema Card.\n";
    public static final String CHOOSE_ROW = "Choose a row between 1 and 4 included:";
    public static final String CHOOSE_COLUMN = "Choose a column between 1 and 5 included:\n";
    public static final String CHOOSE_ROUND = "Choose a round: \n";
    public static final String TOKEN_CHANGE = "Token on %s has been change to %d.";
    public static final String TOOL_CARD_DESTROY = "Tool card %s has been utilized and destroyed.";
    public static final String CHOOSE_TOOL_CARD = "Choose a Tool card: ";
    public static final String USER_JOIN_LOBBY = "User %s joined the lobby.\n";
    public static final String USER_LEFT_LOBBY = "User %s left the lobby.\n";
    public static final String USER_LEAVE_LOBBY = "You have leave the lobby.\n";

    /**
     * CLI error messages.
     */
    public static final String NOT_A_NUMBER = "Is not a number, please retry.\n";
    public static final String COMMAND_NOT_FOUND = "Command not found, please retry.\n";
    public static final String ERROR_READING = ": Error while reading from keyboard.\n";
    public static final String NETWORK_ERROR = ": Network error.\n";
    public static final String AUTOMATIC_ACTION = "The action was chosen automatically, due to the expiration of the timeout.\n";
    public static final String FATAL_ERROR = ": Fatal error. Please close the game and restart it.\n";
    public static final String NO_SCREEN_AVAILABLE = "No screen available.";
    public static final String RECONNECT_FAILED = "Re-connecting failed.";
    public static final String GAME_TERMINATION_ERROR = "The game has terminated before starting due to the fact that some players failed in joining the game";
    public static final String COMMAND_ERROR = "There was an error with the last command which will be repeated.\n";
    public static final String TOOL_CARD_ERROR = "You made an unforgivable mistake when using the Tool Card %s, so you will not be able to use it this turn.";
    public static final String ERROR_TYPE = "ERROR TYPE: ";

}
