package org.poianitibaldizhou.sagrada.game.model;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ClearAll;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.RemoveDiceFromDraftPool;
import org.poianitibaldizhou.sagrada.game.model.state.ResetState;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.util.List;
import java.util.Map;

public class SinglePlayerGame extends Game{

    public static final int NUMBER_OF_PUBLIC_OBJECTIVE_CARDS = 2;
    public static final int NUMBER_OF_DICES_TO_DRAW = 4;
    public static final int NUMBER_OF_PRIVATE_OBJECTIVE_CARDS = 2;

    private final int difficulty;

    /**
     * Constructor.
     * Create a single player game based on difficulty and playerToken
     * @param name the name of the game
     * @param user the single user
     * @param difficulty the difficulty of the game
     */
    public SinglePlayerGame(String name, User user, int difficulty){
        super(name);
        this.users.add(user);
        this.difficulty = difficulty;

        setState(new ResetState(this));
    }

    public int getDifficulty(){
        return difficulty;
    }

    /**
     * Return the target score for a single player game. The player score has to be higher than this to win
     *
     * @return the target score
     */
    @Contract(pure = true)
    public int getTargetScore() {
        int targetScore = 0;
        for (int i = 0; i < RoundTrack.NUMBER_OF_TRACK; i++) {
            List<Dice> dices = getRoundTrack().getDices(i);
            for (Dice dice: dices) {
                targetScore += dice.getNumber();
            }
        }
        return targetScore;
    }

    public void setPlayer(User user, SchemaCard schemaCard, List<PrivateObjectiveCard> privateObjectiveCards) {
        players.put(user.getToken(), new SinglePlayer(user, new ExpendableDice(this), schemaCard, privateObjectiveCards));
    }

    @Override
    public int getNumberOfToolCardForGame() {
        return getDifficulty();
    }

    @Override
    public int getNumberOfPublicObjectiveCardForGame() {
        return NUMBER_OF_PUBLIC_OBJECTIVE_CARDS;
    }

    @Override
    public int getNumberOfDicesToDraw() {
        return NUMBER_OF_DICES_TO_DRAW;
    }

    @Override
    public int getNumberOfPrivateObjectiveCardForGame() {
        return NUMBER_OF_PRIVATE_OBJECTIVE_CARDS;
    }

    @Override
    public void setPlayersOutcome(Map<Player, Integer> scoreMap, Player currentRoundPlayer) {
        int targetScore = getTargetScore();
        Outcome outcome = (scoreMap.get(currentRoundPlayer) > targetScore) ? Outcome.WIN : Outcome.LOSE;
        setPlayerOutcome(currentRoundPlayer, outcome);
    }

    @Override
    public boolean isSinglePlayer() {
        return true;
    }

    @Override
    public void addNewPlayer(User user, SchemaCard schemaCard, List<PrivateObjectiveCard> privateObjectiveCards) {
        setPlayer(user, schemaCard, privateObjectiveCards);
    }

    @Override
    public void notifyPlayersEndGame() {
        players.get(0).getPrivateObjectiveCards();
        //TODO notify observer
    }

    @Override
    public Node<ICommand> getCompleteCommands(ToolCard toolCard) {
        Node<ICommand> useDiceCommand = new Node<>(new RemoveDiceFromDraftPool());
        Node<ICommand> clearAll = new Node<>(new ClearAll());
        useDiceCommand.setLeftChild(clearAll);
        Node<ICommand> destroyToolCard = new Node<>((player, toolCardExecutor, turnState) -> {
            toolCard.destroyToolCard();
            return CommandFlow.MAIN;
        });
        clearAll.setLeftChild(destroyToolCard);

        Node<ICommand> coreToolCardCommands = toolCard.getCommands();
        clearAll.setLeftChild(coreToolCardCommands);
        return useDiceCommand;
    }

}
