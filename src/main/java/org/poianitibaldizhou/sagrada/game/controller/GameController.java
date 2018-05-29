package org.poianitibaldizhou.sagrada.game.controller;

import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ColorExecutorEvent;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.DiceExecutorEvent;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.PositionExecutorEvent;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ValueExecutorEvent;
import org.poianitibaldizhou.sagrada.game.model.observers.ObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.*;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.*;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;
import org.poianitibaldizhou.sagrada.game.view.IGameView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class GameController extends UnicastRemoteObject implements IGameController {

    private final transient HashMap<String, IGameView> viewMap = new HashMap<>();
    private final transient GameManager gameManager;

    public GameController(GameManager gameManager) throws RemoteException {
        super();
        this.gameManager = gameManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void joinGame(final String token, final String gameName, IGameView view, IGameObserver gameObserver,
                         IRoundTrackObserver roundTrackObserver, IStateObserver stateObserver,
                         IDraftPoolObserver draftPoolObserver, IDrawableCollectionObserver<Dice> diceBagObserver) throws RemoteException {
        if (!gameManager.containsGame(gameName))
            view.err("The game doesn't exist");
        IGame game = gameManager.getGameByName(gameName);
        try {
            game.userJoin(token);
        } catch (InvalidActionException e) {
            view.err("You are not playing in this game");
            return;
        }
        view.ack("You are now ready to play");

        ObserverManager observerManager = gameManager.getObserverManagerByGame(gameName);
        game.attachGameObserver(token, new GameFakeObserver(token, gameObserver, observerManager));
        game.attachRoundTrackObserver(token, new RoundTrackFakeObserver(token, roundTrackObserver, observerManager));
        game.attachStateObserver(token, new StateFakeObserver(token, observerManager, stateObserver));
        game.attachDraftPoolObserver(token, new DraftPoolFakeObserver(token, draftPoolObserver, observerManager));
        game.attachDiceBagObserver(token, new DrawableCollectionFakeObserver<>(token, diceBagObserver, observerManager));

        viewMap.put(token, view);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void chooseSchemaCard(String token, String gameName, SchemaCard schemaCard) throws RemoteException {
        if (!viewMap.containsKey(token))
            return;
        if (!gameManager.containsGame(gameName))
            viewMap.get(token).err("The game doesn't exist");
        IGame game = gameManager.getGameByName(gameName);
        try {
            game.userSelectSchemaCard(token, schemaCard);
        } catch (InvalidActionException e) {
            if (viewMap.containsKey(token)) {
                viewMap.get(token).err("The schema card selected is not valid");
            }
            return;
        }
        viewMap.get(token).ack("You have correctly selected the schema card: " + schemaCard.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindPlayer(String token, String gameName, Player player, IPlayerObserver playerObserver, ISchemaCardObserver schemaCardObserver) throws RemoteException {
        if (!viewMap.containsKey(token))
            return;
        if (!gameManager.containsGame(gameName))
            viewMap.get(token).err("The game doesn't exist");
        IGame game = gameManager.getGameByName(gameName);

        if(!game.getPlayers().contains(player)) {
            viewMap.get(token).err("You are trying to listening the actions of an non existing player");
            return;
        }

        game.attachSchemaCardObserver(token, player.getSchemaCard(), new SchemaCardFakeObserver(token,
                gameManager.getObserverManagerByGame(gameName), schemaCardObserver));
        game.attachPlayerObserver(token, player, new PlayerFakeObserver(token, gameManager.getObserverManagerByGame(gameName), playerObserver));

        viewMap.get(token).ack("Binding to " + player.getUser().getName() + " successful");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindToolCard(String token, String gameName, ToolCard toolCard, IToolCardObserver toolCardObserver) throws RemoteException {
        if (!viewMap.containsKey(token))
            return;
        if (!gameManager.containsGame(gameName))
            viewMap.get(token).err("The game doesn't exist");
        IGame game = gameManager.getGameByName(gameName);

        if(!game.getToolCards().contains(toolCard)) {
            viewMap.get(token).err("You are trying to listening on a non existing toolcard");
            return;
        }

        game.attachToolCardObserver(token, toolCard, new ToolCardFakeObserver(token, gameManager.getObserverManagerByGame(gameName), toolCardObserver));

        viewMap.get(token).ack("Binding to " + toolCard.getName() + " successful");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void chooseAction(String token, String gameName, IActionCommand actionCommand) throws RemoteException {
        if (!viewMap.containsKey(token))
            return;
        if (!gameManager.containsGame(gameName))
            viewMap.get(token).err("The game doesn't exist");
        IGame game = gameManager.getGameByName(gameName);
        try {
            game.userChooseAction(token, actionCommand);
        } catch (InvalidActionException e) {
            viewMap.get(token).err("You cannot take any action right now");
        }
        viewMap.get(token).ack("Action performed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void placeDice(String token, String gameName, Dice dice, Position position) throws RemoteException {
        if (!viewMap.containsKey(token))
            return;
        if (!gameManager.containsGame(gameName))
            viewMap.get(token).err("The game doesn't exist");
        IGame game = gameManager.getGameByName(gameName);
        try {
            game.userPlaceDice(token, dice, position);
        } catch (InvalidActionException e) {
            if (e.getException() instanceof RuleViolationException)
                handleRuleViolationException(viewMap.get(token), (RuleViolationException) e.getException());
            else
                viewMap.get(token).err("You cannot take any action right now");
        }
    }

    /**
     * {@inheritDoc}
     */
    private void handleRuleViolationException(IGameView view, RuleViolationException exception) throws RemoteException {
        switch (exception.getViolationType()) {
            case NO_DICE_NEAR:
                view.err("You cannot place dice because there are no dice near");
                break;
            case SIMILAR_DICE_NEAR:
                view.err("You cannot place dice because there is a similar dice near");
                break;
            case TILE_FILLED:
                view.err("You cannot place dice because there is already a dice on it");
                break;
            case TILE_UNMATCHED:
                view.err("You cannot place dice on this tile");
                break;
            case HAS_DICE_NEAR:
                view.err("You cannot place dice because there is at least one dice near");
                break;
            case NO_VIOLATION:
                view.err("You cannot take any action right now");
                break;
            case NOT_BORDER_TILE:
                view.err("You have to place a dice around the border of the schema card");
                break;
            default:
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useToolCard(String token, String gameName, ToolCard toolCard, IToolCardExecutorObserver executorObserver)
            throws RemoteException {
        if (!viewMap.containsKey(token))
            return;
        if (!gameManager.containsGame(gameName))
            viewMap.get(token).err("The game doesn't exist");
        IGame game = gameManager.getGameByName(gameName);
        try {
            game.userUseToolCard(token, toolCard, new ToolCardExecutorFakeObserver(token, gameManager.getObserverManagerByGame(gameName), executorObserver));
        } catch (InvalidActionException e) {
            viewMap.get(token).err("You cannot take any action right now");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void choosePrivateObjectiveCard(String token, String gameName, PrivateObjectiveCard privateObjectiveCard) throws RemoteException {
        if (!viewMap.containsKey(token))
            return;
        if (!gameManager.containsGame(gameName))
            viewMap.get(token).err("The game doesn't exist");
        IGame game = gameManager.getGameByName(gameName);
        try {
            game.userChoosePrivateObjectiveCard(token, privateObjectiveCard);
        } catch (InvalidActionException e) {
            viewMap.get(token).err("The private objective card chosen is invalid");
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setDice(String token, String gameName, Dice dice, String toolCardName) throws RemoteException {
        if (!viewMap.containsKey(token))
            return;
        if (!gameManager.containsGame(gameName))
            viewMap.get(token).err("The game doesn't exist");
        IGame game = gameManager.getGameByName(gameName);

        try {
            game.userFireExecutorEvent(token, new DiceExecutorEvent(dice));
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNewValue(String token, String gameName, int value, String toolCardName) throws RemoteException {
        if (!viewMap.containsKey(token) || !gameManager.containsGame(gameName))
            return;
        IGame game = gameManager.getGameByName(gameName);
        try {
            game.userFireExecutorEvent(token, new ValueExecutorEvent(value));
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setColor(String token, String gameName, Color color, String toolCardName) throws RemoteException {
        if (!viewMap.containsKey(token) || !gameManager.containsGame(gameName) || !gameManager.getGameByName(gameName).getPlayers().contains(token))
            return;
        IGame game = gameManager.getGameByName(gameName);
        try {
            game.userFireExecutorEvent(token, new ColorExecutorEvent(color));
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPosition(String token, String gameName, Position position, String toolCardName) throws RemoteException {
        if (!viewMap.containsKey(token) || !gameManager.containsGame(gameName) || !gameManager.getGameByName(gameName).getPlayers().contains(token))
            return;
        IGame game = gameManager.getGameByName(gameName);
        try {
            game.userFireExecutorEvent(token, new PositionExecutorEvent(position));
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reconnect(String token, String gameName, IGameView gameView, IStateObserver stateObserver, Map<String, IPlayerObserver> playerObserver,
                          Map<String, IToolCardObserver> toolCardObserver, Map<String, ISchemaCardObserver> schemaCardObserver, IGameObserver gameObserver,
                          IDraftPoolObserver draftPoolObserver, IRoundTrackObserver roundTrackObserver, IDrawableCollectionObserver<Dice>
                                  diceBagObserver) throws RemoteException {
        // check if the token is the one of a disconnected player
        ObserverManager observerManager = gameManager.getObserverManagerByGame(gameName);
        if (!observerManager.getDisconnectedPlayer().contains(token)) {
            gameView.err("A player with this name is already connected to the game");
        }

        // check if given data are corrects
        IGame game = gameManager.getGameByName(gameName);
        if (!(game.getPlayers().containsAll(playerObserver.keySet()) && playerObserver.size() == game.getPlayers().size())) {
            gameView.err("Player observers are wrong");
            return;
        }
        if (!(game.getPlayers().containsAll(schemaCardObserver.keySet()) && game.getPlayers().size() == schemaCardObserver.size())) {
            gameView.err("Schema card observers are wrong");
            return;
        }
        if (!(game.getToolCards().containsAll(toolCardObserver.keySet()) && game.getToolCards().size() == toolCardObserver.size())) {
            gameView.err("Tool card observers are wrong");
            return;
        }

        // Attaching observer and view regarding the re-connected player
        if (viewMap.containsKey(token)) {
            viewMap.replace(token, gameView);
        }

        game.getPlayers().forEach(player -> game.attachPlayerObserver(token, player, new PlayerFakeObserver(token, observerManager, playerObserver.get(player.getToken()))));
        game.getPlayers().forEach(player -> game.attachSchemaCardObserver(token, player.getSchemaCard(),
                new SchemaCardFakeObserver(token, observerManager, schemaCardObserver.get(player.getToken()))));
        game.getToolCards().forEach(toolCard -> game.attachToolCardObserver(token, toolCard, new ToolCardFakeObserver(token, observerManager, toolCardObserver.get(toolCard.getName()))));
        game.attachDiceBagObserver(token, new DrawableCollectionFakeObserver<>(token, diceBagObserver, observerManager));
        game.attachDraftPoolObserver(token, new DraftPoolFakeObserver(token, draftPoolObserver, observerManager));
        game.attachGameObserver(token, new GameFakeObserver(token, gameObserver, observerManager));
        game.attachRoundTrackObserver(token, new RoundTrackFakeObserver(token, roundTrackObserver, observerManager));
        game.attachStateObserver(token, new StateFakeObserver(token, observerManager, stateObserver));

        // synchronize data
        synchronizeModel(token, gameName);

        observerManager.signalReconnect(token);
        gameView.ack("You are now riconnected to the game");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void synchronizeModel(String token, String gameName) throws RemoteException {
        IGame game = gameManager.getGameByName(gameName);
        if(!(game.containsToken(token))) {
            viewMap.get(token).err("You can't synchronize on a game that you're not playing");
            return;
        }

        viewMap.get(token).notifyModelSynch(game.getDraftPool(), game.getPlayers(), game.getRoundTrack(), game.getToolCards());
    }


    /**
     * Returns true if a certain player has the synchronized model
     *
     * @param token player's token
     * @param gameName game's name
     * @return true if synchronized, false otherwise
     */
    private boolean isSynchronized(String token, String gameName) {
        return gameManager.getObserverManagerByGame(gameName).getDisconnectedPlayer().contains(token);
    }
}
