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
import org.poianitibaldizhou.sagrada.game.model.observers.*;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;
import org.poianitibaldizhou.sagrada.game.view.IGameView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

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
        game.attachGameObserver(token, gameObserver);
        game.attachRoundTrackObserver(token, roundTrackObserver);
        game.attachStateObserver(token, stateObserver);
        game.attachDraftPoolObserver(token, draftPoolObserver);
        game.attachDiceBagObserver(token, diceBagObserver);
        viewMap.put(token, view);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void chooseSchemaCard(String token, String gameName, SchemaCard schemaCard) throws RemoteException {
        if (!viewMap.containsKey(token))
            return;
        if(!gameManager.containsGame(gameName))
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
        if(!gameManager.containsGame(gameName))
            viewMap.get(token).err("The game doesn't exist");
        IGame game = gameManager.getGameByName(gameName);
        try {
            game.attachSchemaCardObserver(token, player.getSchemaCard(), schemaCardObserver);
        } catch (InvalidActionException e) {
            viewMap.get(token).err("The schema card selected is not valid");
            return;
        }
        try {
            game.attachPlayerObserver(token, player, playerObserver);
        } catch (InvalidActionException e) {
            viewMap.get(token).err("The schema card selected is not valid");
            return;
        }
        viewMap.get(token).ack("Binding to " + player.getUser().getName() + " successful");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindToolCard(String token, String gameName, ToolCard toolCard, IToolCardObserver toolCardObserver) throws RemoteException {
        if (!viewMap.containsKey(token))
            return;
        if(!gameManager.containsGame(gameName))
            viewMap.get(token).err("The game doesn't exist");
        IGame game = gameManager.getGameByName(gameName);
        try {
            game.attachToolCardObserver(token, toolCard, toolCardObserver);
        } catch (InvalidActionException e) {
            viewMap.get(token).err("The tool card selected is not valid");
            return;
        }
        viewMap.get(token).ack("Binding to " + toolCard.getName() + " successful");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void chooseAction(String token, String gameName, IActionCommand actionCommand) throws RemoteException {
        if (!viewMap.containsKey(token))
            return;
        if(!gameManager.containsGame(gameName))
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
            game.userUseToolCard(token, toolCard, executorObserver);
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
     * Set a dice on a certain toolcard for its purpose.
     * This method assumes that game's toolcards contains the specified toolcard.
     *
     * @param token
     * @param gameName     game's name
     * @param dice         dice to set
     * @param toolCardName name of the ToolCard on which to place the dice
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
     * Set a a new value needed for a dice on a certain toolcard and its purpose.
     * This method assumes that game's toolcards contains the specified toolcard.
     *
     * @param token
     * @param gameName     game played
     * @param value        dice's value
     * @param toolCardName toolcard on which to place the value
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
     * Set a new color needed for a certain toolcard and its purpose.
     * This method assumes that game's toolcards contains the specified toolcard.
     *
     * @param token
     * @param gameName     game played
     * @param color        dice's value
     * @param toolCardName toolcard on which to place the color
     */
    @Override
    public void setColor(String token, String gameName, Color color, String toolCardName) throws RemoteException {
        if (!viewMap.containsKey(token) || !gameManager.containsGame(gameName))
            return;
        IGame game = gameManager.getGameByName(gameName);
        try {
            game.userFireExecutorEvent(token, new ColorExecutorEvent(color));
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set a new color needed for a certain toolcard and its purpose.
     * This method assumes that game's toolcards contains the specified toolcard.
     *
     * @param token
     * @param gameName     game played
     * @param position     position that needs to be set for the specified toolcard
     * @param toolCardName toolcard on which to set the posiiton
     * @throws RemoteException
     */
    @Override
    public void setPosition(String token, String gameName, Position position, String toolCardName) throws RemoteException {
        if (!viewMap.containsKey(token) || !gameManager.containsGame(gameName))
            return;
        IGame game = gameManager.getGameByName(gameName);
        try {
            game.userFireExecutorEvent(token, new PositionExecutorEvent(position));
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
    }
}
