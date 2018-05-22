package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.exception.NoCoinsExpendableException;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ExecutorEvent;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;

import java.rmi.RemoteException;

public abstract class IStateGame {

    protected Game game;

    protected IStateGame(Game game){
        this.game = game;
    }

    public abstract void init();
    public void nextRound(){
        throw new IllegalStateException();
    }
    public boolean ready(String token, SchemaCard schemaCard){
        throw new IllegalStateException();
    }
    public void readyGame(String token){
        throw new IllegalStateException();
    }
    public boolean throwDices(Player player){ throw new IllegalStateException(); }
    public void choosePrivateObjectiveCard(Player player, PrivateObjectiveCard privateObjectiveCard) throws InvalidActionException {
        throw new IllegalStateException();
    }
    public void calculateVictoryPoints(){
        throw new IllegalStateException();
    }
    public void chooseAction(Player player, IActionCommand action) throws InvalidActionException {throw new IllegalStateException();}
    public void placeDice(Player player, Dice dice, int row, int column) throws RuleViolationException, InvalidActionException {
        throw new IllegalStateException();
    }
    public void useCard(Player player, ToolCard toolCard, IToolCardExecutorObserver observer) throws NoCoinsExpendableException, InvalidActionException, RemoteException, InterruptedException {
        throw new IllegalStateException();
    }

    public void fireExecutorEvent(ExecutorEvent event) throws InvalidActionException {
        throw new IllegalStateException();
    }

    public void releaseToolCardExecution() {
        throw new IllegalStateException();
    }

    public static IStateGame newInstance(IStateGame iStateGame) {throw new IllegalStateException(); }

    public void interruptToolCardExecution() {
        throw new IllegalStateException();
    }


}
