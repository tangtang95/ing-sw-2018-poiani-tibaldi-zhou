package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.GameInjector;
import org.poianitibaldizhou.sagrada.game.model.cards.Card;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;
import org.poianitibaldizhou.sagrada.network.protocol.JSONable;
import org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces.IToolCardFakeObserver;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * OVERVIEW: Each instance of ToolCard has always tokens >= 0
 */
public class ToolCard extends Card implements JSONable {

    private final Color color;
    private int tokens;
    private final Node<ICommand> commands;
    private final Map<String, IToolCardFakeObserver> observerMap;

    private static final int LOW_COST = 1;
    private static final int HIGH_COST = 2;

    /**
     * ToolCard param for network protocol.
     */
    private static final String JSON_COST = "cost";
    private static final String JSON_TOKENS = "token";

    /**
     * Constructor.
     * Create a toolCard based on a color, a name, a description and an action (string of commands based on a specific
     * language)
     *
     * @param color       the color of the toolCard
     * @param name        the name of the toolCard
     * @param description the description of the toolCard
     * @param action      string of commands based on a specific language
     */
    public ToolCard(Color color, String name, String description, String action) {
        super(name, description);
        this.tokens = 0;
        this.color = color;
        ToolCardLanguageParser toolCardLanguageParser = new ToolCardLanguageParser();
        commands = toolCardLanguageParser.parseToolCard(action);
        observerMap = new HashMap<>();
    }

    /**
     * copy-constructor
     *
     * @param toolCard the toolCard to copy
     */
    private ToolCard(ToolCard toolCard) {
        super(toolCard.getName(), toolCard.getDescription());
        this.color = toolCard.getColor();
        this.tokens = toolCard.getTokens();
        this.commands = toolCard.getCommands();
        this.observerMap = toolCard.getObserverMap();
    }

    //GETTER

    /**
     * Returns the observers that listen to the modification of this.
     *
     * @return map of string and fake tool card observer: represents the observers listening this
     */
    @Contract(pure = true)
    public Map<String, IToolCardFakeObserver> getObserverMap() {
        return new HashMap<>(observerMap);
    }

    /**
     * Returns the number of tokens of the tool card
     *
     * @return number of tokens of the tool card
     */
    @Contract(pure = true)
    public int getTokens() {
        return tokens;
    }

    /**
     * Returns the color of the tool card
     *
     * @return color of the tool card
     */
    @Contract(pure = true)
    public Color getColor() {
        return color;
    }

    /**
     * Return the cost of the tool card.
     *
     * @return cost of the tool card
     */
    @Contract(pure = true)
    public int getCost() {
        return (tokens == 0) ? LOW_COST : HIGH_COST;
    }

    /**
     * Returns the tree of commands of the tool card
     *
     * @return tree of commands of the tool card
     */
    public Node<ICommand> getCommands() {
        return commands;
    }

    //MODIFIERS

    /**
     * Adds a certain number of tokens on the tool card
     *
     * @param tokens tokens that need to be added on this
     */
    public void addTokens(final int tokens) {
        this.tokens += tokens;
        observerMap.forEach((key, value) -> value.onTokenChange(tokens));
    }

    /**
     * Notify that the tool card is destroyed
     */
    public void destroyToolCard() {
        observerMap.forEach((key, value) -> value.onCardDestroy());
    }

    /**
     * Attach a tool card fake observer to this.
     *
     * @param token    token that identify the observers
     * @param observer tool card observer that need to be attached
     */
    public void attachToolCardObserver(String token, IToolCardFakeObserver observer) {
        observerMap.put(token, observer);
    }

    /**
     * Detach a tool card observer from this.
     *
     * @param token identifies the observer that needs to be removed
     */
    public void detachToolCardObserver(String token) {
        observerMap.remove(token);
    }

    /**
     * Creates a new instance of the tool card
     *
     * @param toolCard toolCard to create a copy.
     * @return a new instance of a toolCard passed.
     */
    public static ToolCard newInstance(@NotNull ToolCard toolCard) {
        return new ToolCard(toolCard);
    }

    /**
     * Convert a toolCard in a JSONObject.
     *
     * @return a JSONObject.
     */
    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject main = new JSONObject();
        JSONObject toolCardJSON = new JSONObject();
        toolCardJSON.put(JSON_NAME, this.getName());
        toolCardJSON.put(JSON_DESCRIPTION, this.getDescription());
        toolCardJSON.put(JSON_COLOR, this.getColor().name());
        toolCardJSON.put(JSON_COST, this.getCost());
        toolCardJSON.put(JSON_TOKENS, this.getTokens());
        main.put(SharedConstants.TYPE, SharedConstants.TOOL_CARD);
        main.put(SharedConstants.BODY, toolCardJSON);
        return main;
    }

    /**
     * Convert a json string in a toolCard object.
     *
     * @param jsonObject a JSONObject that contains a name of the toolCard.
     * @return a ToolCard object or null if the jsonObject is wrong.
     */
    public static ToolCard toObject(JSONObject jsonObject) {
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray;
        ToolCard card = null;

        try {
            jsonArray = (JSONArray) jsonParser.parse(new FileReader(GameInjector.FILE_PATH_1));
            for (Object object : Objects.requireNonNull(jsonArray)) {
                JSONObject toolCard = (JSONObject) object;
                if (toolCard.get(GameInjector.CARD_NAME).toString().equals(jsonObject.get(JSON_NAME).toString())) {
                    card = new ToolCard(Color.valueOf((String) toolCard.get(GameInjector.CARD_COLOUR)),
                            (String) toolCard.get(GameInjector.CARD_NAME),
                            (String) toolCard.get(GameInjector.CARD_DESCRIPTION),
                            (String) toolCard.get(GameInjector.CARD_ACTION));
                    card.tokens = Integer.parseInt(jsonObject.get(JSON_TOKENS).toString());
                    break;
                }
            }
        } catch (IOException | ParseException | NumberFormatException e) {
            return null;
        }
        return card;
    }

    /**
     * @param o the other object to compare
     * @return true if the toolCard has the same tokens, color, name, description and commands.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ToolCard toolCard = (ToolCard) o;
        return tokens == toolCard.tokens &&
                color == toolCard.color &&
                this.commands.equals(toolCard.getCommands()) &&
                this.getName().equals(toolCard.getName()) &&
                this.getDescription().equals(toolCard.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), color, tokens, commands);
    }
}
