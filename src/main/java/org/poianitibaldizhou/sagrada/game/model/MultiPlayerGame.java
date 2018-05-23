package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ClearAll;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.RemoveFavorToken;
import org.poianitibaldizhou.sagrada.game.model.coin.FavorToken;
import org.poianitibaldizhou.sagrada.game.model.state.ResetState;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.stream.Collectors;

public class MultiPlayerGame extends Game{

    public static final int NUMBER_OF_TOOL_CARDS = 3;
    public static final int NUMBER_OF_PUBLIC_OBJECTIVE_CARDS = 3;
    public static final int NUMBER_OF_PRIVATE_OBJECTIVE_CARDS = 1;

    /**
     * Constructor for Multi player.
     * Create the Game with all the attributes initialized, create also all the player from the given users and
     * set the state to SetupPlayerState
     *
     */
    public MultiPlayerGame(String name, List<User> users) throws RemoteException {
        super(name);
        this.users.addAll(users);

        setState(new ResetState(this));
    }

    @Override
    public int getNumberOfToolCardForGame() {
        return NUMBER_OF_TOOL_CARDS;
    }

    @Override
    public int getNumberOfPublicObjectiveCardForGame() {
        return NUMBER_OF_PUBLIC_OBJECTIVE_CARDS;
    }

    @Override
    public int getNumberOfDicesToDraw() {
        return this.getNumberOfPlayers()*2 + 1;
    }

    @Override
    public int getNumberOfPrivateObjectiveCardForGame() {
        return NUMBER_OF_PRIVATE_OBJECTIVE_CARDS;
    }


    /**
     * Set the Outcome of each player; first of all it found the winner by VictoryPoints, by PrivateCard points, by
     * FavorTokens and at the end by reverse order of the current player (who has the diceBag at the last round)
     *  @param scoreMap the score of each player
     * @param currentRoundPlayer the current player of the last round
     */
    @Override
    public void setPlayersOutcome(Map<Player, Integer> scoreMap, Player currentRoundPlayer) {
        List<Player> listOfPlayer = new ArrayList<>(players.values());
        List<Player> winners = getWinnersByVictoryPoints(listOfPlayer, scoreMap);
        winners = getWinnersByPrivateCard(winners);
        winners = getWinnersByFavorTokens(winners);
        Player winner = getWinnersByReverseOrder(winners, currentRoundPlayer);
        winner.setOutcome(Outcome.WIN);
        for (Player other : listOfPlayer) {
            if (!other.equals(winner))
                other.setOutcome(Outcome.LOSE);
        }
    }

    @Override
    public boolean isSinglePlayer() {
        return false;
    }

    @Override
    public void addNewPlayer(User user, SchemaCard schemaCard, List<PrivateObjectiveCard> privateObjectiveCards) {
        players.put(user.getToken(), new MultiPlayer(user, new FavorToken(schemaCard.getDifficulty()), schemaCard, privateObjectiveCards));
    }

    @Override
    public void handleEndGame() throws RemoteException {
        getState().calculateVictoryPoints();
    }

    @Override
    public Node<ICommand> getPreCommands(ToolCard toolCard) {
        Node<ICommand> removeFavorToken = new Node<>(new RemoveFavorToken(toolCard.getCost()));
        Node<ICommand> addTokenToolCard = new Node<>((player, toolCardExecutor, turnState) -> {
            toolCard.addTokens(toolCard.getCost());
            return CommandFlow.MAIN;
        });
        Node<ICommand> preCommands = new Node<>(new ClearAll());

        removeFavorToken.setLeftChild(addTokenToolCard);
        addTokenToolCard.setLeftChild(preCommands);
        return removeFavorToken;
    }

    /**
     * Return the list of the winners by VictoryPoints
     *
     * @param players all the players of the game
     * @param scoreMap the score of each player
     * @return the list of winners by VictoryPoints
     */
    private List<Player> getWinnersByVictoryPoints(List<Player> players, Map<Player, Integer> scoreMap) {
        List<Player> victoryPointWinners = new ArrayList<>();
        int maxVictoryPoints = Integer.MIN_VALUE;
        for (Player player : players) {
            maxVictoryPoints = Math.max(maxVictoryPoints, scoreMap.get(player));
        }
        for (Player player : players) {
            if (scoreMap.get(player) == maxVictoryPoints)
                victoryPointWinners.add(player);
        }
        return victoryPointWinners;
    }

    /**
     * Return the list of winners by PrivateObjectiveCard points based on the list of winners given
     *
     * @param winners the list of player who has won by VictoryPoints
     * @return the list of winners by PrivateObjectiveCard points
     */
    private List<Player> getWinnersByPrivateCard(List<Player> winners) {
        if (winners.size() == 1)
            return winners;
        List<Player> privateCardWinners = new ArrayList<>();
        int maxPrivateCardPoints = Integer.MIN_VALUE;
        for (Player player : winners) {
            maxPrivateCardPoints = Math.max(maxPrivateCardPoints, player.getScoreFromPrivateCard());
        }
        for (Player player : winners) {
            if (maxPrivateCardPoints == player.getScoreFromPrivateCard())
                privateCardWinners.add(player);
        }
        return privateCardWinners;
    }

    /**
     * Return the list of winners by FavorTokens (who has most tokens)
     *
     * @param winners the list of players who has won by PrivateObjectiveCards
     * @return the list of winners by FavorTokens
     */
    private List<Player> getWinnersByFavorTokens(List<Player> winners) {
        if (winners.size() == 1)
            return winners;
        OptionalInt maxFavorTokens = winners.stream().mapToInt(Player::getCoins).max();
        if(!maxFavorTokens.isPresent())
            throw new IllegalStateException();
        return winners.stream()
                .filter(player -> maxFavorTokens.getAsInt() == player.getCoins()).collect(Collectors.toList());
    }

    /**
     * Return the only winner by reverse order based on the current player who has the diceBag
     *
     * @param winners              the list of winners after FavorTokens
     * @param currentRoundPlayer the current player who has the diceBag
     * @return the only winner by reverse order
     */
    private Player getWinnersByReverseOrder(List<Player> winners, Player currentRoundPlayer) {
        if (winners.size() == 1)
            return winners.get(0);
        Player nextPlayer = getNextPlayer(currentRoundPlayer, Direction.COUNTER_CLOCKWISE);
        while (nextPlayer != currentRoundPlayer) {
            if (winners.contains(nextPlayer))
                return nextPlayer;
            nextPlayer = getNextPlayer(currentRoundPlayer, Direction.COUNTER_CLOCKWISE);
        }
        if (winners.contains(currentRoundPlayer))
            return currentRoundPlayer;
        throw new IllegalStateException("SEVERE ERROR: No winners founded");
    }

}
