package org.poianitibaldizhou.sagrada.game.controller;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.GameManager;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.view.IGameView;
import org.poianitibaldizhou.sagrada.lobby.view.ILobbyView;

import java.rmi.Remote;
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
    public synchronized void joinGame(Game game, IGameView view, String token) throws RemoteException {
        gameManager.addGame(game);
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
     * This method assumes that game's toolcards contains card.
     *
     * @param dice dice to set
     * @param game game played
     * @param card toolcard on which to place the dice
     * @throws RemoteException
     */
    @Override
    public void setDice(Dice dice, Game game, ToolCard card) throws RemoteException {
        List<ToolCard> toolCards = game.getToolCards();
        for(ToolCard toolCard: toolCards) {
            if(toolCard.equals(card)) {
                toolCard.setDice(dice);
            }
        }
    }
}
