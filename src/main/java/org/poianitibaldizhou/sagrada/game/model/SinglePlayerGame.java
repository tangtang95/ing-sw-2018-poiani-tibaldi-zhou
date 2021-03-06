package org.poianitibaldizhou.sagrada.game.model;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.Node;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ClearAll;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.PayDice;
import org.poianitibaldizhou.sagrada.game.model.coin.ExpendableDice;
import org.poianitibaldizhou.sagrada.game.model.players.Outcome;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.players.SinglePlayer;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.util.List;
import java.util.Map;

/**
 * OVERVIEW: Single player game for Sagrada
 */
public class SinglePlayerGame extends Game {

    public static final int NUMBER_OF_PUBLIC_OBJECTIVE_CARDS = 2;
    public static final int NUMBER_OF_DICES_TO_DRAW = 4;
    public static final int NUMBER_OF_PRIVATE_OBJECTIVE_CARDS = 2;

    private final int difficulty;

    /**
     * Constructor.
     * Create a single player game based on difficulty and playerToken
     *
     * @param gameName   the name of the game
     * @param user       the single user
     * @param difficulty the difficulty of the game
     */
    public SinglePlayerGame(String gameName, User user, int difficulty, TerminationGameManager terminationGameManager) {
        super(gameName, terminationGameManager);
        this.users.add(user);
        this.difficulty = difficulty;
    }

    /**
     * @return the difficulty of the single player game
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Return the target score for a single player game. The player score has to be higher than this to win
     * The target is core is equals to the sum of all the values of the dices present in the round track
     *
     * @return the target score
     */
    @Contract(pure = true)
    public int getTargetScore() {
        int targetScore = 0;
        for (int i = 0; i < RoundTrack.NUMBER_OF_TRACK; i++) {
            List<Dice> dices = getRoundTrack().getDices(i);
            for (Dice dice : dices) {
                targetScore += dice.getNumber();
            }
        }
        return targetScore;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfToolCardForGame() {
        return getDifficulty();
        }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfPublicObjectiveCardForGame() {
        return NUMBER_OF_PUBLIC_OBJECTIVE_CARDS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfDicesToDraw() {
        return NUMBER_OF_DICES_TO_DRAW;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfPrivateObjectiveCardForGame() {
        return NUMBER_OF_PRIVATE_OBJECTIVE_CARDS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPlayersOutcome(Map<Player, Integer> scoreMap, Player currentRoundPlayer) {
        int targetScore = getTargetScore();
        Outcome outcome = (scoreMap.get(currentRoundPlayer) > targetScore) ? Outcome.WIN : Outcome.LOSE;
        setPlayerOutcome(currentRoundPlayer, outcome);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSinglePlayer() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void forceGameTermination(Player winner) {
        throw new IllegalStateException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNewPlayer(User user, SchemaCard schemaCard, List<PrivateObjectiveCard> privateObjectiveCards) {
        players.put(user.getToken(), new SinglePlayer(user, new ExpendableDice(this), schemaCard, privateObjectiveCards));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleEndGame() {
        for (Player player : players.values()) {
            getGameObservers().get(player.getToken()).onChoosePrivateObjectiveCards(player.getPrivateObjectiveCards());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node<ICommand> getPreCommands(ToolCard toolCard) {
        Node<ICommand> useDiceCommand = new Node<>(new PayDice(toolCard.getColor()));
        Node<ICommand> destroyToolCard = new Node<>((player, toolCardExecutor, turnState) -> {
            toolCard.destroyToolCard();
            turnState.removeToolCard(toolCard);
            return CommandFlow.MAIN;
        });

        Node<ICommand> clearAll = new Node<>(new ClearAll());
        useDiceCommand.setLeftChild(destroyToolCard);
        destroyToolCard.setLeftChild(clearAll);
        return useDiceCommand;
    }
}
