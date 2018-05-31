package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.cards.Card;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.IToolCardFakeObserver;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * OVERVIEW: Each instance of ToolCard has always tokens >= 0
 */
public class ToolCard extends Card implements JSONable{


    private final Color color;
    private int tokens;
    private final transient Node<ICommand> commands;
    private final transient Map<String, IToolCardFakeObserver> observerMap;

    private static final int LOW_COST = 1;
    private static final int HIGH_COST = 2;

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
        //TODO refactor
        super(toolCard.getName(), toolCard.getDescription());
        this.color = toolCard.getColor();
        this.tokens = toolCard.getTokens();
        this.commands = toolCard.getCommands();
        this.observerMap = toolCard.getObserverMap();
    }

    //GETTER
    @Contract(pure = true)
    public Map<String, IToolCardFakeObserver> getObserverMap() {
        return new HashMap<>(observerMap);
    }

    @Contract(pure = true)
    public int getTokens() {
        return tokens;
    }

    @Contract(pure = true)
    public Color getColor() {
        return color;
    }

    @Contract(pure = true)
    public int getCost() {
        return (tokens == 0) ? LOW_COST : HIGH_COST;
    }

    public Node<ICommand> getCommands() {
        // TODO deep copy (the best option)
        return commands;
    }

    //MODIFIERS
    public void addTokens(final int tokens) {
        this.tokens += tokens;
        observerMap.forEach((key, value) -> value.onTokenChange(tokens));
    }

    public void destroyToolCard() {
        observerMap.entrySet().forEach(obs -> obs.getValue().onCardDestroy());
    }

    public void attachToolCardObserver(String token, IToolCardFakeObserver observer) {
        observerMap.put(token, observer);
    }

    public void detachToolCardObserver(String token) {
        observerMap.remove(token);
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

    public static ToolCard newInstance(@NotNull ToolCard toolCard) {
        return new ToolCard(toolCard);
    }

    public static ToolCard newInstance() {
        return null;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject toolCardJSON = new JSONObject();
        toolCardJSON.put("name", this.getName());
        toolCardJSON.put("description", this.getDescription());
        toolCardJSON.put("color", this.getColor().toString());
        toolCardJSON.put("cost", this.getCost());
        toolCardJSON.put("tokens", this.getTokens());
        return toolCardJSON;
    }

    @Override
    public Object toObject(JSONObject jsonObject) {
        return null;
    }
}
