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
    public static final String CHOOSE_TOOL_CARD = "Devi scegliere una Carta Utensile.";
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
    public static final String CHOOSE_DICE = "Devi scegliere un dado.";
    public static final String CHOOSE_COLOR = "Devi scegliere un colore.";
    public static final String CHOOSE_POSITION = "Devi scegliere una posizione.";
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

}