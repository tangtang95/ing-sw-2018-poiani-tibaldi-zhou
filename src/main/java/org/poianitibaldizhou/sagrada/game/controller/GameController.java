package org.poianitibaldizhou.sagrada.game.controller;

import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.view.IGameView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;

public class GameController extends UnicastRemoteObject implements IGameController {

    private transient HashMap<String, IGameView> viewMap = new HashMap<>();
    private transient GameManager gameManager;

    public GameController() throws RemoteException {
        super();
        gameManager = new GameManager();
    }

    /**
     * Implements a player joining a certain game.
     *
     * @param game game to join
     * @param view player's view
     * @param token player's token
     * @throws RemoteException
     */
    @Override
    public synchronized void joinGame(Game game, IGameView view, String token, String gameName) throws RemoteException {
        gameManager.addGame(game, gameName);
        try {
            gameManager.joinGame(game,token);
        } catch(RemoteException re) {
            view.err("You are already in game");
            return;
        }
        viewMap.put(token,view);
    }

    /**
     * Set a dice on a certain toolcard for its purpose.
     * This method assumes that game's toolcards contains the specified toolcard.
     * @param dice dice to set
     * @param gameName game's name
     * @param toolCardName name of the ToolCard on which to place the dice
     */
    @Override
    public void setDice(Dice dice, String gameName, String toolCardName) {
        Game game = gameManager.getGameByName(gameName);
        List<ToolCard> toolCards = game.getToolCards();
        for(ToolCard toolCard: toolCards) {
            if(toolCard.getName().equals(toolCardName)){
                toolCard.getToolCardExecutorHelper().setNeededDice(dice);
            }
        }
    }

    /**
     * Set a a new value needed for a dice on a certain toolcard and its purpose.
     * This method assumes that game's toolcards contains the specified toolcard.
     *  @param value dice's value
     * @param gameName game played
     * @param toolCardName toolcard on which to place the value
     */
    @Override
    public void setNewValue(int value, String gameName, String toolCardName) {
        Game game = gameManager.getGameByName(gameName);
        List<ToolCard> toolCards = game.getToolCards();
        for(ToolCard tc: toolCards) {
            if(tc.getName().equals(toolCardName)) {
                tc.getToolCardExecutorHelper().setNeededValue(value);
            }
        }
    }

    /**
     * Set a new color needed for a certain toolcard and its purpose.
     * This method assumes that game's toolcards contains the specified toolcard.
     *  @param color dice's value
     * @param gameName game played
     * @param toolCardName toolcard on which to place the color
     */
    @Override
    public void setColor(Color color, String gameName, String toolCardName) {
        Game game = gameManager.getGameByName(gameName);
        List<ToolCard> toolCards = game.getToolCards();
        for(ToolCard tc : toolCards) {
            if(tc.getName().equals(toolCardName))
                tc.getToolCardExecutorHelper().setNeededColor(color);
        }
    }

    /**
     * Set a new color needed for a certain toolcard and its purpose.
     * This method assumes that game's toolcards contains the specified toolcard.
     * @param position position that needs to be set for the specified toolcard
     * @param gameName game played
     * @param toolCardName toolcard on which to set the posiiton
     * @throws RemoteException
     */
    @Override
    public void setPosition(Position position, String gameName, String toolCardName) {
        Game game = gameManager.getGameByName(gameName);
        List<ToolCard> toolCards = game.getToolCards();
        for(ToolCard tc : toolCards)
            if(tc.getName().equals(toolCardName))
                tc.getToolCardExecutorHelper().setNeededPosition(position);;

    }
}
