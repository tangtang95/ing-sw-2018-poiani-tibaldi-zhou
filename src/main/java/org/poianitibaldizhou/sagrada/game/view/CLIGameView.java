package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.Command;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.ScreenManager;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.NetworkManager;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CLIGameView extends CLIMenuView implements IGameView {
    private final transient Map<String, Command> commandMap = new HashMap<>();

    private List<ToolCard> toolCards;
    private List<PublicObjectiveCard> publicObjectiveCards;
    private Player player;
    private String gameName;
    private transient User currentUser;


    public CLIGameView(NetworkManager networkManager, ScreenManager screenManager)
            throws RemoteException {
        super(networkManager, screenManager);

    }

    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void run() {
        bufferManager.consolePrint("-----------------------------WELCOME-------------------------------",
                Level.LOW);


    }

    @Override
    public void ack(String ack) throws RemoteException {

    }

    @Override
    public void err(String err) throws RemoteException {

    }

    public String getGameName() {
        return gameName;
    }

    public Player getPlayer() {
        return player;
    }
}
